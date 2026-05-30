package su.nightexpress.nightcore.bridge.paper.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
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
    private final BossBar     backend;

    public PaperBossBar(@NonNull PaperBridge bridge, @NonNull BossBar backend) {
        this.bridge = bridge;
        this.backend = backend;
    }

    @Override
    @NonNull
    public PaperBossBar setName(@NonNull NightComponent name) {
        this.backend.name(this.bridge.getTextComponentAdapter().adaptComponent(name));
        return this;
    }

    @Override
    public float getProgress() {
        return this.backend.progress();
    }

    @Override
    @NonNull
    public PaperBossBar setProgress(float progress) {
        this.backend.progress(Math.clamp(progress, MIN_PROGRESS, MAX_PROGRESS));
        return this;
    }

    @Override
    @NonNull
    public NightBarColor getColor() {
        return PaperBossBarAdapter.wrapColor(this.backend.color());
    }

    @Override
    @NonNull
    public PaperBossBar setColor(@NonNull NightBarColor color) {
        this.backend.color(PaperBossBarAdapter.adaptColor(color));
        return this;
    }

    @Override
    @NonNull
    public NightBarOverlay getOverlay() {
        return PaperBossBarAdapter.wrapOverlay(this.backend.overlay());
    }

    @Override
    @NonNull
    public PaperBossBar setOverlay(@NonNull NightBarOverlay overlay) {
        this.backend.overlay(PaperBossBarAdapter.adaptOverlay(overlay));
        return this;
    }

    @Override
    @UnmodifiableView
    @NonNull
    public Set<NightBarFlag> getFlags() {
        return this.backend.flags().stream().map(PaperBossBarAdapter::wrapFlag).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public PaperBossBar setFlags(@NonNull Set<NightBarFlag> flags) {
        this.backend.flags(Lists.modify(flags, PaperBossBarAdapter::adaptFlag));
        return this;
    }

    @Override
    public boolean hasFlag(@NonNull NightBarFlag flag) {
        return this.backend.hasFlag(PaperBossBarAdapter.adaptFlag(flag));
    }

    @Override
    @NonNull
    public PaperBossBar addFlag(@NonNull NightBarFlag flag) {
        this.backend.addFlag(PaperBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @NonNull
    public PaperBossBar addFlags(@NonNull NightBarFlag... flags) {
        for (NightBarFlag barFlag : flags) {
            this.addFlag(barFlag);
        }
        return this;
    }

    @Override
    @NonNull
    public PaperBossBar removeFlag(@NonNull NightBarFlag flag) {
        this.backend.removeFlag(PaperBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @UnmodifiableView
    @NonNull
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
    @NonNull
    public PaperBossBar addViewer(@NonNull Player viewer) {
        this.backend.addViewer(viewer);
        return this;
    }

    @Override
    @NonNull
    public PaperBossBar removeViewer(@NonNull Player viewer) {
        this.backend.removeViewer(viewer);
        return this;
    }

    @Override
    @NonNull
    public PaperBossBar removeViewers() {
        this.getViewers().forEach(this::removeViewer);
        return this;
    }
}
