package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.scheduler.NightScheduler;
import su.nightexpress.nightcore.util.scheduler.SchedulerFactory;

import java.util.function.Function;

public class NightTask {

    private final NightCorePlugin plugin;
    private final BukkitTask bukkitTask;
    private final NightScheduler nightScheduler;
    private final Runnable runnable;
    private final long interval;
    private final boolean isAsync;
    private boolean isRunning;

    public NightTask(@NotNull NightCorePlugin plugin, @Nullable BukkitTask bukkitTask) {
        this(plugin, bukkitTask, null, null, 0, false);
    }

    private NightTask(@NotNull NightCorePlugin plugin, @Nullable BukkitTask bukkitTask, 
                     @Nullable NightScheduler nightScheduler, @Nullable Runnable runnable, 
                     long interval, boolean isAsync) {
        this.plugin = plugin;
        this.bukkitTask = bukkitTask;
        this.nightScheduler = nightScheduler;
        this.runnable = runnable;
        this.interval = interval;
        this.isAsync = isAsync;
        this.isRunning = false;
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return create(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask create(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, runnable, interval, false);
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, int interval) {
        return createAsync(plugin, runnable, TimeUtil.secondsToTicks(interval));
    }

    @NotNull
    public static NightTask createAsync(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, long interval) {
        return createTask(plugin, runnable, interval, true);
    }

    @NotNull
    private static NightTask createTask(@NotNull NightCorePlugin plugin, @NotNull Runnable runnable, 
                                       long interval, boolean isAsync) {
        // 使用新的调度器系统，但保持向后兼容性
        NightScheduler scheduler = SchedulerFactory.createScheduler(plugin);
        
        // 对于Folia，我们需要使用新的调度器API
        if (scheduler.getSchedulerType().name().equals("FOLIA")) {
            // 在Folia中，我们使用新的调度器来创建任务
            // 返回一个特殊的NightTask，它知道如何在Folia环境下管理任务
            return new NightTask(plugin, null, scheduler, runnable, interval, isAsync);
        } else {
            // 传统Bukkit方式
            Function<BukkitScheduler, BukkitTask> function = isAsync ? 
                s -> interval <= 0 ? null : s.runTaskTimerAsynchronously(plugin, runnable, 0L, interval) :
                s -> interval <= 0 ? null : s.runTaskTimer(plugin, runnable, 0L, interval);
            
            BukkitTask bukkitTask = function.apply(plugin.getScheduler());
            return new NightTask(plugin, bukkitTask);
        }
    }

    /**
     * 启动任务（对于Folia环境）
     */
    public void start() {
        if (this.isRunning) return;
        
        if (this.nightScheduler != null && this.runnable != null && this.interval > 0) {
            // Folia环境：使用新的调度器启动任务
            if (this.isAsync) {
                this.nightScheduler.runTaskTimerAsync(this.runnable, 0L, this.interval);
            } else {
                this.nightScheduler.runTaskTimer(this.runnable, 0L, this.interval);
            }
            this.isRunning = true;
        }
        // 对于传统Bukkit环境，任务在创建时就已经启动了
    }

    @Nullable
    public BukkitTask getBukkitTask() {
        return this.bukkitTask;
    }

    public boolean isValid() {
        if (this.nightScheduler != null) {
            // Folia环境：只要调度器存在就认为有效
            return this.nightScheduler != null;
        }
        return this.bukkitTask != null;
    }

    @Deprecated
    public boolean isRunning() {
        if (this.nightScheduler != null) {
            // Folia环境：使用我们自己的运行状态标记
            return this.isRunning;
        }
        return this.isValid();
    }

    public boolean stop() {
        if (this.nightScheduler != null) {
            // Folia环境：标记任务为停止状态
            // 注意：在Folia中，我们无法直接取消单个任务，因为新的调度器没有返回任务ID
            // 所以只能标记为停止，实际的任务会继续运行直到插件卸载时统一取消
            this.isRunning = false;
            return true;
        }
        
        if (this.bukkitTask == null) return false;

        this.plugin.getScheduler().cancelTask(this.bukkitTask.getTaskId());
        return true;
    }
}