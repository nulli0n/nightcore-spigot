package su.nightexpress.nightcore.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.serialize.ItemStackSerializer;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.executor.*;
import su.nightexpress.nightcore.database.sql.query.UpdateEntity;
import su.nightexpress.nightcore.database.sql.query.UpdateQuery;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Deprecated
public abstract class AbstractDataHandler<P extends NightCorePlugin> extends AbstractManager<P> {

    protected final DatabaseConfig    config;
    protected final AbstractConnector connector;
    protected final Gson              gson;

    public AbstractDataHandler(@NonNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDataHandler(@NonNull P plugin, @NonNull DatabaseConfig config) {
        super(plugin);
        this.config = config;
        this.connector = AbstractConnector.create(plugin, config);
        this.gson = this.registerAdapters(new GsonBuilder().setPrettyPrinting()).create();
    }

    @NonNull
    protected static DatabaseConfig getDataConfig(@NonNull NightCorePlugin plugin) {
        DatabaseConfig dataConfig = plugin.getDetails().getDatabaseConfig();
        if (dataConfig == null) {
            plugin.warn("The plugin didn't have database configuration. Fixing it now...");
            dataConfig = DatabaseConfig.read(plugin);
        }
        return dataConfig;
    }

    @Override
    protected void onLoad() {
        if (this.config.getSaveInterval() > 0) {
            this.addTask(this.plugin.createAsyncTask(this::onSave).setSecondsInterval(this.config
                .getSaveInterval() * 60));
        }

        if (this.config.getSyncInterval() > 0 && this.getDatabaseType() != DatabaseType.SQLITE) {
            this.addTask(this.plugin.createAsyncTask(this::onSynchronize).setSecondsInterval(this.config
                .getSyncInterval()));
            this.plugin.info("Enabled data synchronization with " + config.getSyncInterval() + " seconds interval.");
        }

        if (this.config.isPurgeEnabled() && this.config.getPurgePeriod() > 0) {
            this.onPurge();
        }
    }

    @Override
    protected void onShutdown() {
        this.onSave();
        this.getConnector().close();
    }

    public abstract void onSynchronize();

    public abstract void onSave();

    public abstract void onPurge();

    @NonNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NonNull
    public DatabaseType getDatabaseType() {
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
    protected GsonBuilder registerAdapters(@NonNull GsonBuilder builder) {
        return builder.registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
    }

    @NonNull
    protected final Connection getConnection() throws SQLException {
        return this.getConnector().getConnection();
    }

    public void createTable(@NonNull String table, @NonNull List<SQLColumn> columns) {
        CreateTableExecutor.builder(table, this.getDatabaseType()).columns(columns).execute(this.getConnector());
    }

    public void renameTable(@NonNull String from, @NonNull String to) {
        RenameTableExecutor.builder(from, this.getDatabaseType()).renameTo(to).execute(this.getConnector());
    }

    public void addColumn(@NonNull String table, @NonNull SQLValue... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).addColumn(columns).execute(this.getConnector());
    }

    public void renameColumn(@NonNull String table, @NonNull SQLValue... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).renameColumn(columns).execute(this.getConnector());
    }

    public void dropColumn(@NonNull String table, @NonNull SQLColumn... columns) {
        AlterTableExecutor.builder(table, this.getDatabaseType()).dropColumn(columns).execute(this.getConnector());
    }

    public boolean hasColumn(@NonNull String table, @NonNull SQLColumn column) {
        return SQLQueries.hasColumn(this.getConnector(), table, column);
    }

    public void insert(@NonNull String table, @NonNull List<SQLValue> values) {
        InsertQueryExecutor.builder(table).values(values).execute(this.getConnector());
    }


    @Deprecated
    public void update(@NonNull String table, @NonNull List<SQLValue> values, @NonNull SQLCondition... conditions) {
        UpdateQueryExecutor.builder(table).values(values).where(conditions).execute(this.getConnector());
    }

    @NonNull
    public UpdateEntity createUpdateEntity(@NonNull List<SQLValue> values, @NonNull List<SQLCondition> conditions) {
        return UpdateEntity.create(values, conditions);
    }

    public void executeUpdate(@NonNull String table, @NonNull List<SQLValue> values,
                              @NonNull List<SQLCondition> conditions) {
        this.executeUpdate(UpdateQuery.create(table, values, conditions));
    }

    public void executeUpdate(@NonNull UpdateQuery query) {
        SQLQueries.executeUpdate(this.connector, query);
    }


    public void delete(@NonNull String table, @NonNull SQLCondition... conditions) {
        DeleteQueryExecutor.builder(table).where(conditions).execute(this.getConnector());
    }

    public boolean contains(@NonNull String table, @NonNull SQLCondition... conditions) {
        return this.load(table, (resultSet -> true), Collections.emptyList(), Arrays.asList(conditions)).isPresent();
    }

    public boolean contains(@NonNull String table, @NonNull List<SQLColumn> columns,
                            @NonNull SQLCondition... conditions) {
        return this.load(table, (resultSet -> true), columns, Arrays.asList(conditions)).isPresent();
    }

    @NonNull
    public <T> Optional<T> load(@NonNull String table, @NonNull Function<ResultSet, T> function,
                                @NonNull List<SQLColumn> columns,
                                @NonNull List<SQLCondition> conditions) {
        List<T> list = this.load(table, function, columns, conditions, 1);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @NonNull
    public <T> List<T> load(@NonNull String table, @NonNull Function<ResultSet, T> dataFunction) {
        return this.load(table, dataFunction, -1);
    }

    @NonNull
    public <T> List<T> load(@NonNull String table, @NonNull Function<ResultSet, T> dataFunction, int amount) {
        return this.load(table, dataFunction, Collections.emptyList(), amount);
    }

    @NonNull
    public <T> List<T> load(@NonNull String table,
                            @NonNull Function<ResultSet, T> dataFunction,
                            @NonNull List<SQLColumn> columns,
                            int amount) {
        return this.load(table, dataFunction, columns, Collections.emptyList(), amount);
    }

    @NonNull
    public <T> List<T> load(@NonNull String table,
                            @NonNull Function<ResultSet, T> dataFunction,
                            @NonNull List<SQLColumn> columns,
                            @NonNull List<SQLCondition> conditions,
                            int amount) {
        return SelectQueryExecutor.builder(table, dataFunction).columns(columns).where(conditions).execute(this
            .getConnector());
    }
}
