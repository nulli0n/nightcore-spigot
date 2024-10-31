package su.nightexpress.nightcore.db.connection.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.connection.AbstractConnector;

public class MySQLConnector extends AbstractConnector {

    public MySQLConnector(@NotNull NightPlugin plugin, @NotNull DatabaseConfig config) {
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
