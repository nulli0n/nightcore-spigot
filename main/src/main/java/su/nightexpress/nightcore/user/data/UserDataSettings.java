package su.nightexpress.nightcore.user.data;

import su.nightexpress.nightcore.configuration.AbstractConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigTypes;

public class UserDataSettings extends AbstractConfig {

    private final ConfigProperty<Long> cacheLifetime = this.addProperty(ConfigTypes.LONG, "UserData.Cache.LifeTime",
        300L,
        "Sets cache lifetime for player's data.",
        "Data loaded for offline players and data of previously online players will be cached in the memory for that time.",
        "When cache is expired, data have to be loaded from the database again.",
        "[*] Set to 0 to disable data cache for offline players.",
        "[*] Set to -1 for permanent data cache for offline players.",
        "[Default is 300 (5 minutes)]"
    );

    private final ConfigProperty<Integer> cacheCleanupInterval = this.addProperty(ConfigTypes.INT, "UserData.Cache.CleanUp_Interval",
        300,
        "Sets how often (in seconds) plugin will clean up user data cache.",
        "Cache contains data loaded for offline players and data of previously online players",
        "[*] Set to -1 to disable cache clean up and keep user data loaded until reboot.",
        "[Default is 300 (5 minutes)]"
    );

    private final ConfigProperty<Long> saveInterval = this.addProperty(ConfigTypes.LONG, "UserData.Scheduled_Save_Interval",
        20L,
        "Sets how often (in ticks) plugin will attempt to save data of users marked to be saved.",
        "This will save only users that are 'ready' to save (see 'Scheduled_Save_Delay').",
        "[DO NOT DISABLE UNDER ANY CIRCUMSTANCES!]",
        "[20 ticks = 1 second]",
        "[Default is 20]"
    );

    public long getCacheLifetime() {
        return this.cacheLifetime.get();
    }

    public int getCacheCleanupInterval() {
        return this.cacheCleanupInterval.get();
    }

    public long getSaveInterval() {
        return this.saveInterval.get();
    }
}
