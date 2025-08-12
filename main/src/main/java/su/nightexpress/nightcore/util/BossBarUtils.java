package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

public class BossBarUtils {

    @NotNull
    public static NightBossBar createBossBar(@NotNull String title, @NotNull NightBarColor color, @NotNull NightBarOverlay overlay, @NotNull NightBarFlag... flags) {
        NightComponent titleComponent = NightMessage.parse(title);
        return createBossBar(titleComponent, color, overlay, flags);
    }

    @NotNull
    public static NightBossBar createBossBar(@NotNull NightComponent title, @NotNull NightBarColor color, @NotNull NightBarOverlay overlay, @NotNull NightBarFlag... flags) {
        return Software.instance().createBossBar(title, color, overlay, flags);
    }
}
