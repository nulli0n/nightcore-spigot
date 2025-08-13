package su.nightexpress.nightcore.bridge.paper.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;

public class PaperBossBarAdapter {

    @NotNull
    public static BossBar.Color adaptColor(@NotNull NightBarColor nightColor) {
        return switch (nightColor) {
            case RED -> BossBar.Color.RED;
            case BLUE -> BossBar.Color.BLUE;
            case PINK -> BossBar.Color.PINK;
            case GREEN -> BossBar.Color.GREEN;
            case WHITE -> BossBar.Color.WHITE;
            case PURPLE -> BossBar.Color.PURPLE;
            case YELLOW -> BossBar.Color.YELLOW;
        };
    }

    @NotNull
    public static NightBarColor wrapColor(@NotNull BossBar.Color color) {
        return switch (color) {
            case YELLOW -> NightBarColor.YELLOW;
            case PURPLE -> NightBarColor.PURPLE;
            case WHITE -> NightBarColor.WHITE;
            case GREEN -> NightBarColor.GREEN;
            case PINK -> NightBarColor.PINK;
            case BLUE -> NightBarColor.BLUE;
            case RED -> NightBarColor.RED;
        };
    }

    @NotNull
    public static BossBar.Overlay adaptOverlay(@NotNull NightBarOverlay nightOverlay) {
        return switch (nightOverlay) {
            case PROGRESS -> BossBar.Overlay.PROGRESS;
            case NOTCHED_6 -> BossBar.Overlay.NOTCHED_6;
            case NOTCHED_10 -> BossBar.Overlay.NOTCHED_10;
            case NOTCHED_12 -> BossBar.Overlay.NOTCHED_12;
            case NOTCHED_20 -> BossBar.Overlay.NOTCHED_20;
        };
    }

    @NotNull
    public static NightBarOverlay wrapOverlay(@NotNull BossBar.Overlay overlay) {
        return switch (overlay) {
            case NOTCHED_20 -> NightBarOverlay.NOTCHED_20;
            case NOTCHED_12 -> NightBarOverlay.NOTCHED_12;
            case NOTCHED_10 -> NightBarOverlay.NOTCHED_10;
            case NOTCHED_6 -> NightBarOverlay.NOTCHED_6;
            case PROGRESS -> NightBarOverlay.PROGRESS;
        };
    }

    @NotNull
    public static BossBar.Flag adaptFlag(@NotNull NightBarFlag nightFlag) {
        return switch (nightFlag) {
            case DARKEN_SCREEN -> BossBar.Flag.DARKEN_SCREEN;
            case PLAY_BOSS_MUSIC -> BossBar.Flag.PLAY_BOSS_MUSIC;
            case CREATE_WORLD_FOG -> BossBar.Flag.CREATE_WORLD_FOG;
        };
    }

    @NotNull
    public static NightBarFlag wrapFlag(@NotNull BossBar.Flag flag) {
        return switch (flag) {
            case CREATE_WORLD_FOG -> NightBarFlag.CREATE_WORLD_FOG;
            case PLAY_BOSS_MUSIC -> NightBarFlag.PLAY_BOSS_MUSIC;
            case DARKEN_SCREEN -> NightBarFlag.DARKEN_SCREEN;
        };
    }
}
