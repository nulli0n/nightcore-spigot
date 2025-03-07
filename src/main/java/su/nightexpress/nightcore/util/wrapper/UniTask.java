package su.nightexpress.nightcore.util.wrapper;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class UniTask {

    private final NightCorePlugin plugin;
    private final Runnable        runnable;
    private final ConcurrentHashMap<Integer, MyScheduledTask> taskIdHashMap;

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
        this.taskIdHashMap = new ConcurrentHashMap<>();
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
        return !this.taskIdHashMap.isEmpty();
    }

    public final void restart() {
        this.stop();
        this.start();
    }

    public UniTask start() {
        if (this.taskIdHashMap.isEmpty() || this.interval <= 0L) return this;

        if (this.async) {
            this.taskIdHashMap.put(taskIdHashMap.size() + 1, plugin.getFoliaScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, interval));
        } else {
            this.taskIdHashMap.put(taskIdHashMap.size() + 1, plugin.getFoliaScheduler().runTaskTimer(plugin, runnable, 0L, interval));
        }
        return this;
    }

    public boolean stop() {
        if (this.taskIdHashMap.isEmpty()) return false;

        this.taskIdHashMap.forEach((id, task) -> task.cancel());
        this.taskIdHashMap.remove(taskIdHashMap.size() - 1);
        return true;
    }
}
