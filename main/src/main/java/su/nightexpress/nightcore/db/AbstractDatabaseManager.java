package su.nightexpress.nightcore.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.statement.SQLStatements;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.*;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;
import su.nightexpress.nightcore.db.table.Table;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractDatabaseManager<P extends NightPlugin> extends AbstractManager<P> {

    private static final SelectStatement<Boolean> ROW_LOOKUP = SelectStatement.builder(resultSet -> true).column("1").build(); // SELECT 1 FROM ...

    protected final DatabaseConfig    config;
    protected final AbstractConnector connector;
    protected final DataSynchronizer  synchronizer;
    protected final DatabaseType      databaseType;

    public AbstractDatabaseManager(@NotNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDatabaseManager(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
        this.synchronizer = new DataSynchronizer(this.connector);
        this.databaseType = config.getStorageType();
    }

    @NotNull
    protected static DatabaseConfig getDataConfig(@NotNull NightPlugin plugin) {
        return DatabaseConfig.read(plugin);
    }

    @Override
    protected void onLoad() {
        if (this.config.getSyncInterval() > 0 && this.databaseType != DatabaseType.SQLITE) {
            this.addAsyncTask(this::onSynchronize, this.config.getSyncInterval());
            this.plugin.info("Enabled data synchronization with " + config.getSyncInterval() + " seconds interval.");
        }

        this.onInitialize();

        if (this.config.isPurgeEnabled() && this.config.getPurgePeriod() > 0) {
            this.onPurge();
        }
    }

    @Override
    protected void onShutdown() {
        this.onClose();
        this.connector.close();
    }

    protected abstract void onInitialize();

    protected abstract void onClose();

    public abstract void onSynchronize();

    public abstract void onPurge();

    @NotNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NotNull
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    @NotNull
    public String getTablePrefix() {
        return this.config.getTablePrefix();
    }

    @NotNull
    public AbstractConnector getConnector() {
        return this.connector;
    }

    @NotNull
    protected final Connection getConnection() throws SQLException {
        return this.connector.getConnection();
    }

    public void addTableSync(@NotNull Table table, @NotNull Consumer<ResultSet> consumer) {
        this.addTableSync(table.getName(), consumer);
    }

    public void addTableSync(@NotNull String tableName, @NotNull Consumer<ResultSet> consumer) {
        if (this.config.getSyncInterval() > 0 && this.databaseType == DatabaseType.MYSQL) {
            this.synchronizer.addTable(tableName, consumer);
        }
    }

    public void createTable(@NotNull Table table) {
        SQLStatements.executeUpdate(this.connector, table.toSqlCreate(this.databaseType));

        table.getColumns().forEach(column -> this.addColumn(table, column)); // Add missing columns
    }

    public void addColumn(@NotNull Table table, @NotNull Column<?> column) {
        if (SQLStatements.hasColumn(this.connector, table, column)) return;

        SQLStatements.executeUpdate(this.connector, table.toSqlAddColumn(this.databaseType, column));
    }

    public void renameColumn(@NotNull Table table, @NotNull Column<?> column, @NotNull String targetName) {
        this.renameColumn(table, column.getQuotedName(), targetName);
    }

    public void renameColumn(@NotNull Table table, @NotNull String sourceName, @NotNull String targetName) {
        if (!SQLStatements.hasColumn(this.connector, table.getName(), sourceName)) return;

        SQLStatements.executeUpdate(this.connector, table.toSqlRenameColumn(this.databaseType, sourceName, targetName));
    }

    public void dropColumn(@NotNull Table table, @NotNull String... columnNames) {
        for (String columnName : columnNames) {
            if (SQLStatements.hasColumn(this.connector, table.getName(), columnName)) {
                SQLStatements.executeUpdate(this.connector, table.toSqlDropColumn(this.databaseType, columnName));
            }
        }
    }

    public boolean hasColumn(@NotNull Table table, @NotNull Column<?> column) {
        return SQLStatements.hasColumn(this.connector, table, column);
    }



    public <T> void insert(@NotNull Table table, @NotNull InsertStatement<T> statement, @NotNull T entity) {
        this.executeBatch(table, statement, entity, null);
    }

    public <T> void insert(@NotNull Table table, @NotNull InsertStatement<T> statement, @NotNull Collection<T> entities) {
        this.executeBatch(table, statement, entities, null);
    }

    public <T> void update(@NotNull Table table, @NotNull UpdateStatement<T> statement, @NotNull T entity) {
        this.update(table, statement, entity, null);
    }

    public <T> void update(@NotNull Table table, @NotNull UpdateStatement<T> statement, @NotNull Collection<T> entities) {
        this.update(table, statement, entities, null);
    }

    public <T> void update(@NotNull Table table, @NotNull UpdateStatement<T> statement, @NotNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, statement, entity, wheres);
    }

    public <T> void update(@NotNull Table table, @NotNull UpdateStatement<T> statement, @NotNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, statement, entities, wheres);
    }

    public void delete(@NotNull Table table) {
        this.delete(table, (Wheres<Object>) null);
    }

    public void delete(@NotNull Table table, @Nullable Wheres<Object> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), new Object(), wheres);
    }

    public <T> void delete(@NotNull Table table, @NotNull T entity) {
        this.delete(table, entity, null);
    }

    public <T> void delete(@NotNull Table table, @NotNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), entity, wheres);
    }

    public <T> void delete(@NotNull Table table, @NotNull Collection<T> entities) {
        this.delete(table, entities, (Wheres<T>) null);
    }

    public <T> void delete(@NotNull Table table, @NotNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), entities, wheres);
    }

    public <T> void executeBatch(@NotNull Table table, @NotNull BatchStatement<T> query, @NotNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table.getName(), query, entity, wheres);
    }

    public <T> void executeBatch(@NotNull String table, @NotNull BatchStatement<T> query, @NotNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, query, Collections.singletonList(entity), wheres);
    }

    public <T> void executeBatch(@NotNull Table table, @NotNull BatchStatement<T> query, @NotNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table.getName(), query, entities, wheres);
    }

    public <T> void executeBatch(@NotNull String table, @NotNull BatchStatement<T> query, @NotNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        SQLStatements.executeBatch(this.connector, table, query, entities, wheres);
    }

    public void executeStatement(@NotNull Table table, @NotNull String statement, @Nullable Wheres<Object> wheres) {
        this.executeStatement(table.getName(), statement, wheres);
    }

    public void executeStatement(@NotNull String table, @NotNull String statement, @Nullable Wheres<Object> wheres) {
        StringBuilder sql = new StringBuilder(statement);
        if (wheres != null && !wheres.isEmpty()) {
            sql.append(" WHERE ").append(wheres.toSql());
        }

        SQLStatements.executeUpdate(this.connector, sql.toString());
    }

    public boolean contains(@NotNull Table table, @NotNull Wheres<Object> wheres) {
        return this.selectFirst(table, ROW_LOOKUP, wheres).isPresent();
    }

    @NotNull
    public <R> Optional<R> selectAnyFirst(@NotNull Table table, @NotNull SelectStatement<R> query) {
        return SQLStatements.selectAnyFirst(this.connector, table.getName(), query);
    }

    @NotNull
    public <R> Optional<R> selectFirst(@NotNull Table table, @NotNull SelectStatement<R> query, @NotNull Wheres<Object> wheres) {
        return SQLStatements.selectFirst(this.connector, table.getName(), query, wheres);
    }

    @NotNull
    public <R> List<R> selectAny(@NotNull Table table, @NotNull SelectStatement<R> query) {
        return SQLStatements.selectAny(this.connector, table.getName(), query);
    }

    @NotNull
    public <R> List<R> selectWhere(@NotNull Table table, @NotNull SelectStatement<R> query, @NotNull Wheres<Object> wheres) {
        return SQLStatements.select(this.connector, table.getName(), query, wheres, null);
    }
}
