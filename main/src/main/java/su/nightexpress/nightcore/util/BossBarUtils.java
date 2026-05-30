package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

public class BossBarUtils {

    @NonNull
    public static NightBossBar createBossBar(@NonNull String title, @NonNull NightBarColor color,
                                             @NonNull NightBarOverlay overlay, @NonNull NightBarFlag... flags) {
        NightComponent titleComponent = NightMessage.parse(title);
        return createBossBar(titleComponent, color, overlay, flags);
    }

    @NonNull
    public static NightBossBar createBossBar(@NonNull NightComponent title, @NonNull NightBarColor color,
                                             @NonNull NightBarOverlay overlay, @NonNull NightBarFlag... flags) {
        return Software.instance().createBossBar(title, color, overlay, flags);
    }
}
