package su.nightexpress.nightcore.db;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.UserdataConfig;
import su.nightexpress.nightcore.db.listener.UserListener;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.Players;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Deprecated
public abstract class AbstractUserManager<P extends NightPlugin, U extends AbstractUser> extends AbstractManager<P> {

    protected final UserdataConfig                config;
    protected final AbstractUserDataManager<P, U> dataManager;

    private final Map<UUID, U>   loadedByIdMap;
    private final Map<String, U> loadedByNameMap;

    public AbstractUserManager(@NotNull P plugin, @NotNull AbstractUserDataManager<P, U> dataManager) {
        super(plugin);
        this.config = UserdataConfig.read(plugin);
        this.dataManager = dataManager;
        this.loadedByIdMap = new ConcurrentHashMap<>();
        this.loadedByNameMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.addListener(new UserListener<>(this.plugin, this));
        this.addAsyncTask(this::saveScheduled, this.config.getSaveInterval());
        this.addAsyncTask(this::unloadExpired, this.config.getCacheCleanupInterval());

        this.plugin.onPostLoad(this::loadOnline);
    }

    @Override
    protected void onShutdown() {
        this.saveLoaded();
        this.loadedByIdMap.clear();
        this.loadedByNameMap.clear();
    }

    protected void onLoad(@NotNull U user) {

    }

    protected void onUnload(@NotNull U user) {

    }

    public void loadOnline() {
        Players.getOnline().forEach(player -> {
            U user = this.getOrFetch(player.getUniqueId());
            if (user != null) this.cachePermanent(user);
        });
    }

    public void unloadExpired() {
        this.getLoaded().forEach(user -> {
            if (user.isCacheExpired() && !user.isOnline()) {
                this.unload(user);
            }
        });
    }

    public final void handleJoin(@NotNull Player player) {
        U user = this.getLoaded(player);
        if (user == null) return;

        user.setName(player.getName()); // Update name

        this.cachePermanent(user);
    }

    public final void handleQuit(@NotNull Player player) {
        U user = this.getLoaded(player);
        if (user == null) return;

        user.setLastOnline(System.currentTimeMillis());

        // Force save data on quit + disable auto-save and delay synchronization.
        if (user.isAutoSavePlanned()) {
            this.plugin.runTaskAsync(() -> this.saveScheduled(Collections.singletonList(user)));
        }
        else {
            this.plugin.runTaskAsync(() -> this.dataManager.saveUserCommons(user));
        }

        this.cacheTemporary(user);
    }

    public void saveScheduled() {
        Set<U> users = this.getLoaded().stream().filter(AbstractUser::isAutoSaveReady).collect(Collectors.toCollection(HashSet::new));
        this.saveScheduled(users);
    }

    private void saveScheduled(@NotNull Collection<U> users) {
        if (users.isEmpty()) return;

        this.dataManager.saveUsersFully(users);

        users.forEach(user -> {
            user.disableAutoSave(); // Reset autosave timestamp.
            user.setAutoSyncIn(this.config.getSaveSyncPause()); // Unlock synchronization only when all data was pushed to the database.
        });
    }

    public void saveLoaded() {
        this.dataManager.saveUsersCommons(this.getLoaded());
    }

    public void save(@NotNull Player player) {
        U user = this.getLoaded(player.getUniqueId());
        if (user == null) return;

        this.save(user);
    }

    public void save(@NotNull U user) {
        user.setAutoSaveIn(this.config.getSaveDelay());
        user.disableAutoSync();
    }

    private void load(@NotNull U user) {
        user.onLoad();
        this.cacheTemporary(user);
        this.onLoad(user);
    }

    private void unload(@NotNull U user) {
        user.onUnload();
        this.onUnload(user);

        this.loadedByIdMap.remove(user.getId());
        this.loadedByNameMap.remove(user.getName().toLowerCase());
    }

    @NotNull
    public abstract U create(@NotNull UUID uuid, @NotNull String name);

    public void cacheTemporary(@NotNull U user) {
        user.setCacheFor(this.config.getCacheLifetime());
        this.cache(user);
    }

    public void cachePermanent(@NotNull U user) {
        user.setPermanentCache();
        this.cache(user);
    }

    private void cache(@NotNull U user) {
        this.loadedByIdMap.putIfAbsent(user.getId(), user);
        this.loadedByNameMap.putIfAbsent(user.getName().toLowerCase(), user);
    }

    public boolean isInDatabase(@NotNull String name) {
        return this.dataManager.isUserExists(name);
    }

    public boolean isInDatabase(@NotNull UUID uuid) {
        return this.dataManager.isUserExists(uuid);
    }

    public void addInDatabase(@NotNull U user) {
        this.dataManager.insertUser(user);
    }

    public void saveInDatabase(@NotNull U user) {
        this.dataManager.saveUserFully(user);
    }

    @Nullable
    public U getFromDatabase(@NotNull String name) {
        return this.dataManager.getUser(name);
    }

    @Nullable
    public U getFromDatabase(@NotNull UUID uuid) {
        return this.dataManager.getUser(uuid);
    }

