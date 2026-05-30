package su.nightexpress.nightcore.db.connection.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.connection.AbstractConnector;

public class SQLiteConnector extends AbstractConnector {

    public SQLiteConnector(@NonNull NightPlugin plugin, @NonNull DatabaseConfig config) {
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
