package su.nightexpress.nightcore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractConnector {

    protected final NightCorePlugin plugin;
    //protected final String           url;
    protected final HikariConfig    config;
    protected final HikariDataSource dataSource;

    public AbstractConnector(@NotNull NightCorePlugin plugin, @NotNull DatabaseConfig config) {
        this.plugin = plugin;
        //this.url = getURL(config);

        this.config = new HikariConfig();
        this.config.setJdbcUrl(this.getURL(config));
        this.setupConfig(config);
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(this.config);
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
