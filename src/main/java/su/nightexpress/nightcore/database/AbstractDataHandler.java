package su.nightexpress.nightcore.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.serialize.ItemStackSerializer;
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

public abstract class AbstractDataHandler<P extends NightCorePlugin> extends AbstractManager<P> {
    protected final AbstractDelegatedDataHandler<P> delegate;
    protected final DatabaseConfig config;
    protected final P plugin;
    protected final Gson gson; // Used by other plugins

    public AbstractDataHandler(@NotNull P plugin) {
        this(plugin, getDataConfig(plugin));
    }

    public AbstractDataHandler(@NotNull P plugin, @NotNull DatabaseConfig config) {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        this.gson = this.registerAdapters(new GsonBuilder().setPrettyPrinting()).create();
        AbstractDataHandler<P> root = this;
        this.delegate = switch (config.getStorageType()) {
            case MONGODB -> new AbstractMongoDBDataHandler<P>(plugin, config) {
                @Override
                public void reload() {
                    root.reload();
                }

                @Override
                public void onSynchronize() {
                    root.onSynchronize();
                }

                @Override
                public void onSave() {
                    root.onSave();
                }

                @Override
                public void onPurge() {
                    root.onPurge();
                }
            };
            case MYSQL, SQLITE -> new AbstractSQLDataHandler<>(plugin, config) {
                @Override
                public void reload() {
                    root.reload();
                }

                @Override
                public void onSynchronize() {
                    root.onSynchronize();
                }

                @Override
                public void onSave() {
                    root.onSave();
                }

                @Override
                public void onPurge() {
                    root.onPurge();
                }
            };
        };
    }

    public abstract void onSynchronize();

    public abstract void onSave();

    public abstract void onPurge();

    protected void onLoad() {
        delegate.onLoad();
    }

    protected void onShutdown() {
        delegate.onShutdown();
    }

    @NotNull
    public DatabaseConfig getConfig() {
        return this.config;
    }

    @NotNull
    public String getTablePrefix() {
        if (this.getConfig().getTablePrefix().isEmpty()) {
            return this.plugin.getName().replace(" ", "_").toLowerCase();
        }
        return this.getConfig().getTablePrefix();
    }

    @Nullable // MongoDB doesn't need this
    public AbstractConnector getConnector() {
        return this.delegate.getConnector();
    }

    @NotNull
    protected GsonBuilder registerAdapters(@NotNull GsonBuilder builder) {
        return builder.registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
    }

    @Nullable // MongoDB doesn't need this
    protected Connection getConnection() throws SQLException {
        return this.delegate.getConnection();
    }

    public void createTable(@NotNull String table, @NotNull List<SQLColumn> columns) {
        this.delegate.createTable(table, columns);
    }

    public void renameTable(@NotNull String from, @NotNull String to) {
        this.delegate.renameTable(from, to);
    }

    public void addColumn(@NotNull String table, @NotNull SQLValue... columns) {
        this.delegate.addColumn(table, columns);
    }

    public void renameColumn(@NotNull String table, @NotNull SQLValue... columns) {
        this.delegate.renameColumn(table, columns);
    }

    public void dropColumn(@NotNull String table, @NotNull SQLColumn... columns) {
        this.delegate.dropColumn(table, columns);
    }

    public boolean hasColumn(@NotNull String table, @NotNull SQLColumn column) {
        return this.delegate.hasColumn(table, column);
    }

    public void insert(@NotNull String table, @NotNull List<SQLValue> values) {
        this.delegate.insert(table, values);
    }

    @Deprecated
    public void update(@NotNull String table, @NotNull List<SQLValue> values, @NotNull SQLCondition... conditions) {
        this.delegate.update(table, values, conditions);
    }

    @NotNull
    public IUpdateQuery updateQuery(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        return this.delegate.updateQuery(table, values, conditions);
    }

    public void executeUpdate(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        this.delegate.executeUpdate(table, values, conditions);
    }

    public void executeUpdate(@NotNull IUpdateQuery query) {
        this.delegate.executeUpdate(query);
    }

    public void executeUpdates(@NotNull List<IUpdateQuery> queries) {
        this.delegate.executeUpdates(queries);
    }

    public void delete(@NotNull String table, @NotNull SQLCondition... conditions) {
        this.delegate.delete(table, conditions);
    }

    public boolean contains(@NotNull String table, @NotNull SQLCondition... conditions) {
        return this.delegate.contains(table, conditions);
    }

    public boolean contains(@NotNull String table, @NotNull List<SQLColumn> columns, @NotNull SQLCondition... conditions) {
        return this.delegate.contains(table, columns, conditions);
    }

    @NotNull
    public <T> Optional<T> load(@NotNull String table, @NotNull Function<ResultSet, T> function,
                                @NotNull List<SQLColumn> columns,
                                @NotNull List<SQLCondition> conditions) {
        return this.delegate.load(table, function, columns, conditions);
    }

    @NotNull
    public <T> List<T> load(@NotNull String table, @NotNull Function<ResultSet, T> dataFunction,
                            @NotNull List<SQLColumn> columns,
                            @NotNull List<SQLCondition> conditions,
                            int amount) {
        return this.delegate.load(table, dataFunction, columns, conditions, amount);
    }
    @NotNull
    protected static DatabaseConfig getDataConfig(@NotNull NightCorePlugin plugin) {
        DatabaseConfig dataConfig = plugin.getDetails().getDatabaseConfig();
        if (dataConfig == null) {
            plugin.warn("The plugin didn't have database configuration. Fixing it now...");
            dataConfig = DatabaseConfig.read(plugin);
        }
        return dataConfig;
    }
}
