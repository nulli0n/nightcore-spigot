package su.nightexpress.nightcore.bridge.bossbar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Set;

public interface NightBossBar {

    float MIN_PROGRESS = 0f;

    float MAX_PROGRESS = 1f;

    //@NotNull NightComponent getName();

    @NotNull NightBossBar setName(@NotNull NightComponent name);

    float getProgress();
    
    @NotNull NightBossBar setProgress(float progress);

    @NotNull NightBarColor getColor();

    @NotNull NightBossBar setColor(@NotNull NightBarColor color);

    @NotNull NightBarOverlay getOverlay();
    
    @NotNull NightBossBar setOverlay(@NotNull NightBarOverlay overlay);

    @UnmodifiableView
    @NotNull Set<NightBarFlag> getFlags();
    
    @NotNull NightBossBar setFlags(@NotNull Set<NightBarFlag> flags);

    boolean hasFlag(@NotNull NightBarFlag flag);
    
    @NotNull NightBossBar addFlag(@NotNull NightBarFlag flag);
    
    @NotNull NightBossBar removeFlag(@NotNull NightBarFlag flag);

    @NotNull NightBossBar addFlags(@NotNull NightBarFlag... flags);
    
    @UnmodifiableView
    @NotNull Set<Player> getViewers();
    
    @NotNull NightBossBar addViewer(@NotNull Player viewer);

    @NotNull NightBossBar removeViewer(@NotNull Player viewer);

    @NotNull NightBossBar removeViewers();
}
