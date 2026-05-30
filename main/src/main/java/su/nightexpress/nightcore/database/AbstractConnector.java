package su.nightexpress.nightcore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.connection.MySQLConnector;
import su.nightexpress.nightcore.database.connection.SQLiteConnector;

import java.sql.Connection;
import java.sql.SQLException;

@Deprecated
public abstract class AbstractConnector {

    protected final NightCorePlugin  plugin;
    protected final HikariConfig     config;
    protected final HikariDataSource dataSource;

    public AbstractConnector(@NonNull NightCorePlugin plugin, @NonNull DatabaseConfig config) {
        this.plugin = plugin;

        this.config = new HikariConfig();
        this.config.setJdbcUrl(this.getURL(config));
        this.setupConfig(config);
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(this.config);
    }

    @NonNull
    public static AbstractConnector create(@NonNull NightCorePlugin plugin, @NonNull DatabaseConfig config) {
        return config
            .getStorageType() == DatabaseType.SQLITE ? new SQLiteConnector(plugin, config) : new MySQLConnector(plugin, config);
    }

    protected abstract String getURL(@NonNull DatabaseConfig config);

    protected abstract void setupConfig(@NonNull DatabaseConfig config);

    public void close() {
        this.dataSource.close();
    }

    @NonNull
    public final Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
