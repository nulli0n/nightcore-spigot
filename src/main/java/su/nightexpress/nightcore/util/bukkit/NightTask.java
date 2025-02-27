package su.nightexpress.nightcore.util.bukkit;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class NightTask {

    private final NightCorePlugin plugin;
    private final MyScheduledTask scheduledTask;

    public NightTask(@NotNull NightCorePlugin plugin, @Nullable MyScheduledTask scheduledTask) {
        this.plugin = plugin;
        this.scheduledTask = scheduledTask;
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return create(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, () -> interval <= 0 ? null : plugin.getScheduler().runTaskTimer(plugin, runnable, 0L, interval));
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return createAsync(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, () -> {
            if (interval <= 0) return null;

            return plugin.getScheduler().runTaskTimer(plugin, () -> CompletableFuture.runAsync(runnable), 0L, interval);
        });
    }

    @NotNull
    private static NightTask createTask(@NotNull NightCorePlugin plugin, @NotNull Supplier<MyScheduledTask> supplier) {
        MyScheduledTask scheduledTask = supplier.get();
        return new NightTask(plugin, scheduledTask);
    }

    @Nullable
    public MyScheduledTask getScheduledTask() {
        return this.scheduledTask;
    }

    public boolean isValid() {
        return this.scheduledTask != null;
    }

    @Deprecated
    public boolean isRunning() {
        return this.isValid();
    }

    public boolean stop() {
        if (this.scheduledTask == null) return false;

        this.scheduledTask.cancel();
        return true;
    }
}
