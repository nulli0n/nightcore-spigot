package su.nightexpress.nightcore.database;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.database.listener.UserListener;
import su.nightexpress.nightcore.util.Players;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractUserManager<P extends NightDataPlugin<U>, U extends DataUser> extends AbstractManager<P> {

    private final UserdataConfig config;

    private final Map<UUID, U>   loadedById;
    private final Map<String, U> loadedByName;
    private final Map<UUID, U>   scheduledSaves;

    public AbstractUserManager(@NotNull P plugin) {
        super(plugin);
        this.config = UserdataConfig.read(plugin);
        this.loadedById = new ConcurrentHashMap<>();
        this.loadedByName = new ConcurrentHashMap<>();
        this.scheduledSaves = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.addListener(new UserListener<>(this.plugin));
        this.addTask(this.plugin.createAsyncTask(this::saveScheduled).setTicksInterval(this.config.getScheduledSaveInterval()));
    }

    @Override
    protected void onShutdown() {
        this.saveAll();
        this.getLoadedByIdMap().clear();
        this.getLoadedByNameMap().clear();
    }

    @NotNull
    private AbstractUserDataHandler<?, U> getDataHandler() {
        return this.plugin.getData();
    }

    @NotNull
    public abstract U createUserData(@NotNull UUID uuid, @NotNull String name);

    public void loadOnlineUsers() {
        this.plugin.getServer().getOnlinePlayers().stream().map(Player::getUniqueId).forEach(id -> {
            U user = this.getUserData(id);
            if (user != null) this.cachePermanent(user);
        });
    }

    public void saveScheduled() {
        if (this.scheduledSaves.isEmpty()) return;

        Set<U> users = new HashSet<>();

        this.scheduledSaves.values().forEach(user -> {
            if (user.isAutoSaveReady()) {
                users.add(user);
            }
        });

        this.getDataHandler().saveUsers(users);

        users.forEach(user -> {
            user.cancelAutoSave(); // Reset autosave timestamp.
            user.setNextSyncIn(this.config.getScheduledSaveSyncPause()); // Unlock synchronization only when all data was pushed to the database.
        });

        this.scheduledSaves.values().removeAll(users);
    }

    public void saveAll() {
        Set<U> users = new HashSet<>();
        users.addAll(this.scheduledSaves.values());
        users.addAll(this.getLoaded());
        this.getDataHandler().saveUsers(users);
        this.scheduledSaves.values().forEach(DataUser::cancelAutoSave);
        this.scheduledSaves.clear();
    }

    @NotNull
    public final U getUserData(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        U user = this.getLoaded(uuid);
        if (user != null) return user;

        if (Players.isReal(player)) {
            user = this.getUserData(uuid);
            if (user != null) {
                if (CoreConfig.USER_DEBUG_ENABLED.get()) {
                    new Throwable().printStackTrace();
                    this.plugin.warn("Main thread user data load for '" + uuid + "' aka '" + player.getName() + "'.");
                }
                return user;
            }
        }

        return this.createUserData(uuid, player.getName());
    }

    @Nullable
    public final U getUserData(@NotNull String name) {
        Player player = Players.getPlayer(name);
        if (player != null) return this.getUserData(player);

        U user = this.getLoaded(name);
        if (user != null) return user;

        user = this.plugin.getData().getUser(name);
        if (user != null) {
            user.onLoad();
            //this.plugin.debug("Loaded by name from DB: " + user.getName());
            this.cacheTemporary(user);
        }

        return user;
    }

    @Nullable
    public final U getUserData(@NotNull UUID uuid) {
        U user = this.getLoaded(uuid);
        if (user != null) return user;

        user = this.plugin.getData().getUser(uuid);
        if (user != null) {
            user.onLoad();
            //this.plugin.debug("Loaded by UUID from DB: " + user.getName());
            this.cacheTemporary(user);
        }

        return user;
    }

    public final CompletableFuture<U> getUserDataAsync(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> this.getUserData(name));
    }

    public final CompletableFuture<U> getUserDataAsync(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.getUserData(uuid));
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

    private void manageUser(@NotNull Supplier<U> loadedSupplier, @NotNull Supplier<CompletableFuture<U>> fetchSupplier, @NotNull Consumer<U> consumer) {
        U user = loadedSupplier.get();
        if (user != null) {
            consumer.accept(user);
        }
        else fetchSupplier.get().thenAccept(consumer);
    }

    private void manageUserSynchronized(@NotNull Supplier<U> loadedSupplier, @NotNull Supplier<CompletableFuture<U>> fetchSupplier, @NotNull Consumer<U> consumer) {
        this.manageUser(loadedSupplier, fetchSupplier, user -> this.plugin.runTask(task -> consumer.accept(user)));
    }

    @Deprecated
    public void getUserDataAndPerform(@NotNull String name, Consumer<U> consumer) {
        this.manageUserSynchronized(name, consumer);
    }

    @Deprecated
    public void getUserDataAndPerform(@NotNull UUID uuid, Consumer<U> consumer) {
        this.manageUserSynchronized(uuid, consumer);
    }

    @Deprecated
    public void getUserDataAndPerformAsync(@NotNull String name, Consumer<U> consumer) {
        this.manageUser(name, consumer);
    }

    @Deprecated
    public void getUserDataAndPerformAsync(@NotNull UUID uuid, Consumer<U> consumer) {
        this.manageUser(uuid, consumer);
    }

    public final void unload(@NotNull Player player) {
        this.unload(player.getUniqueId());
    }

    public final void unload(@NotNull UUID uuid) {
        U user = this.getLoadedByIdMap().get(uuid);
        if (user == null) return;

        this.unload(user);
    }

    public void unload(@NotNull U user) {
        Player player = user.getPlayer();
        if (player != null) {
            user.setName(player.getName());
            user.setLastOnline(System.currentTimeMillis());
        }
        this.scheduleSave(user);
        this.cacheTemporary(user);
    }

    public void save(@NotNull U user) {
        this.plugin.getData().saveUser(user);
    }

    public void scheduleSave(@NotNull U user) {
        user.setAutoSaveIn(this.config.getScheduledSaveDelay());
        user.cancelSynchronization();
        this.scheduledSaves.put(user.getId(), user);
    }

    public boolean isScheduledToSave(@NotNull U user) {
        return this.scheduledSaves.containsKey(user.getId());
    }

    @Deprecated
    public void saveAsync(@NotNull U user) {
        this.scheduleSave(user);
    }

    @NotNull
    public Set<U> getAllUsers() {
        Map<UUID, U> users = new HashMap<>();
        this.getLoaded().forEach(user -> users.put(user.getId(), user));
        this.plugin.getData().getUsers().forEach(user -> {
            users.putIfAbsent(user.getId(), user);
        });
        return new HashSet<>(users.values());
    }

    @NotNull
    public Map<UUID, U> getLoadedByIdMap() {
        this.removeExpired(this.loadedById.values());

        return this.loadedById;
    }

    @NotNull
    public Map<String, U> getLoadedByNameMap() {
        this.removeExpired(this.loadedByName.values());

        return loadedByName;
    }

    private void removeExpired(@NotNull Collection<U> collection) {
        collection.removeIf(user -> {
            if (user.isCacheExpired()) {
                user.onUnload();
                //this.plugin.debug("Cache expired: " + user.getName());
                return true;
            }
            return false;
        });
    }

    @NotNull
    public Set<U> getLoaded() {
        return new HashSet<>(this.getLoadedByIdMap().values());
    }

    @Nullable
    public U getLoaded(@NotNull UUID uuid) {
        return this.getLoadedByIdMap().get(uuid);
    }

    @Nullable
    public U getLoaded(@NotNull String name) {
        return this.getLoadedByNameMap().get(name.toLowerCase());
    }

    public boolean isLoaded(@NotNull Player player) {
        return this.isLoaded(player.getUniqueId());
    }

    public boolean isLoaded(@NotNull UUID id) {
        return this.getLoadedByIdMap().containsKey(id);
    }

    public boolean isLoaded(@NotNull String name) {
        return this.getLoadedByNameMap().containsKey(name.toLowerCase());
    }

    public boolean isCreated(@NotNull String name) {
        return this.plugin.getData().isUserExists(name);
    }

    public boolean isCreated(@NotNull UUID uuid) {
        return this.plugin.getData().isUserExists(uuid);
    }

    public void cacheTemporary(@NotNull U user) {
        user.setCachedUntil(System.currentTimeMillis() + CoreConfig.USER_CACHE_LIFETIME.get() * 1000L);
        this.cache(user);
        //this.plugin.debug("Temp user cache: " + user.getName());
    }

    public void cachePermanent(@NotNull U user) {
        user.setCachedUntil(-1);
        this.cache(user);
        //this.plugin.debug("Permanent user cache: " + user.getName());
    }

    private void cache(@NotNull U user) {
        this.getLoadedByIdMap().putIfAbsent(user.getId(), user);
        this.getLoadedByNameMap().putIfAbsent(user.getName().toLowerCase(), user);
    }
}
