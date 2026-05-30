package su.nightexpress.nightcore.database;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

@Deprecated
public abstract class AbstractUserManager<P extends NightDataPlugin<U>, U extends DataUser> extends AbstractManager<P> {

    private final UserdataConfig config;

    private final Map<UUID, U>   loadedById;
    private final Map<String, U> loadedByName;
    private final Map<UUID, U>   scheduledSaves;

    public AbstractUserManager(@NonNull P plugin) {
        super(plugin);
        this.config = UserdataConfig.read(plugin);
        this.loadedById = new ConcurrentHashMap<>();
        this.loadedByName = new ConcurrentHashMap<>();
        this.scheduledSaves = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.addListener(new UserListener<>(this.plugin));
        this.addTask(this.plugin.createAsyncTask(this::saveScheduled).setTicksInterval(this.config.getSaveInterval()));
    }

    @Override
    protected void onShutdown() {
        this.saveAll();
        this.getLoadedByIdMap().clear();
        this.getLoadedByNameMap().clear();
    }

    @NonNull
    private AbstractUserDataHandler<?, U> getDataHandler() {
        return this.plugin.getData();
    }

    @NonNull
    public abstract U createUserData(@NonNull UUID uuid, @NonNull String name);

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
            user.setNextSyncIn(this.config.getSaveSyncPause()); // Unlock synchronization only when all data was pushed to the database.
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

    @NonNull
    public final U getUserData(@NonNull Player player) {
        UUID uuid = player.getUniqueId();

        U user = this.getLoaded(uuid);
        if (user != null) return user;

        if (Players.isReal(player)) {
            user = this.getUserData(uuid);
            if (user != null) {
                //                if (CoreConfig.USER_DEBUG_ENABLED.get()) {
                //                    new Throwable().printStackTrace();
                //                    this.plugin.warn("Main thread user data load for '" + uuid + "' aka '" + player.getName() + "'.");
                //                }
                return user;
            }
        }

        return this.createUserData(uuid, player.getName());
    }

