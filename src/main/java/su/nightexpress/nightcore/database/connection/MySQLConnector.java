package su.nightexpress.nightcore.database.connection;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.DatabaseConfig;

public class MySQLConnector extends AbstractConnector {

    public MySQLConnector(@NotNull NightCorePlugin plugin, @NotNull DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    protected String getURL(@NotNull DatabaseConfig config) {
        String host = config.getHost();
        String database = config.getDatabase();
        String options = config.getUrlOptions();

        return "jdbc:mysql://" + host + "/" + database + options;
    }

    @Override
    protected void setupConfig(@NotNull DatabaseConfig config) {
        this.config.setUsername(config.getUsername());
        this.config.setPassword(config.getPassword());
    }
}
