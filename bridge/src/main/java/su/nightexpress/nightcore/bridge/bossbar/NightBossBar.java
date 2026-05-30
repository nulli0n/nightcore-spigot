package su.nightexpress.nightcore.bridge.bossbar;

import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface NightBossBar {

    float MIN_PROGRESS = 0f;

    float MAX_PROGRESS = 1f;

    @NonNull
    NightBossBar setName(@NonNull NightComponent name);

    float getProgress();

    @NonNull
    NightBossBar setProgress(float progress);

    @NonNull
    NightBarColor getColor();

    @NonNull
    NightBossBar setColor(@NonNull NightBarColor color);

    @NonNull
    NightBarOverlay getOverlay();

    @NonNull
    NightBossBar setOverlay(@NonNull NightBarOverlay overlay);

    @NonNull
    Set<NightBarFlag> getFlags();

    @NonNull
    NightBossBar setFlags(@NonNull Set<NightBarFlag> flags);

    boolean hasFlag(@NonNull NightBarFlag flag);

    @NonNull
    NightBossBar addFlag(@NonNull NightBarFlag flag);

    @NonNull
    NightBossBar removeFlag(@NonNull NightBarFlag flag);

    @NonNull
    NightBossBar addFlags(@NonNull NightBarFlag... flags);

    @NonNull
    Set<Player> getViewers();

    @NonNull
    NightBossBar addViewer(@NonNull Player viewer);

    @NonNull
    NightBossBar removeViewer(@NonNull Player viewer);

    @NonNull
    NightBossBar removeViewers();
}
