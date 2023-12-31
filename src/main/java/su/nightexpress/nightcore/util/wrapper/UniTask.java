package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;

public class UniTask {

    private final NightCorePlugin plugin;
    private final Runnable        runnable;

    private long    interval;
    private boolean async;
    private int     taskId;

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable) {
        this(plugin, runnable, 0L);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        this(plugin, runnable, interval, false);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval, boolean async) {
        this.plugin = plugin;
        this.runnable = runnable;
        this.setTicksInterval(interval);
        this.setAsync(async);

        this.taskId = -1;
    }

    public UniTask setSecondsInterval(int interval) {
        return this.setTicksInterval(interval * 20L);
    }

    public UniTask setTicksInterval(long interval) {
        this.interval = interval;
        return this;
    }

    public UniTask setAsync() {
        return this.setAsync(true);
    }

    public UniTask setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public boolean isRunning() {
        return this.taskId >= 0 && this.plugin.getScheduler().isCurrentlyRunning(this.taskId);
    }

    public final void restart() {
        this.stop();
        this.start();
    }

    public UniTask start() {
        if (this.taskId >= 0 || this.interval <= 0L) return this;

        if (this.async) {
            this.taskId = plugin.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, interval).getTaskId();
        }
        else {
            this.taskId = plugin.getScheduler().runTaskTimer(plugin, runnable, 0L, interval).getTaskId();
        }
        return this;
    }

    public boolean stop() {
        if (this.taskId < 0) return false;

        this.plugin.getServer().getScheduler().cancelTask(this.taskId);
        this.taskId = -1;
        return true;
    }
}
