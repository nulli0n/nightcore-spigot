package su.nightexpress.nightcore.bridge.paper.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PaperBossBar implements NightBossBar {

    private final PaperBridge bridge;
    private final BossBar backend;

    public PaperBossBar(@NotNull PaperBridge bridge, @NotNull BossBar backend) {
        this.bridge = bridge;
        this.backend = backend;
    }

    @Override
    @NotNull
    public PaperBossBar setName(@NotNull NightComponent name) {
        this.backend.name(this.bridge.getTextComponentAdapter().adaptComponent(name));
        return this;
    }

    @Override
    public float getProgress() {
        return this.backend.progress();
    }

    @Override
    @NotNull
    public PaperBossBar setProgress(float progress) {
        this.backend.progress(Math.clamp(progress, MIN_PROGRESS, MAX_PROGRESS));
        return this;
    }

    @Override
    @NotNull
    public NightBarColor getColor() {
        return PaperBossBarAdapter.wrapColor(this.backend.color());
    }

    @Override
    @NotNull
    public PaperBossBar setColor(@NotNull NightBarColor color) {
        this.backend.color(PaperBossBarAdapter.adaptColor(color));
        return this;
    }

    @Override
    @NotNull
    public NightBarOverlay getOverlay() {
        return PaperBossBarAdapter.wrapOverlay(this.backend.overlay());
    }

    @Override
    @NotNull
    public PaperBossBar setOverlay(@NotNull NightBarOverlay overlay) {
        this.backend.overlay(PaperBossBarAdapter.adaptOverlay(overlay));
        return this;
    }

    @Override
    @UnmodifiableView
    @NotNull
    public Set<NightBarFlag> getFlags() {
        return this.backend.flags().stream().map(PaperBossBarAdapter::wrapFlag).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public PaperBossBar setFlags(@NotNull Set<NightBarFlag> flags) {
        this.backend.flags(Lists.modify(flags, PaperBossBarAdapter::adaptFlag));
        return this;
    }

    @Override
    public boolean hasFlag(@NotNull NightBarFlag flag) {
        return this.backend.hasFlag(PaperBossBarAdapter.adaptFlag(flag));
    }

    @Override
    @NotNull
    public PaperBossBar addFlag(@NotNull NightBarFlag flag) {
        this.backend.addFlag(PaperBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @NotNull
    public PaperBossBar addFlags(@NotNull NightBarFlag... flags) {
        for (NightBarFlag barFlag : flags) {
            this.addFlag(barFlag);
        }
        return this;
    }

    @Override
    @NotNull
    public PaperBossBar removeFlag(@NotNull NightBarFlag flag) {
        this.backend.removeFlag(PaperBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @UnmodifiableView
    @NotNull
    public Set<Player> getViewers() {
        Set<Player> players = new HashSet<>();
        this.backend.viewers().forEach(viewer -> {
            if (viewer instanceof Player player) {
                players.add(player);
            }
        });
        return players;
    }

    @Override
    @NotNull
    public PaperBossBar addViewer(@NotNull Player viewer) {
        this.backend.addViewer(viewer);
        return this;
    }

    @Override
    @NotNull
    public PaperBossBar removeViewer(@NotNull Player viewer) {
        this.backend.removeViewer(viewer);
        return this;
    }

    @Override
    @NotNull
    public PaperBossBar removeViewers() {
        this.getViewers().forEach(this::removeViewer);
        return this;
    }
}
