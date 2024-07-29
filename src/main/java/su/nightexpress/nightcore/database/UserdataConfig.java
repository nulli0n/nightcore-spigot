package su.nightexpress.nightcore.database;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class UserdataConfig {

    private final long scheduledSaveInterval;
    private final int scheduledSaveDelay;
    private final int scheduledSaveSyncPause;

    public UserdataConfig(long scheduledSaveInterval, int scheduledSaveDelay, int scheduledSaveSyncPause) {
        this.scheduledSaveInterval = scheduledSaveInterval;
        this.scheduledSaveDelay = scheduledSaveDelay;
        this.scheduledSaveSyncPause = scheduledSaveSyncPause;
    }

    @NotNull
    public static UserdataConfig read(@NotNull NightPlugin plugin) {
        FileConfig config = plugin.getConfig();

        long scheduledSaveInterval = ConfigValue.create("Database.UserData.Scheduled_Save_Interval",
            20L,
            "Sets how often (in ticks) plugin will attempt to save data of users marked to be saved.",
            "This will save only users that are 'ready' to save (see 'Scheduled_Save_Delay').",
            "[DO NOT DISABLE UNDER ANY CIRCUMSTANCES!]",
            "[20 ticks = 1 second]",
            "[Default is 20]"
        ).read(config);

        int scheduledSaveDelay = ConfigValue.create("Database.UserData.Scheduled_Save_Delay",
            1,
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
            "[Default is 1]"
        ).read(config);

        int scheduledSaveSynchronizationPause = ConfigValue.create("Database.UserData.Scheduled_Save_Sync_Pause",
            3,
            "Sets synchronization delay (in seconds) before the plugin can continue syncing data for a user after its scheduled save.",
            "When a user marked for saving has been saved, their synchronization will be unlocked but delayed for this value.",
            "This might be helpful to prevent possible issues when synchronization process access the database before user data was fully saved.",
            "[Default is 3]"
        ).read(config);


        return new UserdataConfig(scheduledSaveInterval, scheduledSaveDelay, scheduledSaveSynchronizationPause);
    }

    public long getScheduledSaveInterval() {
        return scheduledSaveInterval;
    }

    public int getScheduledSaveDelay() {
        return scheduledSaveDelay;
    }

    public int getScheduledSaveSyncPause() {
        return scheduledSaveSyncPause;
    }

    @Override
    public String toString() {
        return "UserdataConfig{" +
            "scheduledSaveInterval=" + scheduledSaveInterval +
            ", scheduledSaveDelay=" + scheduledSaveDelay +
            ", scheduledSaveSyncPause=" + scheduledSaveSyncPause +
            '}';
    }
}
