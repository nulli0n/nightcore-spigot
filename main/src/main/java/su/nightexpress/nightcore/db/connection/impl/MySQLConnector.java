package su.nightexpress.nightcore.db.connection.impl;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.DatabaseConstants;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.connection.AbstractConnector;

public class MySQLConnector extends AbstractConnector {

    public MySQLConnector(@NonNull NightPlugin plugin, @NonNull DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    protected String getURL(@NonNull DatabaseConfig databaseConfig) {
        String host = databaseConfig.getHost();
        String database = databaseConfig.getDatabase();
        String options = databaseConfig.getUrlOptions();

        return "jdbc:mysql://" + host + "/" + database + options;
    }

    @Override
    protected void setupConfig(@NonNull DatabaseConfig databaseConfig) {
        this.config.setUsername(databaseConfig.getUsername());
        this.config.setPassword(databaseConfig.getPassword());
        this.config.setMaxLifetime(databaseConfig.getMaxLifetime());
        this.config.setConnectionInitSql("SET %s= '%s';"
            .formatted(DatabaseConstants.SQL_VAR_SERVER_INSTANCE, databaseConfig.getServerId())
        );
    }
}
