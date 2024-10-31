package su.nightexpress.nightcore.db.connection.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.connection.AbstractConnector;

public class SQLiteConnector extends AbstractConnector {

    public SQLiteConnector(@NotNull NightPlugin plugin, @NotNull DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    protected String getURL(@NotNull DatabaseConfig config) {
        String filePath = plugin.getDataFolder().getAbsolutePath() + "/" + config.getFilename();
        return "jdbc:sqlite:" + filePath;
    }

    @Override
    protected void setupConfig(@NotNull DatabaseConfig config) {
        this.config.setMaximumPoolSize(1);
    }
}