    @Nullable
    public final U getUserData(@NonNull String name) {
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
    public final U getUserData(@NonNull UUID uuid) {
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

    public final CompletableFuture<U> getUserDataAsync(@NonNull String name) {
        return CompletableFuture.supplyAsync(() -> this.getUserData(name));
    }

    public final CompletableFuture<U> getUserDataAsync(@NonNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.getUserData(uuid));
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation in async CompletableFuture thread.
     * 
     * @param name Name of a player.
     */
    public void manageUser(@NonNull String name, Consumer<U> consumer) {
        this.manageUser(() -> this.getLoaded(name), () -> this.getUserDataAsync(name), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation in async CompletableFuture thread.
     * 
     * @param playerId UUID of a player.
     */
    public void manageUser(@NonNull UUID playerId, Consumer<U> consumer) {
        this.manageUser(() -> this.getLoaded(playerId), () -> this.getUserDataAsync(playerId), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation next tick in the main thread.
     * 
     * @param name Name of a player.
     */
    public void manageUserSynchronized(@NonNull String name, Consumer<U> consumer) {
        this.manageUserSynchronized(() -> this.getLoaded(name), () -> this.getUserDataAsync(name), consumer);
    }

    /**
     * Performs an operation on the given user.<br>
     * Runs immediately in the current thread if player is online or data is already loaded.<br>
     * Otherwise fetches player data asynchronously and performs an operation next tick in the main thread.
     * 
     * @param playerId UUID of a player.
     */
    public void manageUserSynchronized(@NonNull UUID playerId, Consumer<U> consumer) {
        this.manageUserSynchronized(() -> this.getLoaded(playerId), () -> this.getUserDataAsync(playerId), consumer);
    }

    private void manageUser(@NonNull Supplier<U> loadedSupplier, @NonNull Supplier<CompletableFuture<U>> fetchSupplier,
                            @NonNull Consumer<U> consumer) {
        U user = loadedSupplier.get();
        if (user != null) {
            consumer.accept(user);
        }
        else fetchSupplier.get().thenAccept(consumer);
    }

    private void manageUserSynchronized(@NonNull Supplier<U> loadedSupplier,
                                        @NonNull Supplier<CompletableFuture<U>> fetchSupplier,
                                        @NonNull Consumer<U> consumer) {
        this.manageUser(loadedSupplier, fetchSupplier, user -> this.plugin.runTask(() -> consumer.accept(user)));
    }

    @Deprecated
    public void getUserDataAndPerform(@NonNull String name, Consumer<U> consumer) {
        this.manageUserSynchronized(name, consumer);
    }

    @Deprecated
    public void getUserDataAndPerform(@NonNull UUID uuid, Consumer<U> consumer) {
        this.manageUserSynchronized(uuid, consumer);
    }

    @Deprecated
    public void getUserDataAndPerformAsync(@NonNull String name, Consumer<U> consumer) {
        this.manageUser(name, consumer);
    }

    @Deprecated
    public void getUserDataAndPerformAsync(@NonNull UUID uuid, Consumer<U> consumer) {
        this.manageUser(uuid, consumer);
    }

    public final void unload(@NonNull Player player) {
        this.unload(player.getUniqueId());
    }

    public final void unload(@NonNull UUID uuid) {
        U user = this.getLoadedByIdMap().get(uuid);
        if (user == null) return;

        this.unload(user);
    }

    public void unload(@NonNull U user) {
        Player player = user.getPlayer();
        if (player != null) {
            user.setName(player.getName());
            user.setLastOnline(System.currentTimeMillis());
        }
        if (this.isScheduledToSave(user)) {
            this.scheduledSaves.remove(user.getId());
        }
        this.plugin.runTaskAsync(() -> this.save(user));
        this.cacheTemporary(user);
    }

    public void save(@NonNull U user) {
        this.plugin.getData().saveUser(user);
    }

    public void scheduleSave(@NonNull U user) {
        user.setAutoSaveIn(this.config.getSaveDelay());
        user.cancelSynchronization();
        this.scheduledSaves.put(user.getId(), user);
    }

    public boolean isScheduledToSave(@NonNull U user) {
        return this.scheduledSaves.containsKey(user.getId());
    }

    @Deprecated
    public void saveAsync(@NonNull U user) {
        this.scheduleSave(user);
    }

    @NonNull
    public Set<U> getAllUsers() {
        Map<UUID, U> users = new HashMap<>();
        this.getLoaded().forEach(user -> users.put(user.getId(), user));
        this.plugin.getData().getUsers().forEach(user -> {
            users.putIfAbsent(user.getId(), user);
        });
        return new HashSet<>(users.values());
    }

    @NonNull
    public Map<UUID, U> getLoadedByIdMap() {
        this.removeExpired(this.loadedById.values());

        return this.loadedById;
    }

    @NonNull
    public Map<String, U> getLoadedByNameMap() {
        this.removeExpired(this.loadedByName.values());

        return loadedByName;
    }

    private void removeExpired(@NonNull Collection<U> collection) {
        collection.removeIf(user -> {
            if (user.isCacheExpired()) {
                user.onUnload();
                //this.plugin.debug("Cache expired: " + user.getName());
                return true;
            }
            return false;
        });
    }

    @NonNull
    public Set<U> getLoaded() {
        return new HashSet<>(this.getLoadedByIdMap().values());
    }

    @Nullable
    public U getLoaded(@NonNull UUID uuid) {
        return this.getLoadedByIdMap().get(uuid);
    }

    @Nullable
    public U getLoaded(@NonNull String name) {
        return this.getLoadedByNameMap().get(name.toLowerCase());
    }

    public boolean isLoaded(@NonNull Player player) {
        return this.isLoaded(player.getUniqueId());
    }

    public boolean isLoaded(@NonNull UUID id) {
        return this.getLoadedByIdMap().containsKey(id);
    }

    public boolean isLoaded(@NonNull String name) {
        return this.getLoadedByNameMap().containsKey(name.toLowerCase());
    }

    public boolean isCreated(@NonNull String name) {
        return this.plugin.getData().isUserExists(name);
    }

    public boolean isCreated(@NonNull UUID uuid) {
        return this.plugin.getData().isUserExists(uuid);
    }

    public void cacheTemporary(@NonNull U user) {
        user.setCachedUntil(System.currentTimeMillis() + CoreConfig.USER_CACHE_LIFETIME.get() * 1000L);
        this.cache(user);
        //this.plugin.debug("Temp user cache: " + user.getName());
    }

    public void cachePermanent(@NonNull U user) {
        user.setCachedUntil(-1);
        this.cache(user);
        //this.plugin.debug("Permanent user cache: " + user.getName());
    }

    private void cache(@NonNull U user) {
        this.getLoadedByIdMap().putIfAbsent(user.getId(), user);
        this.getLoadedByNameMap().putIfAbsent(user.getName().toLowerCase(), user);
    }
}
