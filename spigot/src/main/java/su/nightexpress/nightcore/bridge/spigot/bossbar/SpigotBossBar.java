package su.nightexpress.nightcore.bridge.spigot.bossbar;

import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.bossbar.NightBossBar;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.HashSet;
import java.util.Set;

public class SpigotBossBar implements NightBossBar {

    private final BossBar backend;

    public SpigotBossBar(@NotNull BossBar backend) {
        this.backend = backend;
    }

    @Override
    @NotNull
    public SpigotBossBar setName(@NotNull NightComponent name) {
        this.backend.setTitle(name.toLegacy());
        return this;
    }

    @Override
    public float getProgress() {
        return (float) this.backend.getProgress();
    }

    @Override
    @NotNull
    public SpigotBossBar setProgress(float progress) {
        this.backend.setProgress(Math.clamp(progress, MIN_PROGRESS, MAX_PROGRESS));
        return this;
    }

    @Override
    @NotNull
    public NightBarColor getColor() {
        return SpigotBossBarAdapter.wrapColor(this.backend.getColor());
    }

    @Override
    @NotNull
    public SpigotBossBar setColor(@NotNull NightBarColor color) {
        this.backend.setColor(SpigotBossBarAdapter.adaptColor(color));
        return this;
    }

    @Override
    @NotNull
    public NightBarOverlay getOverlay() {
        return SpigotBossBarAdapter.wrapOverlay(this.backend.getStyle());
    }

    @Override
    @NotNull
    public SpigotBossBar setOverlay(@NotNull NightBarOverlay overlay) {
        this.backend.setStyle(SpigotBossBarAdapter.adaptOverlay(overlay));
        return this;
    }

    @Override
    @UnmodifiableView
    @NotNull
    public Set<NightBarFlag> getFlags() {
        Set<NightBarFlag> flags = new HashSet<>();
        for (BarFlag value : BarFlag.values()) {
            if (this.backend.hasFlag(value)) {
                flags.add(SpigotBossBarAdapter.wrapFlag(value));
            }
        }
        return flags;
    }

    @Override
    @NotNull
    public SpigotBossBar setFlags(@NotNull Set<NightBarFlag> flags) {
        flags.forEach(nightFlag -> this.backend.addFlag(SpigotBossBarAdapter.adaptFlag(nightFlag)));
        return this;
    }

    @Override
    public boolean hasFlag(@NotNull NightBarFlag flag) {
        return this.backend.hasFlag(SpigotBossBarAdapter.adaptFlag(flag));
    }

    @Override
    @NotNull
    public SpigotBossBar addFlag(@NotNull NightBarFlag flag) {
        this.backend.addFlag(SpigotBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @NotNull
    public SpigotBossBar addFlags(@NotNull NightBarFlag... flags) {
        for (NightBarFlag barFlag : flags) {
            this.addFlag(barFlag);
        }
        return this;
    }

    @Override
    @NotNull
    public SpigotBossBar removeFlag(@NotNull NightBarFlag flag) {
        this.backend.removeFlag(SpigotBossBarAdapter.adaptFlag(flag));
        return this;
    }

    @Override
    @UnmodifiableView
    @NotNull
    public Set<Player> getViewers() {
        return new HashSet<>(this.backend.getPlayers());
    }

    @Override
    @NotNull
    public SpigotBossBar addViewer(@NotNull Player viewer) {
        this.backend.removePlayer(viewer);
        return this;
    }

    @Override
    @NotNull
    public SpigotBossBar removeViewer(@NotNull Player viewer) {
        this.backend.addPlayer(viewer);
        return this;
    }

    @Override
    @NotNull
    public SpigotBossBar removeViewers() {
        this.backend.removeAll();
        return this;
    }
}
