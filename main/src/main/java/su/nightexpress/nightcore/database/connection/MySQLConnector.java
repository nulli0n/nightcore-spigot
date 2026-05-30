package su.nightexpress.nightcore.database.connection;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.DatabaseConfig;

@Deprecated
public class MySQLConnector extends AbstractConnector {

    public MySQLConnector(@NonNull NightCorePlugin plugin, @NonNull DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    protected String getURL(@NonNull DatabaseConfig config) {
        String host = config.getHost();
        String database = config.getDatabase();
        String options = config.getUrlOptions();

        return "jdbc:mysql://" + host + "/" + database + options;
    }

    @Override
    protected void setupConfig(@NonNull DatabaseConfig config) {
        this.config.setUsername(config.getUsername());
        this.config.setPassword(config.getPassword());
    }
}
