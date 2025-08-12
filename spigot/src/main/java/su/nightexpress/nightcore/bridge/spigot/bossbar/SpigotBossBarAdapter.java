package su.nightexpress.nightcore.bridge.spigot.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;

public class SpigotBossBarAdapter {

    @NotNull
    public static BarColor adaptColor(@NotNull NightBarColor nightColor) {
        return switch (nightColor) {
            case RED -> BarColor.RED;
            case BLUE -> BarColor.BLUE;
            case PINK -> BarColor.PINK;
            case GREEN -> BarColor.GREEN;
            case WHITE -> BarColor.WHITE;
            case PURPLE -> BarColor.PURPLE;
            case YELLOW -> BarColor.YELLOW;
        };
    }

    @NotNull
    public static NightBarColor wrapColor(@NotNull BarColor color) {
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
    public static BarStyle adaptOverlay(@NotNull NightBarOverlay nightOverlay) {
        return switch (nightOverlay) {
            case PROGRESS -> BarStyle.SOLID;
            case NOTCHED_6 -> BarStyle.SEGMENTED_6;
            case NOTCHED_10 -> BarStyle.SEGMENTED_10;
            case NOTCHED_12 -> BarStyle.SEGMENTED_12;
            case NOTCHED_20 -> BarStyle.SEGMENTED_20;
        };
    }

    @NotNull
    public static NightBarOverlay wrapOverlay(@NotNull BarStyle overlay) {
        return switch (overlay) {
            case SEGMENTED_20 -> NightBarOverlay.NOTCHED_20;
            case SEGMENTED_12 -> NightBarOverlay.NOTCHED_12;
            case SEGMENTED_10 -> NightBarOverlay.NOTCHED_10;
            case SEGMENTED_6 -> NightBarOverlay.NOTCHED_6;
            case SOLID -> NightBarOverlay.PROGRESS;
        };
    }

    @NotNull
    public static BarFlag adaptFlag(@NotNull NightBarFlag nightFlag) {
        return switch (nightFlag) {
            case DARKEN_SCREEN -> BarFlag.DARKEN_SKY;
            case PLAY_BOSS_MUSIC -> BarFlag.PLAY_BOSS_MUSIC;
            case CREATE_WORLD_FOG -> BarFlag.CREATE_FOG;
        };
    }

    @NotNull
    public static NightBarFlag wrapFlag(@NotNull BarFlag flag) {
        return switch (flag) {
            case CREATE_FOG -> NightBarFlag.CREATE_WORLD_FOG;
            case PLAY_BOSS_MUSIC -> NightBarFlag.PLAY_BOSS_MUSIC;
            case DARKEN_SKY -> NightBarFlag.DARKEN_SCREEN;
        };
    }
}
