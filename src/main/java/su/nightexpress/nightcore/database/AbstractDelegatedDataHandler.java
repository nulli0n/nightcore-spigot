package su.nightexpress.nightcore.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.query.IUpdateQuery;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractDelegatedDataHandler<P extends NightCorePlugin> extends AbstractManager<P> {
    protected final DatabaseConfig config;

    protected AbstractDelegatedDataHandler(@NotNull P plugin) {
        this(plugin, AbstractDataHandler.getDataConfig(plugin));
    }

    protected AbstractDelegatedDataHandler(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
    }

    public abstract void onSynchronize();

    public abstract void onSave();

    public abstract void onPurge();

    public abstract void onLoad();

    public abstract void onShutdown();

    @NotNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NotNull
    public DatabaseType getDatabaseType() {
        return this.getConfig().getStorageType();
    }

    @Nullable
    public abstract AbstractConnector getConnector();

    @Nullable
    protected abstract Connection getConnection() throws SQLException;

    public abstract void createTable(@NotNull String table, @NotNull List<SQLColumn> columns);

    public abstract void renameTable(@NotNull String from, @NotNull String to);

    public abstract void addColumn(@NotNull String table, @NotNull SQLValue... columns);

    public abstract void renameColumn(@NotNull String table, @NotNull SQLValue... columns);

    public abstract void dropColumn(@NotNull String table, @NotNull SQLColumn... columns);

    public abstract boolean hasColumn(@NotNull String table, @NotNull SQLColumn column);

    public abstract void insert(@NotNull String table, @NotNull List<SQLValue> values);

    public abstract void update(@NotNull String table, @NotNull List<SQLValue> values, @NotNull SQLCondition... conditions);

    @NotNull
    public abstract IUpdateQuery updateQuery(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions);

    public abstract void executeUpdate(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions);

    public abstract void executeUpdate(@NotNull IUpdateQuery query);

    public abstract void executeUpdates(@NotNull List<IUpdateQuery> queries);

    public abstract void delete(@NotNull String table, @NotNull SQLCondition... conditions);

    public abstract boolean contains(@NotNull String table, @NotNull SQLCondition... conditions);

    public abstract boolean contains(@NotNull String table, @NotNull List<SQLColumn> columns, @NotNull SQLCondition... conditions);

    @NotNull
    public abstract <T> Optional<T> load(@NotNull String table, @NotNull Function<ResultSet, T> function,
                                         @NotNull List<SQLColumn> columns,
                                         @NotNull List<SQLCondition> conditions);

    @NotNull
    public abstract <T> List<T> load(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction,
                                     @NotNull List<SQLColumn> columns,
                                     @NotNull List<SQLCondition> conditions,
                                     int amount);
}
