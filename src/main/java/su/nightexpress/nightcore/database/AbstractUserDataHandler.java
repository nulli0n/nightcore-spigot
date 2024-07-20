package su.nightexpress.nightcore.database;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.column.ColumnType;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.database.sql.query.UpdateEntity;
import su.nightexpress.nightcore.database.sql.query.UpdateQuery;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.TimeUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public abstract class AbstractUserDataHandler<P extends NightDataPlugin<U>, U extends DataUser> extends AbstractDataHandler<P> {

    protected static final SQLColumn COLUMN_USER_ID           = SQLColumn.of("uuid", ColumnType.STRING);
    protected static final SQLColumn COLUMN_USER_NAME         = SQLColumn.of("name", ColumnType.STRING);
    protected static final SQLColumn COLUMN_USER_DATE_CREATED = SQLColumn.of("dateCreated", ColumnType.LONG);
    protected static final SQLColumn COLUMN_USER_LAST_ONLINE  = SQLColumn.of("last_online", ColumnType.LONG);

    protected final String tableUsers;

    protected final Set<UUID> existIDs;
    protected final Set<String> existNames;

    public AbstractUserDataHandler(@NotNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractUserDataHandler(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin, config);
        this.tableUsers = this.getTablePrefix() + "_users";
        this.existIDs = new HashSet<>();
        this.existNames = new HashSet<>();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.createUserTable();
        this.cacheNamesAndIds();
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
    }

    @Override
    public void onSave() {
        //this.saveUsers(this.plugin.getUserManager().getLoaded());
    }

    @Override
    public void onPurge() {
        if (!SQLQueries.hasTable(this.getConnector(), this.tableUsers)) return;

        LocalDateTime deadline = LocalDateTime.now().minusDays(this.getConfig().getPurgePeriod());
        long deadlineMs = TimeUtil.toEpochMillis(deadline);

        this.delete(this.tableUsers, SQLCondition.smaller(COLUMN_USER_LAST_ONLINE.toValue(deadlineMs)));
    }

    protected void createUserTable() {
        List<SQLColumn> columns = new ArrayList<>();
        columns.add(COLUMN_USER_ID);
        columns.add(COLUMN_USER_NAME);
        columns.add(COLUMN_USER_DATE_CREATED);
        columns.add(COLUMN_USER_LAST_ONLINE);
        columns.addAll(this.getExtraColumns());

        this.createTable(this.tableUsers, columns);
    }

    public boolean isNameIdCacheEnabled() {
        // TODO NOT WORKING FOR MYSQL DUE TO LOCAL CACHE, OTHER SERVERS THINK THAT PLAYER NOT EXISTS
        return CoreConfig.USER_CACHE_NAME_AND_UUID.get() && this.getDatabaseType() == DatabaseType.SQLITE;
    }

    public void cacheNamesAndIds() {
        if (!this.isNameIdCacheEnabled()) return;

        Function<ResultSet, Void> function = resultSet -> {
            try {
                this.existIDs.add(UUID.fromString(resultSet.getString(COLUMN_USER_ID.getName())));
                this.existNames.add(resultSet.getString(COLUMN_USER_NAME.getName()).toLowerCase());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        };

        this.load(this.tableUsers, function, Arrays.asList(COLUMN_USER_ID, COLUMN_USER_NAME), Collections.emptyList(), -1);
    }

    @NotNull
    protected abstract List<SQLColumn> getExtraColumns();

    @NotNull
    protected List<SQLColumn> getReadColumns() {
        return Collections.emptyList();
    }

    @NotNull
    protected abstract List<SQLValue> getSaveColumns(@NotNull U user);

    @NotNull
    protected abstract Function<ResultSet, U> getUserFunction();

    @NotNull
    public List<U> getUsers() {
        return this.load(this.tableUsers, this.getUserFunction(), Collections.emptyList(), Collections.emptyList(), -1);
    }

    @Nullable
    public U getUser(@NotNull Player player) {
        return this.getUser(player.getUniqueId());
    }

    @Nullable
    public final U getUser(@NotNull String name) {
        return this.load(this.tableUsers, this.getUserFunction(), this.getReadColumns(),
            Collections.singletonList(SQLCondition.equal(COLUMN_USER_NAME.asLowerCase().toValue(name.toLowerCase())))
        ).orElse(null);
    }

    @Nullable
    public final U getUser(@NotNull UUID uuid) {
        return this.load(this.tableUsers, this.getUserFunction(), this.getReadColumns(),
            Collections.singletonList(SQLCondition.equal(COLUMN_USER_ID.toValue(uuid)))
        ).orElse(null);
    }

    public boolean isUserExists(@NotNull String name) {
        if (this.isNameIdCacheEnabled()) {
            return this.existNames.contains(name.toLowerCase());
        }
        return this.contains(this.tableUsers, Collections.singletonList(COLUMN_USER_NAME), SQLCondition.equal(COLUMN_USER_NAME.asLowerCase().toValue(name.toLowerCase())));
    }

    public boolean isUserExists(@NotNull UUID uuid) {
        if (this.isNameIdCacheEnabled()) {
            return this.existIDs.contains(uuid);
        }
        return this.contains(this.tableUsers, Collections.singletonList(COLUMN_USER_ID), SQLCondition.equal(COLUMN_USER_ID.toValue(uuid)));
    }

    public void saveUser(@NotNull U user) {
        this.executeUpdate(UpdateQuery.create(this.tableUsers, this.createUpdateEntity(user)));
    }

    public void saveUsers(@NotNull Collection<U> users) {
        this.executeUpdate(UpdateQuery.create(this.tableUsers, users.stream().map(this::createUpdateEntity).toList()));
    }

    @NotNull
    public UpdateEntity createUpdateEntity(@NotNull U user) {
        List<SQLValue> values = new ArrayList<>();
        values.add(COLUMN_USER_NAME.toValue(user.getName()));
        values.add(COLUMN_USER_DATE_CREATED.toValue(user.getDateCreated()));
        values.add(COLUMN_USER_LAST_ONLINE.toValue(user.getLastOnline()));
        values.addAll(this.getSaveColumns(user));

        List<SQLCondition> conditions = Lists.newList(
            SQLCondition.equal(COLUMN_USER_ID.toValue(user.getId()))
        );

        return this.createUpdateEntity(values, conditions);
    }

    public void addUser(@NotNull U user) {
        List<SQLValue> values = new ArrayList<>();
        values.add(COLUMN_USER_ID.toValue(user.getId()));
        values.add(COLUMN_USER_NAME.toValue(user.getName()));
        values.add(COLUMN_USER_DATE_CREATED.toValue(user.getDateCreated()));
        values.add(COLUMN_USER_LAST_ONLINE.toValue(user.getLastOnline()));
        values.addAll(this.getSaveColumns(user));

        this.insert(this.tableUsers, values);

        this.existIDs.add(user.getId());
        this.existNames.add(user.getName());
    }

    public void deleteUser(@NotNull UUID uuid) {
        this.delete(this.tableUsers, SQLCondition.equal(COLUMN_USER_ID.toValue(uuid)));

        this.existIDs.clear();
        this.existNames.clear();
        this.cacheNamesAndIds();
    }

    public void deleteUser(@NotNull DataUser user) {
        this.delete(this.tableUsers, SQLCondition.equal(COLUMN_USER_ID.toValue(user.getId())));

        this.existIDs.remove(user.getId());
        this.existNames.remove(user.getName());
    }
}
