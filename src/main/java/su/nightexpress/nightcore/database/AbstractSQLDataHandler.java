package su.nightexpress.nightcore.database;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.executor.*;
import su.nightexpress.nightcore.database.sql.query.SQLUpdateQuery;
import su.nightexpress.nightcore.database.sql.query.UpdateEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static su.nightexpress.nightcore.database.AbstractDataHandler.getDataConfig;

public abstract class AbstractSQLDataHandler<P extends NightCorePlugin> extends AbstractDelegatedDataHandler<P> {
    protected final DatabaseConfig config;
    protected final AbstractConnector connector;

    public AbstractSQLDataHandler(@NotNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractSQLDataHandler(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
    }

    @Override
    public void onLoad() {
        if (this.config.getSaveInterval() > 0) {
            this.addTask(this.plugin.createAsyncTask(this::onSave).setSecondsInterval(this.config.getSaveInterval() * 60));
        }

        if (this.config.getSyncInterval() > 0 && this.getDatabaseType() != DatabaseType.SQLITE) {
            this.addTask(this.plugin.createAsyncTask(this::onSynchronize).setSecondsInterval(this.config.getSyncInterval()));
            this.plugin.info("Enabled data synchronization with " + config.getSyncInterval() + " seconds interval.");
        }

        if (this.config.isPurgeEnabled() && this.config.getPurgePeriod() > 0) {
            this.onPurge();
        }
    }

    @Override
    public void onShutdown() {
        this.onSave();
        this.getConnector().close();
    }

    @NotNull
    public AbstractConnector getConnector() {
        return this.connector;
    }

    @NotNull
    protected final Connection getConnection() throws SQLException {
        return this.getConnector().getConnection();
    }

    public void createTable(@NotNull String table, @NotNull List<SQLColumn> columns) {
        CreateTableExecutor.builder(table, this.getDatabaseType()).columns(columns).execute(this.getConnector());
    }

    public void renameTable(@NotNull String from, @NotNull String to) {
        RenameTableExecutor.builder(from, this.getDatabaseType()).renameTo(to).execute(this.getConnector());
    }

    public void addColumn(@NotNull String table, @NotNull SQLValue... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).addColumn(columns).execute(this.getConnector());
    }

    public void renameColumn(@NotNull String table, @NotNull SQLValue... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).renameColumn(columns).execute(this.getConnector());
    }

    public void dropColumn(@NotNull String table, @NotNull SQLColumn... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).dropColumn(columns).execute(this.getConnector());
    }

    public boolean hasColumn(@NotNull String table, @NotNull SQLColumn column) {
        return SQLQueries.hasColumn(this.getConnector(), table, column);
    }

    public void insert(@NotNull String table, @NotNull List<SQLValue> values) {
        InsertQueryExecutor.builder(table).values(values).execute(this.getConnector());
    }


    @Deprecated
    public void update(@NotNull String table, @NotNull List<SQLValue> values, @NotNull SQLCondition... conditions) {
        UpdateQueryExecutor.builder(table).values(values).where(conditions).execute(this.getConnector());
    }

    public void executeUpdate(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        this.executeUpdate(SQLUpdateQuery.create(table, values, conditions));
    }

    public void executeUpdate(@NotNull SQLUpdateQuery query) {
        SQLQueries.executeUpdate(this.connector, query);
    }

    public void delete(@NotNull String table, @NotNull SQLCondition... conditions) {
        DeleteQueryExecutor.builder(table).where(conditions).execute(this.getConnector());
    }

    public boolean contains(@NotNull String table, @NotNull SQLCondition... conditions) {
        return this.load(table, (resultSet -> true), Collections.emptyList(), Arrays.asList(conditions)).isPresent();
    }

    public boolean contains(@NotNull String table, @NotNull List<SQLColumn> columns, @NotNull SQLCondition... conditions) {
        return this.load(table, (resultSet -> true), columns, Arrays.asList(conditions)).isPresent();
    }

    @NotNull
    public <T> Optional<T> load(@NotNull String table, @NotNull Function<ResultSet, T> function,
                                @NotNull List<SQLColumn> columns,
                                @NotNull List<SQLCondition> conditions) {
        List<T> list = this.load(table, function, columns, conditions, 1);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @NotNull
    public <T> List<T> load(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction) {
        return this.load(table, dataFunction, -1);
    }

    @NotNull
    public <T> List<T> load(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction, int amount) {
        return this.load(table, dataFunction, Collections.emptyList(), amount);
    }

    @NotNull
    public <T> List<T> load(@NotNull String table,
                            @NotNull Function<ResultSet, T> dataFunction,
                            @NotNull List<SQLColumn> columns,
                            int amount) {
        return this.load(table, dataFunction, columns, Collections.emptyList(), amount);
    }

    @NotNull
    public <T> List<T> load(@NotNull String table,
                            @NotNull Function<ResultSet, T> dataFunction,
                            @NotNull List<SQLColumn> columns,
                            @NotNull List<SQLCondition> conditions,
                            int amount) {
        return SelectQueryExecutor.builder(table, dataFunction).columns(columns).where(conditions).execute(this.getConnector());
    }
}