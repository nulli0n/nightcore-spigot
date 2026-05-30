package su.nightexpress.nightcore.database.connection;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.DatabaseConfig;

@Deprecated
public class SQLiteConnector extends AbstractConnector {

    public SQLiteConnector(@NonNull NightCorePlugin plugin, @NonNull DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    protected String getURL(@NonNull DatabaseConfig config) {
        String filePath = plugin.getDataFolder().getAbsolutePath() + "/" + config.getFilename();
        return "jdbc:sqlite:" + filePath;
    }

    @Override
    protected void setupConfig(@NonNull DatabaseConfig config) {
        this.config.setMaximumPoolSize(1);
    }
}
