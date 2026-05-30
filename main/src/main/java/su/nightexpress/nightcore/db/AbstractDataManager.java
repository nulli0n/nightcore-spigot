package su.nightexpress.nightcore.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
    protected final DataSynchronizer  synchronizer;

    @Deprecated
    protected final Gson gson;

    public AbstractDataManager(@NonNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDataManager(@NonNull P plugin, @NonNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
        this.synchronizer = new DataSynchronizer(config.getServerId(), this.connector);

        this.gson = this.registerAdapters(new GsonBuilder().setPrettyPrinting()).create();
    }

    @NonNull
    protected static DatabaseConfig getDataConfig(@NonNull NightPlugin plugin) {
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

    @NonNull
    protected abstract GsonBuilder registerAdapters(@NonNull GsonBuilder builder);

    protected abstract void onInitialize();

    protected abstract void onClose();

    public abstract void onSynchronize();

    public abstract void onPurge();

    @NonNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NonNull
    public DatabaseType getStorageType() {
        return this.config.getStorageType();
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
        return this.getConnector().getConnection();
    }

    public void addTableSync(@NonNull String tableName, @NonNull Consumer<ResultSet> consumer) {
        if (this.config.getSyncInterval() > 0 && this.getStorageType() == DatabaseType.MYSQL) {
            this.synchronizer.addTable(tableName, consumer);
        }
    }

    public void createTable(@NonNull String table, @NonNull List<Column> columns) {
        SQLQueries.createTable(this.connector, this.getStorageType(), table, columns);
    }

    public void renameTable(@NonNull String table, @NonNull String toName) {
        SQLQueries.renameTable(this.connector, this.getStorageType(), toName, toName);
    }

    public void addColumn(@NonNull String table, @NonNull Column column, @NonNull String defaultValue) {
        SQLQueries.addColumn(this.connector, this.getStorageType(), table, column, defaultValue);
    }

    public void renameColumn(@NonNull String table, @NonNull Column column, @NonNull String toName) {
        SQLQueries.renameColumn(this.connector, table, column, toName);
    }

    public void renameColumn(@NonNull String table, @NonNull String column, @NonNull String toName) {
        SQLQueries.renameColumn(this.connector, table, column, toName);
    }

    public void dropColumn(@NonNull String table, @NonNull Column... columns) {
        for (Column column : columns) {
            SQLQueries.dropColumn(this.connector, table, column);
        }
    }

    public void dropColumn(@NonNull String table, @NonNull String... columns) {
        for (String column : columns) {
            SQLQueries.dropColumn(this.connector, table, column);
        }
    }

    public boolean hasColumn(@NonNull String table, @NonNull Column column) {
        return SQLQueries.hasColumn(this.connector, table, column);
    }


    public <T> void insert(@NonNull String table, @NonNull InsertQuery<T> query, @NonNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void insert(@NonNull String table, @NonNull InsertQuery<T> query, @NonNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void update(@NonNull String table, @NonNull UpdateQuery<T> query, @NonNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void update(@NonNull String table, @NonNull UpdateQuery<T> query, @NonNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void delete(@NonNull String table, @NonNull DeleteQuery<T> query, @NonNull T entity) {
        this.executeUpdate(table, query, entity);
    }

    public <T> void delete(@NonNull String table, @NonNull DeleteQuery<T> query, @NonNull Collection<T> entities) {
        this.executeUpdate(table, query, entities);
    }

    public <T> void executeUpdate(@NonNull String table, @NonNull AbstractQuery<T> query, @NonNull T entity) {
        SQLQueries.executeQuery(this.connector, table, query, entity);
    }

    public <T> void executeUpdate(@NonNull String table, @NonNull AbstractQuery<T> query,
                                  @NonNull Collection<T> entities) {
        SQLQueries.executeQuery(this.connector, table, query, entities);
    }


    public boolean contains(@NonNull String table, @NonNull Consumer<SelectQuery<Boolean>> consumer) {
        return this.selectFirst(table, resultSet -> true, consumer) != null;
    }

    @Nullable
    public <T> T selectFirst(@NonNull String table, @NonNull Function<ResultSet, T> function,
                             @NonNull Consumer<SelectQuery<T>> consumer) {
        SelectQuery<T> query = new SelectQuery<>(function);

        consumer.accept(query);

        return this.selectFirst(table, query);
    }

    @NonNull
    public <T> List<T> select(@NonNull String table, @NonNull Function<ResultSet, T> function,
                              @NonNull Consumer<SelectQuery<T>> consumer) {
        SelectQuery<T> query = new SelectQuery<>(function);

        consumer.accept(query);

        return this.select(table, query);
    }

    @Nullable
    public <T> T selectFirst(@NonNull String table, @NonNull SelectQuery<T> query) {
        List<T> list = this.select(table, query.limit(1));
        return list.isEmpty() ? null : list.getFirst();
    }

    @NonNull
    public <T> List<T> select(@NonNull String table, @NonNull SelectQuery<T> query) {
        return SQLQueries.executeSelect(this.connector, table, query);
    }
}
