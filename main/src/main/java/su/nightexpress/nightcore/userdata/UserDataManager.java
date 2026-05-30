package su.nightexpress.nightcore.userdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.db.AbstractDatabaseManager;
import su.nightexpress.nightcore.db.data.AbstractBaseDataManager;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.util.Players;

public class UserDataManager extends AbstractBaseDataManager<UserData> {

    private final AbstractDatabaseManager<?> database;
    private final UserDataCache              cache;
    private final UserDataSettings           settings;

    private final List<UserLoginListener> loginListeners;

    private Table userTable;

    public UserDataManager(@NonNull NightCore plugin, @NonNull AbstractDatabaseManager<?> dataHandler) {
        super(plugin);
        this.database = dataHandler;
        this.cache = new UserDataCache();
        this.settings = new UserDataSettings();

        this.loginListeners = new ArrayList<>();
    }

    @Override
    public @NonNull UserDataCache getCache() {
        return this.cache;
    }

    @Override
    public @NonNull UserDataSettings getSettings() {
        return this.settings;
    }

    @Override
    protected void initialize() {
        this.loadSettings();
        this.loadSchema();

        this.addListener(new UserDataListener(this.plugin, this));

        if (this.settings.getLoadAllUsers()) {
            this.asyncExecutor.execute(this::loadAll);
        }
        else {
            this.loadOnline();
        }
    }

    private void loadSettings() {
        FileConfig config = FileConfig.load(this.plugin.dataPath().resolve(UserDataFiles.FILE_SETTINGS));
        this.settings.load(config);
        config.saveChanges();
    }

    private void loadSchema() {
        this.userTable = Table.builder(this.database.getTablePrefix() + "_" + this.settings.getTableName())
            .withColumn(UserDataQueries.USER_ID_COLUMN)
            .withColumn(UserDataQueries.USER_NAME_COLUMN)
            .withColumn(UserDataQueries.LAST_SKIN_COLUMN)
            .withColumn(UserDataQueries.LAST_SEEN_COLUMN)
            .build();

        this.database.createTable(this.userTable);
    }

    public void loadAll() {
        this.selectAll().forEach(this::cachePermanently);
    }

    private void loadOnline() {
        Players.stream()
            .map(Player::getUniqueId)
            .map(this::selectById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(this::cachePermanently);
    }

    public final @NonNull UserData getData(@NonNull Player player) {
        UUID uuid = player.getUniqueId();

        UserData byId = this.cache.getById(uuid);
        if (byId != null) return byId;

        // Try load user data if it wasn't cached for some reason.
        CompletableFuture.supplyAsync(() -> this.selectById(uuid)).thenAccept(opt -> {
            if (opt.isEmpty()) return;

            UserData data = opt.get();
            this.plugin.warn("User data for '%s' was present, but not loaded.");
            this.cachePermanently(data);
        });

        // Fallback until real data is loaded.
        UserData data = UserData.of(player);
        this.cachePermanently(data);
        return data;
    }

    public @NonNull Optional<UserData> getById(@NonNull UUID playerId) {
        return this.cache.byId(playerId);
    }

    public @NonNull Optional<UserData> getByName(@NonNull String name) {
        return this.cache.byName(name);
    }

    public final @NonNull CompletableFuture<Optional<UserData>> loadByIdAndCacheAsync(@NonNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.loadByIdAndCache(uuid));
    }

    public final @NonNull CompletableFuture<Optional<UserData>> loadByNameAndCacheAsync(@NonNull String name) {
        return CompletableFuture.supplyAsync(() -> this.loadByNameAndCache(name));
    }

    public final @NonNull Optional<UserData> loadByIdAndCache(@NonNull UUID uuid) {
        UserData data = this.cache.getById(uuid);
        if (data != null) return Optional.of(data);

        Optional<UserData> optional = this.selectById(uuid);
        optional.ifPresent(this::cacheTemporary);
        return optional;
    }

