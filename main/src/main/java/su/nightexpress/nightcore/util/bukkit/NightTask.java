package su.nightexpress.nightcore.util.bukkit;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedTask;
import su.nightexpress.nightcore.util.TimeUtil;

public class NightTask {

    private final AdaptedTask scheduledTask;

    public NightTask(@NonNull NightCorePlugin plugin, @Nullable AdaptedTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @NonNull
    public static NightTask create(@NonNull NightCorePlugin plugin, @NonNull Runnable runnable, int interval) {
        return create(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NonNull
    public static NightTask create(@NonNull NightCorePlugin plugin, @NonNull Runnable runnable, long interval) {
        return createTask(plugin, () -> interval <= 0 ? null : plugin.scheduler().runTaskTimer(runnable, 0L, interval));
    }

    @NonNull
    public static NightTask createAsync(@NonNull NightCorePlugin plugin, @NonNull Runnable runnable, int interval) {
        return createAsync(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NonNull
    public static NightTask createAsync(@NonNull NightCorePlugin plugin, @NonNull Runnable runnable, long interval) {
        return createTask(plugin, () -> {
            if (interval <= 0) return null;

            return plugin.scheduler().runTaskTimer(() -> {
                CompletableFuture.runAsync(runnable).whenComplete((empty, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                });
            }, 0L, interval);
        });
    }

    @NonNull
    private static NightTask createTask(@NonNull NightCorePlugin plugin, @NonNull Supplier<AdaptedTask> supplier) {
        AdaptedTask scheduledTask = supplier.get();
        return new NightTask(plugin, scheduledTask);
    }

    @Nullable
    public AdaptedTask getScheduledTask() {
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
