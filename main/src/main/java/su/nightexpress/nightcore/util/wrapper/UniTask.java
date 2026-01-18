package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedTask;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class UniTask {

    private final NightCorePlugin plugin;
    private final Runnable         runnable;
    private final Set<AdaptedTask> taskIdSet;

    private long    interval;
    private boolean async;

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable) {
        this(plugin, runnable, 0L);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        this(plugin, runnable, interval, false);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval, boolean async) {
        this(plugin, runnable, interval * 20L, async);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        this(plugin, runnable, interval, false);
    }

    public UniTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval, boolean async) {
        this.plugin = plugin;
        this.runnable = runnable;
        this.taskIdSet = ConcurrentHashMap.newKeySet();
        this.interval = interval;
        this.async = async;
    }

    @Deprecated
    public UniTask setSecondsInterval(int interval) {
        return this.setTicksInterval(interval * 20L);
    }

    @Deprecated
    public UniTask setTicksInterval(long interval) {
        this.interval = interval;
        return this;
    }

    @Deprecated
    public UniTask setAsync() {
        return this.setAsync(true);
    }

    @Deprecated
    public UniTask setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public boolean isRunning() {
        return !this.taskIdSet.isEmpty() && taskIdSet.iterator().next().isCurrentlyRunning();
    }

    public final void restart() {
        this.stop();
        this.start();
    }

    public UniTask start() {
        if (!this.taskIdSet.isEmpty() || this.interval <= 0L) return this;

        if (this.async) {
            this.taskIdSet.add(plugin.scheduler().runTaskTimerAsync(runnable, 0L, interval));
        } else {
            this.taskIdSet.add(plugin.scheduler().runTaskTimer(runnable, 0L, interval));
        }
        return this;
    }

    public boolean stop() {
        if (this.taskIdSet.isEmpty()) return false;

        this.taskIdSet.iterator().next().cancel();
        this.taskIdSet.remove(this.taskIdSet.iterator().next());
        return true;
    }
}
