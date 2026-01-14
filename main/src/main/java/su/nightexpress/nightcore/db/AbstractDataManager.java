package su.nightexpress.nightcore.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.SQLQueries;
import su.nightexpress.nightcore.db.sql.query.type.AbstractQuery;
import su.nightexpress.nightcore.db.sql.query.impl.DeleteQuery;
import su.nightexpress.nightcore.db.sql.query.impl.InsertQuery;
import su.nightexpress.nightcore.db.sql.query.impl.SelectQuery;
import su.nightexpress.nightcore.db.sql.query.impl.UpdateQuery;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Deprecated
public abstract class AbstractDataManager<P extends NightPlugin> extends AbstractManager<P> {

    protected final DatabaseConfig    config;
    protected final AbstractConnector connector;
    protected final DataSynchronizer synchronizer;

    @Deprecated
    protected final Gson              gson;

    public AbstractDataManager(@NotNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDataManager(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
        this.synchronizer = new DataSynchronizer(this.connector);

        this.gson = this.registerAdapters(new GsonBuilder().setPrettyPrinting()).create();
    }

    @NotNull
    protected static DatabaseConfig getDataConfig(@NotNull NightPlugin plugin) {
        return DatabaseConfig.read(plugin);
    }

    @Override
    protected void onLoad() {
        if (this.config.getSyncInterval() > 0 && this.getStorageType() != DatabaseType.SQLITE) {
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

    @NotNull
    protected abstract GsonBuilder registerAdapters(@NotNull GsonBuilder builder);

    protected abstract void onInitialize();

    protected abstract void onClose();

    public abstract void onSynchronize();

    public abstract void onPurge();

    @NotNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NotNull
    public DatabaseType getStorageType() {
        return this.config.getStorageType();
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
        return this.getConnector().getConnection();
    }

    public void addTableSync(@NotNull String tableName, @NotNull Consumer<ResultSet> consumer) {
        if (this.config.getSyncInterval() > 0 && this.getStorageType() == DatabaseType.MYSQL) {
            this.synchronizer.addTable(tableName, consumer);
        }
    }

    public void createTable(@NotNull String table, @NotNull List<Column> columns) {
        SQLQueries.createTable(this.connector, this.getStorageType(), table, columns);
    }

    public void renameTable(@NotNull String table, @NotNull String toName) {
        SQLQueries.renameTable(this.connector, this.getStorageType(), toName, toName);
    }

    public void addColumn(@NotNull String table, @NotNull Column column, @NotNull String defaultValue) {
        SQLQueries.addColumn(this.connector, this.getStorageType(), table, column, defaultValue);
    }

    public void renameColumn(@NotNull String table, @NotNull Column column, @NotNull String toName) {
        SQLQueries.renameColumn(this.connector, table, column, toName);
    }

    public void renameColumn(@NotNull String table, @NotNull String column, @NotNull String toName) {
        SQLQueries.renameColumn(this.connector, table, column, toName);
    }

    public void dropColumn(@NotNull String table, @NotNull Column... columns) {
        for (Column column : columns) {
            SQLQueries.dropColumn(this.connector, table, column);
        }
    }

    public void dropColumn(@NotNull String table, @NotNull String... columns) {
        for (String column : columns) {
            SQLQueries.dropColumn(this.connector, table, column);
        }
    }

    public boolean hasColumn(@NotNull String table, @NotNull Column column) {
        return SQLQueries.hasColumn(this.connector, table, column);
    }



    public <T> void insert(@NotNull String table, @NotNull InsertQuery<T> query, @NotNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void insert(@NotNull String table, @NotNull InsertQuery<T> query, @NotNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void update(@NotNull String table, @NotNull UpdateQuery<T> query, @NotNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void update(@NotNull String table, @NotNull UpdateQuery<T> query, @NotNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void delete(@NotNull String table, @NotNull DeleteQuery<T> query, @NotNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void delete(@NotNull String table, @NotNull DeleteQuery<T> query, @NotNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void executeUpdate(@NotNull String table, @NotNull AbstractQuery<T> query, @NotNull T entity) {
        SQLQueries.executeQuery(this.connector, table, query, entity);
    }

    public <T> void executeUpdate(@NotNull String table, @NotNull AbstractQuery<T> query, @NotNull Collection<T> entities) {
        SQLQueries.executeQuery(this.connector, table, query, entities);
    }



    public boolean contains(@NotNull String table, @NotNull Consumer<SelectQuery<Boolean>> consumer) {
        return this.selectFirst(table, resultSet -> true, consumer) != null;
    }

    @Nullable
    public <T> T selectFirst(@NotNull String table, @NotNull Function<ResultSet, T> function, @NotNull Consumer<SelectQuery<T>> consumer) {
        SelectQuery<T> query = new SelectQuery<>(function);

        consumer.accept(query);

        return this.selectFirst(table, query);
    }

    @NotNull
    public <T> List<T> select(@NotNull String table, @NotNull Function<ResultSet, T> function, @NotNull Consumer<SelectQuery<T>> consumer) {
        SelectQuery<T> query = new SelectQuery<>(function);

        consumer.accept(query);

        return this.select(table, query);
    }

    @Nullable
    public <T> T selectFirst(@NotNull String table, @NotNull SelectQuery<T> query) {
        List<T> list = this.select(table, query.limit(1));
        return list.isEmpty() ? null : list.getFirst();
    }

    @NotNull
    public <T> List<T> select(@NotNull String table, @NotNull SelectQuery<T> query) {
        return SQLQueries.executeSelect(this.connector, table, query);
    }
}
