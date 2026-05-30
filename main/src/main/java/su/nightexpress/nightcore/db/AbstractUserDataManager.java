package su.nightexpress.nightcore.db;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.column.ColumnType;
import su.nightexpress.nightcore.db.sql.query.UserQueries;
import su.nightexpress.nightcore.db.sql.query.impl.InsertQuery;
import su.nightexpress.nightcore.db.sql.query.impl.SelectQuery;
import su.nightexpress.nightcore.db.sql.query.impl.UpdateQuery;
import su.nightexpress.nightcore.db.sql.query.type.ValuedQuery;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;
import su.nightexpress.nightcore.util.TimeUtil;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Deprecated
public abstract class AbstractUserDataManager<P extends NightPlugin, U extends AbstractUser> extends AbstractDataManager<P> {

    public static final Column COLUMN_USER_ID           = Column.of("uuid", ColumnType.STRING);
    public static final Column COLUMN_USER_NAME         = Column.of("name", ColumnType.STRING);
    public static final Column COLUMN_USER_DATE_CREATED = Column.of("dateCreated", ColumnType.LONG);
    public static final Column COLUMN_USER_LAST_ONLINE  = Column.of("last_online", ColumnType.LONG);

    protected final String tableUsers;

    protected final Function<ResultSet, U> userFunction;

    public AbstractUserDataManager(@NonNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractUserDataManager(@NonNull P plugin, @NonNull DatabaseConfig config) {
        super(plugin, config);
        this.tableUsers = this.getTablePrefix() + "_users";

        this.userFunction = this.createUserFunction();
    }

    @Override
    protected void onInitialize() {
        this.createUserTable();
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void onPurge() {
        LocalDateTime deadline = LocalDateTime.now().minusDays(this.getConfig().getPurgePeriod());
        long deadlineMs = TimeUtil.toEpochMillis(deadline);

        this.delete(this.tableUsers, UserQueries.deleteByLastOnline(), deadlineMs);
    }

    @NonNull
    protected abstract Function<ResultSet, U> createUserFunction();

    protected void createUserTable() {
        List<Column> columns = new ArrayList<>();
        columns.add(COLUMN_USER_ID);
        columns.add(COLUMN_USER_NAME);
        columns.add(COLUMN_USER_DATE_CREATED);
        columns.add(COLUMN_USER_LAST_ONLINE);
        this.addTableColumns(columns);

        this.createTable(this.tableUsers, columns);
    }

    protected abstract void addUpsertQueryData(@NonNull ValuedQuery<?, U> query);

    protected abstract void addSelectQueryData(@NonNull SelectQuery<U> query);

    protected abstract void addTableColumns(@NonNull List<Column> columns);

    @NonNull
    public List<U> getUsers() {
        return this.select(this.tableUsers, this.userFunction, SelectQuery::all);
    }

    @Nullable
    public U getUser(@NonNull Player player) {
        return this.getUser(player.getUniqueId());
    }

    @Nullable
    public final U getUser(@NonNull String name) {
        return this.selectUser(query -> query.whereIgnoreCase(COLUMN_USER_NAME, WhereOperator.EQUAL, name
            .toLowerCase()));
    }

    @Nullable
    public final U getUser(@NonNull UUID uuid) {
        return this.selectUser(query -> query.where(COLUMN_USER_ID, WhereOperator.EQUAL, uuid.toString()));
    }

    private U selectUser(@NonNull Consumer<SelectQuery<U>> consumer) {
        SelectQuery<U> query = new SelectQuery<>(this.userFunction);
        query.column(COLUMN_USER_ID);
        query.column(COLUMN_USER_NAME);
        query.column(COLUMN_USER_LAST_ONLINE);
        query.column(COLUMN_USER_DATE_CREATED);
        this.addSelectQueryData(query);

        consumer.accept(query);

        return this.selectFirst(this.tableUsers, query);
    }

    public boolean isUserExists(@NonNull String name) {
        return this.contains(this.tableUsers, query -> query
            .column(COLUMN_USER_NAME)
            .whereIgnoreCase(COLUMN_USER_NAME, WhereOperator.EQUAL, name.toLowerCase())
        );
    }

    public boolean isUserExists(@NonNull UUID uuid) {
        return this.contains(this.tableUsers, query -> query
            .column(COLUMN_USER_ID)
            .where(COLUMN_USER_ID, WhereOperator.EQUAL, uuid.toString())
        );
    }

    @Deprecated
    public void saveUser(@NonNull U user) {
        this.saveUsersFully(Collections.singletonList(user));
    }

    @Deprecated
    public void saveUsers(@NonNull Collection<U> users) {
        this.saveUsersFully(users);
    }

    public void saveUserCommons(@NonNull U user) {
        this.saveUsersCommons(Collections.singletonList(user));
    }

    public void saveUsersCommons(@NonNull Collection<U> users) {
        this.update(this.tableUsers, UserQueries.updateCommons(), users);
    }

    public void saveUserFully(@NonNull U user) {
        this.saveUsersFully(Collections.singletonList(user));
    }

    public void saveUsersFully(@NonNull Collection<U> users) {
        UpdateQuery<U> query = UserQueries.updateCommons();
        this.addUpsertQueryData(query);

        this.update(this.tableUsers, query, users);
    }

    public void insertUser(@NonNull U user) {
        InsertQuery<U> query = UserQueries.insert();
        this.addUpsertQueryData(query);

        this.insert(this.tableUsers, query, user);
    }

    public void deleteUser(@NonNull U user) {
        this.deleteUser(user.getId());
    }

    public void deleteUser(@NonNull UUID userId) {
        this.delete(this.tableUsers, UserQueries.deleteByUUID(), userId);
    }
}
