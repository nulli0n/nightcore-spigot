package su.nightexpress.nightcore.db;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.statement.SQLStatements;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.DeleteStatement;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.statement.template.UpdateStatement;
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

    public AbstractDatabaseManager(@NonNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDatabaseManager(@NonNull P plugin, @NonNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
        this.synchronizer = new DataSynchronizer(this.connector);
        this.databaseType = config.getStorageType();
    }

    @NonNull
    protected static DatabaseConfig getDataConfig(@NonNull NightPlugin plugin) {
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

    @NonNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NonNull
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    @NonNull
    public String getTablePrefix() {
        return this.config.getTablePrefix();
    }

    @NonNull
    public AbstractConnector getConnector() {
        return this.connector;
    }

    @NonNull
    protected final Connection getConnection() throws SQLException {
        return this.connector.getConnection();
    }

    public void addTableSync(@NonNull Table table, @NonNull Consumer<ResultSet> consumer) {
        this.addTableSync(table.getName(), consumer);
    }

    public void addTableSync(@NonNull String tableName, @NonNull Consumer<ResultSet> consumer) {
        if (this.config.getSyncInterval() > 0 && this.databaseType == DatabaseType.MYSQL) {
            this.synchronizer.addTable(tableName, consumer);
        }
    }

    public void createTable(@NonNull Table table) {
        SQLStatements.executeUpdate(this.connector, table.toSqlCreate(this.databaseType));

        table.getColumns().forEach(column -> this.addColumn(table, column)); // Add missing columns
    }

    public void addColumn(@NonNull Table table, @NonNull Column<?> column) {
        if (SQLStatements.hasColumn(this.connector, table, column)) return;

        SQLStatements.executeUpdate(this.connector, table.toSqlAddColumn(this.databaseType, column));
    }

    public void renameColumn(@NonNull Table table, @NonNull Column<?> column, @NonNull String targetName) {
        this.renameColumn(table, column.getQuotedName(), targetName);
    }

    public void renameColumn(@NonNull Table table, @NonNull String sourceName, @NonNull String targetName) {
        if (!SQLStatements.hasColumn(this.connector, table.getName(), sourceName)) return;

        SQLStatements.executeUpdate(this.connector, table.toSqlRenameColumn(this.databaseType, sourceName, targetName));
    }

    public void dropColumn(@NonNull Table table, @NonNull String... columnNames) {
        for (String columnName : columnNames) {
            if (SQLStatements.hasColumn(this.connector, table.getName(), columnName)) {
                SQLStatements.executeUpdate(this.connector, table.toSqlDropColumn(this.databaseType, columnName));
            }
        }
    }

    public boolean hasColumn(@NonNull Table table, @NonNull Column<?> column) {
        return SQLStatements.hasColumn(this.connector, table, column);
    }



    public <T> void insert(@NonNull Table table, @NonNull InsertStatement<T> statement, @NonNull T entity) {
        this.executeBatch(table, statement, entity, null);
    }

    public <T> void insert(@NonNull Table table, @NonNull InsertStatement<T> statement, @NonNull Collection<T> entities) {
        this.executeBatch(table, statement, entities, null);
    }

    public <T> void update(@NonNull Table table, @NonNull UpdateStatement<T> statement, @NonNull T entity) {
        this.update(table, statement, entity, null);
    }

    public <T> void update(@NonNull Table table, @NonNull UpdateStatement<T> statement, @NonNull Collection<T> entities) {
        this.update(table, statement, entities, null);
    }

    public <T> void update(@NonNull Table table, @NonNull UpdateStatement<T> statement, @NonNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, statement, entity, wheres);
    }

    public <T> void update(@NonNull Table table, @NonNull UpdateStatement<T> statement, @NonNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, statement, entities, wheres);
    }

    public void delete(@NonNull Table table) {
        this.delete(table, (Wheres<Object>) null);
    }

    public void delete(@NonNull Table table, @Nullable Wheres<Object> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), new Object(), wheres);
    }

    public <T> void delete(@NonNull Table table, @NonNull T entity) {
        this.delete(table, entity, null);
    }

    public <T> void delete(@NonNull Table table, @NonNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), entity, wheres);
    }

    public <T> void delete(@NonNull Table table, @NonNull Collection<T> entities) {
        this.delete(table, entities, (Wheres<T>) null);
    }

    public <T> void delete(@NonNull Table table, @NonNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, new DeleteStatement<>(), entities, wheres);
    }

    public <T> void executeBatch(@NonNull Table table, @NonNull BatchStatement<T> query, @NonNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table.getName(), query, entity, wheres);
    }

    public <T> void executeBatch(@NonNull String table, @NonNull BatchStatement<T> query, @NonNull T entity, @Nullable Wheres<T> wheres) {
        this.executeBatch(table, query, Collections.singletonList(entity), wheres);
    }

    public <T> void executeBatch(@NonNull Table table, @NonNull BatchStatement<T> query, @NonNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        this.executeBatch(table.getName(), query, entities, wheres);
    }

    public <T> void executeBatch(@NonNull String table, @NonNull BatchStatement<T> query, @NonNull Collection<T> entities, @Nullable Wheres<T> wheres) {
        SQLStatements.executeBatch(this.connector, table, query, entities, wheres);
    }

    public void executeStatement(@NonNull Table table, @NonNull String statement, @Nullable Wheres<Object> wheres) {
        this.executeStatement(table.getName(), statement, wheres);
    }

    public void executeStatement(@NonNull String table, @NonNull String statement, @Nullable Wheres<Object> wheres) {
        StringBuilder sql = new StringBuilder(statement);
        if (wheres != null && !wheres.isEmpty()) {
            sql.append(" WHERE ").append(wheres.toSql());
        }

        SQLStatements.executeUpdate(this.connector, sql.toString());
    }

    public boolean contains(@NonNull Table table, @NonNull Wheres<Object> wheres) {
        return this.selectFirst(table, ROW_LOOKUP, wheres).isPresent();
    }

    @NonNull
    public <R> Optional<R> selectAnyFirst(@NonNull Table table, @NonNull SelectStatement<R> query) {
        return SQLStatements.selectAnyFirst(this.connector, table.getName(), query);
    }

    @NonNull
    public <R> Optional<R> selectFirst(@NonNull Table table, @NonNull SelectStatement<R> query, @NonNull Wheres<Object> wheres) {
        return SQLStatements.selectFirst(this.connector, table.getName(), query, wheres);
    }

    @NonNull
    public <R> List<R> selectAny(@NonNull Table table, @NonNull SelectStatement<R> query) {
        return SQLStatements.selectAny(this.connector, table.getName(), query);
    }

    @NonNull
    public <R> List<R> selectWhere(@NonNull Table table, @NonNull SelectStatement<R> query, @NonNull Wheres<Object> wheres) {
        return SQLStatements.select(this.connector, table.getName(), query, wheres, null);
    }
}
