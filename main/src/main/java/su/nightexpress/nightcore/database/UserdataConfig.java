package su.nightexpress.nightcore.database;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

@Deprecated
public class UserdataConfig {

    private final long cacheLifetime;

    private final long   saveInterval;
    private final double saveDelay;
    private final int    saveSyncPause;

    public UserdataConfig(long cacheLifetime, long saveInterval, double saveDelay, int saveSyncPause) {
        this.cacheLifetime = cacheLifetime;
        this.saveInterval = saveInterval;
        this.saveDelay = saveDelay;
        this.saveSyncPause = saveSyncPause;
    }

    @NotNull
    public static UserdataConfig read(@NotNull NightPlugin plugin) {
        FileConfig config = plugin.getConfig();

        long cacheLifetime = ConfigValue.create("Database.UserData.Cache_LifeTime",
            300L,
            "Sets cache lifetime for player's data.",
            "Data loaded for offline players and data of previously online players will be cached in the memory for that time.",
            "When cache is expired, data have to be loaded from the database again.",
            "[*] Set to 0 to disable data cache for offline players.",
            "[*] Set to -1 for permanent data cache for offline players.",
            "[Default is 300 (5 minutes)]"
        ).read(config);

        long saveInterval = ConfigValue.create("Database.UserData.Scheduled_Save_Interval",
            20L,
            "Sets how often (in ticks) plugin will attempt to save data of users marked to be saved.",
            "This will save only users that are 'ready' to save (see 'Scheduled_Save_Delay').",
            "[DO NOT DISABLE UNDER ANY CIRCUMSTANCES!]",
            "[20 ticks = 1 second]",
            "[Default is 20]"
        ).read(config);

        double saveDelay = ConfigValue.create("Database.UserData.Scheduled_Save_Delay",
            1D,
            "Sets scheduled save delay (in seconds) for a user when marked to be saved.",
            "This means that a user will be saved X seconds later after being marked.",
            "Generally, you should keep this value less than 'Scheduled_Save_Interval' for best results,",
            "and not setting it extremely high, otherwise you may result in user data never saved & synchronized.",
            "",
            "IMPORTANT NOTE #1: When a user is marked for saving, this will prevent them from syncing until saving occurs!",
            "IMPORTANT NOTE #2: Every time a user is marked for saving, their save time will be updated with this value!",
            "",
            "Users marked for save when their data has been changed/affected in some way.",
            "However this is higly depends on the plugin.",
            "[Decimals allowed]",
            "[Default is 1]"
        ).read(config);

        int saveSyncPause = ConfigValue.create("Database.UserData.Scheduled_Save_Sync_Pause",
            3,
            "Sets synchronization delay (in seconds) before the plugin can continue syncing data for a user after its scheduled save.",
            "When a user marked for saving has been saved, their synchronization will be unlocked but delayed for this value.",
            "This might be helpful to prevent possible issues when synchronization process access the database before user data was fully saved.",
            "[Default is 3]"
        ).read(config);


        return new UserdataConfig(cacheLifetime, saveInterval, saveDelay, saveSyncPause);
    }

    public long getCacheLifetime() {
        return this.cacheLifetime;
    }

    public long getSaveInterval() {
        return this.saveInterval;
    }

    public double getSaveDelay() {
        return this.saveDelay;
    }

    public int getSaveSyncPause() {
        return this.saveSyncPause;
    }

    @Override
    public String toString() {
        return "UserdataConfig{" +
            "scheduledSaveInterval=" + saveInterval +
            ", scheduledSaveDelay=" + saveDelay +
            ", scheduledSaveSyncPause=" + saveSyncPause +
            '}';
    }
}
