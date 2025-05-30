package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.function.Function;

public class NightTask {

    private final NightCorePlugin plugin;
    private final BukkitTask bukkitTask;

    public NightTask(@NotNull NightCorePlugin plugin, @Nullable BukkitTask bukkitTask) {
        this.plugin = plugin;
        this.bukkitTask = bukkitTask;
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return create(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, scheduler -> interval <= 0 ? null : scheduler.runTaskTimer(plugin, runnable, 0L, interval));
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return createAsync(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, scheduler -> interval <= 0 ? null : scheduler.runTaskTimerAsynchronously(plugin, runnable, 0L, interval));
    }

    @NotNull
    private static NightTask createTask(@NotNull NightCorePlugin plugin, @NotNull Function<BukkitScheduler, BukkitTask> function) {
        BukkitTask bukkitTask = function.apply(plugin.getScheduler());
        return new NightTask(plugin, bukkitTask);
    }

    @Nullable
    public BukkitTask getBukkitTask() {
        return this.bukkitTask;
    }

    public boolean isValid() {
        return this.bukkitTask != null;
    }

    @Deprecated
    public boolean isRunning() {
        return this.isValid();//this.bukkitTask != null && this.plugin.getScheduler().isCurrentlyRunning(this.bukkitTask.getTaskId());
    }

    public boolean stop() {
        if (this.bukkitTask == null) return false;

        this.plugin.getScheduler().cancelTask(this.bukkitTask.getTaskId());
        return true;
    }
}