    @NotNull
    public final U getOrFetch(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        U user = this.getLoaded(uuid);
        if (user != null) return user;

        if (Players.isReal(player)) {
            user = this.getOrFetch(uuid);
            if (user != null) {
                this.plugin.warn("Main thread user data load for '" + uuid + "' aka '" + player.getName() + "'.");
                return user;
            }
        }

        return this.create(uuid, player.getName());
    }

    @Nullable
    public final U getOrFetch(@NotNull String name) {
        U user = this.getLoaded(name);
        if (user != null) return user;

        user = this.getFromDatabase(name);
        if (user != null) {
            this.load(user);
        }

        return user;
    }

    @Nullable
    public final U getOrFetch(@NotNull UUID uuid) {
        U user = this.getLoaded(uuid);
        if (user != null) return user;

        user = this.getFromDatabase(uuid);
        if (user != null) {
            this.load(user);
        }

        return user;
    }

    public final CompletableFuture<U> getUserDataAsync(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> this.getOrFetch(name));
    }

    public final CompletableFuture<U> getUserDataAsync(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.getOrFetch(uuid));
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation in async CompletableFuture thread.
     * @param name Name of a player.
     */
    public void manageUser(@NotNull String name, Consumer<U> consumer) {
        this.manageUser(() -> this.getLoaded(name), () -> this.getUserDataAsync(name), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation in async CompletableFuture thread.
     * @param playerId UUID of a player.
     */
    public void manageUser(@NotNull UUID playerId, Consumer<U> consumer) {
        this.manageUser(() -> this.getLoaded(playerId), () -> this.getUserDataAsync(playerId), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation next tick in the main thread.
     * @param name Name of a player.
     */
    public void manageUserSynchronized(@NotNull String name, Consumer<U> consumer) {
        this.manageUserSynchronized(() -> this.getLoaded(name), () -> this.getUserDataAsync(name), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation next tick in the main thread.
     * @param playerId UUID of a player.
     */
    public void manageUserSynchronized(@NotNull UUID playerId, Consumer<U> consumer) {
        this.manageUserSynchronized(() -> this.getLoaded(playerId), () -> this.getUserDataAsync(playerId), consumer);
    }

    private void manageUserSynchronized(@NotNull Supplier<U> loadedSupplier, @NotNull Supplier<CompletableFuture<U>> fetchSupplier, @NotNull Consumer<U> consumer) {
        this.manageUser(loadedSupplier, fetchSupplier, user -> this.plugin.runTask(() -> consumer.accept(user)));
    }

    public void manageUser(@NotNull Player player, Consumer<U> consumer) {
        this.manageUser(player, consumer, false);
    }

    public void manageUserWithSave(@NotNull Player player, Consumer<U> consumer) {
        this.manageUser(player, consumer, true);
    }

    private void manageUser(@NotNull Player player, Consumer<U> consumer, boolean save) {
        U user = this.getLoaded(player);
        if (user == null) return;

        consumer.accept(user);
        if (save) this.save(user);
    }

    private void manageUser(@NotNull Supplier<U> loadedSupplier, @NotNull Supplier<CompletableFuture<U>> fetchSupplier, @NotNull Consumer<U> consumer) {
        U user = loadedSupplier.get();
        if (user != null) {
            consumer.accept(user);
        }
        else fetchSupplier.get().thenAccept(consumer);
    }

    /**
     * @return Fetches all users from the database and returns them in as a single set.<br>
     * Users that are already loaded won't be replaced by ones from the database, however they will be fetched anyway.
     */
    @NotNull
    public Set<U> getAll() {
        Set<U> users = this.getLoaded();
        this.dataManager.getUsers().stream().filter(user -> !this.isLoaded(user.getId())).forEach(users::add);
        return users;
    }

    @NotNull
    public Set<U> getLoaded() {
        return new HashSet<>(this.loadedByIdMap.values());
    }

    @NotNull
    public Map<UUID, U> getLoadedByIdMap() {
        return this.loadedByIdMap;
    }

    @NotNull
    public Map<String, U> getLoadedByNameMap() {
        return this.loadedByNameMap;
    }

    @Nullable
    public U getLoaded(@NotNull Player player) {
        return this.getLoaded(player.getUniqueId());
    }

    @Nullable
    public U getLoaded(@NotNull UUID uuid) {
        return this.loadedByIdMap.get(uuid);
    }

    @Nullable
    public U getLoaded(@NotNull String name) {
        U byName = this.loadedByNameMap.get(name.toLowerCase());
        if (byName != null) return byName;

        Player player = Players.getPlayer(name);
        return player == null ? null : this.getLoaded(player.getUniqueId());
    }

    public boolean isLoaded(@NotNull Player player) {
        return this.isLoaded(player.getUniqueId());
    }

    public boolean isLoaded(@NotNull UUID id) {
        return this.loadedByIdMap.containsKey(id);
    }

    public boolean isLoaded(@NotNull String name) {
        return this.getLoaded(name) != null;
    }
}
