package su.nightexpress.nightcore.db.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.impl.MySQLConnector;
import su.nightexpress.nightcore.db.connection.impl.SQLiteConnector;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractConnector {

    protected final NightPlugin  plugin;
    protected final HikariConfig config;
    protected final HikariDataSource dataSource;

    public AbstractConnector(@NotNull NightPlugin plugin, @NotNull DatabaseConfig config) {
        this.plugin = plugin;

        this.config = new HikariConfig();
        this.config.setJdbcUrl(this.getURL(config));
        this.setupConfig(config);
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(this.config);
    }

    @NotNull
    public static AbstractConnector create(@NotNull NightPlugin plugin, @NotNull DatabaseConfig config) {
        return config.getStorageType() == DatabaseType.SQLITE ? new SQLiteConnector(plugin, config) : new MySQLConnector(plugin, config);
    }

    protected abstract String getURL(@NotNull DatabaseConfig config);

    protected abstract void setupConfig(@NotNull DatabaseConfig config);

    public void close() {
        this.dataSource.close();
    }

    @NotNull
    public final Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
