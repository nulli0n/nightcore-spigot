package su.nightexpress.nightcore.user;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.user.cache.UserRepository;
import su.nightexpress.nightcore.user.data.UserDataAccessor;
import su.nightexpress.nightcore.user.data.UserDataSettings;
import su.nightexpress.nightcore.user.listener.UserListener;
import su.nightexpress.nightcore.util.Players;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class AbstractUserManager<P extends NightPlugin, U extends UserTemplate> extends AbstractManager<P> {

    protected final UserDataAccessor<U> dataAccessor;
    protected final UserRepository<U>   repository;
    protected final UserDataSettings    settings;

    public AbstractUserManager(@NotNull P plugin, @NotNull UserDataAccessor<U> dataAccessor) {
        super(plugin);
        this.dataAccessor = dataAccessor;
        this.repository = new UserRepository<>();
        this.settings = new UserDataSettings();
    }

    @Override
    protected void onLoad() {
        this.settings.load(this.plugin.getEngineConfig());

        this.addListener(new UserListener<>(this.plugin, this));
        this.addAsyncTask(this::saveDirty, this.settings.getSaveInterval());
        this.addAsyncTask(this.repository::cleanExpired, this.settings.getCacheCleanupInterval());

        this.plugin.runTaskAsync(() -> {
            this.dataAccessor.loadProfiles().forEach(userInfo -> this.repository.addNameIdMapping(userInfo.name(), userInfo.id()));
        });

        this.plugin.onPostLoad(() -> {
            this.loadOnline();
            this.dataAccessor.addSynchronization(this::synchronizeUserData);
        });
    }

    @Override
    protected void onShutdown() {
        this.saveDirty();
        this.repository.clear();
    }

    private void loadOnline() {
        Players.getOnline().stream().map(Entity::getUniqueId).map(this::getOrFetch).filter(Optional::isPresent).map(Optional::get).forEach(this::cachePermanent);
    }

    private void synchronizeUserData(@NotNull U user) {
        // Temporary cache new users that were created on another server.
        if (!this.repository.contains(user.getId())) {
            this.cacheTemporary(user);
            return;
        }

        this.repository.getById(user.getId()).ifPresent(cached -> {
            this.synchronize(user, cached);
        });
    }

    @NotNull
    protected abstract U create(@NotNull UUID uuid, @NotNull String name, @NotNull InetAddress address);

    protected abstract void synchronize(@NotNull U fetched, @NotNull U cached);

    @NotNull
    public UserDataAccessor<U> getDataAccessor() {
        return this.dataAccessor;
    }

    @NotNull
    public UserRepository<U> getRepository() {
        return this.repository;
    }

    private void cacheTemporary(@NotNull U user) {
        this.repository.addTemporary(user, this.settings.getCacheLifetime());
    }

    private void cachePermanent(@NotNull U user) {
        this.repository.addPermanent(user);
    }

    public final void handlePreLogin(@NotNull AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        UUID uuid = event.getUniqueId();
        if (!this.dataAccessor.isExists(uuid)) {
            U user = this.create(uuid, event.getName(), event.getAddress());
            this.dataAccessor.insert(user);
            this.cacheTemporary(user); // Cache temporary because player can cancel connection.
            return;
        }

        this.getOrFetch(uuid); // Fetch and cache temporary.
    }

    public final void handleJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.repository.getById(player.getUniqueId()).ifPresent(user -> {
            user.setName(player.getName());
            this.cachePermanent(user);
        });
    }

    public final void handleQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.repository.getById(player.getUniqueId()).ifPresent(user -> {
            if (user.isDirty()) {
                this.plugin.runTaskAsync(() -> this.dataAccessor.update(user));
                user.markClean();
            }
            else {
                this.plugin.runTaskAsync(() -> this.dataAccessor.tinyUpdate(user));
            }

            this.cacheTemporary(user); // Cache temporary so user data is still accessible for a while.
        });
    }

    public void saveDirty() {
        Set<U> users = this.repository.getAll().stream().filter(UserTemplate::isDirty).peek(UserTemplate::markClean).collect(Collectors.toSet());
        if (users.isEmpty()) return;

        this.dataAccessor.update(users);
    }

    @NotNull
    public final U getOrFetch(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        U byId = this.repository.getById(uuid).orElse(null);
        if (byId != null) return byId;

        U fromDb = this.dataAccessor.loadById(uuid).orElse(null);
        if (fromDb != null) {
            this.plugin.warn("User data for '%s' was present, but not loaded.");
            this.cachePermanent(fromDb);
            return fromDb;
        }

        InetAddress address = Optional.ofNullable(player.getAddress()).map(InetSocketAddress::getAddress).orElse(null);
        if (address == null) throw new IllegalStateException("%s is not a real player?".formatted(player));

        return this.create(uuid, player.getName(), address);
    }

    @NotNull
    public final Optional<U> getOrFetch(@NotNull String name) {
        Player player = Players.getPlayer(name);
        if (player != null) return this.getOrFetch(player.getUniqueId());

        Optional<U> byName = this.repository.getByName(name);
        if (byName.isPresent()) return byName;

        Optional<U> fromDb = this.dataAccessor.loadByName(name);
        if (fromDb.isPresent()) {
            this.cacheTemporary(fromDb.get());
            return fromDb;
        }

        return Optional.empty();
    }

    @NotNull
    public final Optional<U> getOrFetch(@NotNull UUID uuid) {
        Optional<U> byName = this.repository.getById(uuid);
        if (byName.isPresent()) return byName;

        Optional<U> fromDb = this.dataAccessor.loadById(uuid);
        if (fromDb.isPresent()) {
            this.cacheTemporary(fromDb.get());
            return fromDb;
        }

        return Optional.empty();
    }


    @NotNull
    public final CompletableFuture<Optional<U>> loadByIdAsync(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.getOrFetch(uuid));
    }

    @NotNull
    public final CompletableFuture<Optional<U>> loadByNameAsync(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> this.getOrFetch(name));
    }

    /**
     * @return Fetches all users from the database and returns them in as a single set.<br>
     * Users that are already loaded won't be replaced by ones from the database, however they will be fetched anyway.
     */
    @NotNull
    public Set<U> getAll() {
        Set<U> users = this.repository.getAll();
        this.dataAccessor.loadAll().stream().filter(user -> !this.repository.contains(user.getId())).forEach(users::add);
        return users;
    }
}