    public final @NonNull Optional<UserData> loadByNameAndCache(@NonNull String name) {
        UserData data = this.cache.getByName(name);
        if (data != null) return Optional.of(data);

        Optional<UserData> optional = this.selectByName(name);
        optional.ifPresent(this::cacheTemporary);
        return optional;
    }

    /**
     * @return Fetches all users from the database and returns them in as a single set.<br>
     *         Users that are already loaded won't be replaced by ones from the database, however they will be fetched
     *         anyway.
     */
    public @NonNull Set<UserData> getAll() {
        Set<UserData> users = this.cache.getAll();
        this.selectAll().stream()
            .filter(user -> !this.cache.contains(user.getId()))
            .forEach(users::add);
        return users;
    }

    public void addLoginListener(@NonNull UserLoginListener listener) {
        this.loginListeners.add(listener);
    }

    public void removeLoginListener(@NonNull UserLoginListener listener) {
        this.loginListeners.remove(listener);
    }

    public void clearLoginListeners() {
        this.loginListeners.clear();
    }

    public void handlePreLoginEvent(@NonNull AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        UUID uuid = event.getUniqueId();
        if (!this.cache.contains(uuid)) {
            if (!this.isExists(uuid)) {
                UserData data = new UserData(uuid, event.getName());
                this.database.insert(this.userTable, UserDataQueries.USER_INSERT, data);
                this.cacheTemporary(data); // Cache temporary because player can cancel connection.
                return;
            }

            this.selectById(uuid).ifPresent(this::cacheTemporary);
        }

        this.loginListeners.forEach(listener -> listener.onPreLogin(event));
    }

    public void handleJoinEvent(@NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.cache.byId(player.getUniqueId()).ifPresent(data -> {
            data.update(player);
            data.updateLastSeen();
            this.loginListeners.forEach(listener -> listener.onJoin(event));
            this.cachePermanently(data);
        });
    }

    public void handleQuitEvent(@NonNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.cache.byId(player.getUniqueId()).ifPresent(data -> {
            data.updateLastSeen();
            this.loginListeners.forEach(listener -> listener.onQuit(event));
            this.cacheTemporary(data); // Cache temporary so user data is still accessible for a while.
        });
    }


    public boolean isExists(@NonNull UUID playerId) {
        Wheres<Object> wheres = Wheres
            .whereUUID(UserDataQueries.USER_ID_COLUMN, o -> playerId);

        return this.database.contains(this.userTable, wheres);
    }

    public @NonNull Optional<UserData> selectById(@NonNull UUID playerId) {
        Wheres<Object> wheres = Wheres
            .whereUUID(UserDataQueries.USER_ID_COLUMN, o -> playerId);

        return this.database.selectFirst(this.userTable, UserDataQueries.USER_SELECT, wheres);
    }

    public @NonNull Optional<UserData> selectByName(@NonNull String playerName) {
        Wheres<Object> wheres = Wheres
            .where(UserDataQueries.USER_NAME_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> playerName);

        return this.database.selectFirst(this.userTable, UserDataQueries.USER_SELECT, wheres);
    }

    @Override
    protected @NonNull List<UserData> selectAll() {
        SelectStatement<UserData> statement = SelectStatement.builder(UserDataQueries.USER_ROW_MAPPER).build();
        return this.database.selectAny(this.userTable, statement);
    }

    @Override
    protected void upsertData(@NonNull Collection<UserData> datas) {
        this.database.insert(this.userTable, UserDataQueries.USER_INSERT, datas);
    }

    @Override
    protected void deleteData(@NonNull Collection<UserData> datas) {
        Wheres<UserData> wheres = Wheres
            .whereUUID(UserDataQueries.USER_ID_COLUMN, UserData::getId)
            .and(UserDataQueries.USER_NAME_COLUMN, Operator.EQUALS_IGNORE_CASE, UserData::getName);

        this.database.delete(this.userTable, datas, wheres);
    }
}
