package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.TimeUtil;

/**
 * 新一代任务类，完全支持Folia和Bukkit
 */
public class NightTaskV2 {
    
    private final Plugin plugin;
    private final NightScheduler scheduler;
    private final Runnable runnable;
    private final TaskType taskType;
    private final Location location;
    private final Entity entity;
    private final long delay;
    private final long interval;
    
    private boolean running = false;
    
    private NightTaskV2(@NotNull Builder builder) {
        this.plugin = builder.plugin;
        this.scheduler = SchedulerFactory.createScheduler(builder.plugin);
        this.runnable = builder.runnable;
        this.taskType = builder.taskType;
        this.location = builder.location;
        this.entity = builder.entity;
        this.delay = builder.delay;
        this.interval = builder.interval;
    }
    
    /**
     * 启动任务
     */
    public void start() {
        if (running) return;
        
        switch (taskType) {
            case SYNC:
                if (interval > 0) {
                    scheduler.runTaskTimer(runnable, delay, interval);
                } else if (delay > 0) {
                    scheduler.runTaskLater(runnable, delay);
                } else {
                    scheduler.runTask(runnable);
                }
                break;
                
            case ASYNC:
                if (interval > 0) {
                    scheduler.runTaskTimerAsync(runnable, delay, interval);
                } else if (delay > 0) {
                    scheduler.runTaskLaterAsync(runnable, delay);
                } else {
                    scheduler.runTaskAsync(runnable);
                }
                break;
                
            case LOCATION:
                if (location != null) {
                    if (interval > 0) {
                        scheduler.runTaskTimerAtLocation(runnable, location, delay, interval);
                    } else if (delay > 0) {
                        scheduler.runTaskLaterAtLocation(runnable, location, delay);
                    } else {
                        scheduler.runTaskAtLocation(runnable, location);
                    }
                }
                break;
                
            case ENTITY:
                if (entity != null) {
                    if (interval > 0) {
                        scheduler.runTaskTimerOnEntity(runnable, entity, delay, interval);
                    } else if (delay > 0) {
                        scheduler.runTaskLaterOnEntity(runnable, entity, delay);
                    } else {
                        scheduler.runTaskOnEntity(runnable, entity);
                    }
                }
                break;
        }
        
        running = true;
    }
    
    /**
     * 停止任务
     */
    public void stop() {
        if (!running) return;
        scheduler.cancelAllTasks();
        running = false;
    }
    
    /**
     * 重启任务
     */
    public void restart() {
        stop();
        start();
    }
    
    /**
     * 检查任务是否正在运行
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * 获取调度器类型
     */
    @NotNull
    public SchedulerType getSchedulerType() {
        return scheduler.getSchedulerType();
    }
    
    /**
     * 任务类型枚举
     */
    public enum TaskType {
        SYNC,       // 同步任务
        ASYNC,      // 异步任务
        LOCATION,   // 位置相关任务（Folia特有）
        ENTITY      // 实体相关任务（Folia特有）
    }
    
    /**
     * 构建器类
     */
    public static class Builder {
        private final Plugin plugin;
        private final Runnable runnable;
        private TaskType taskType = TaskType.SYNC;
        private Location location = null;
        private Entity entity = null;
        private long delay = 0;
        private long interval = 0;
        
        public Builder(@NotNull Plugin plugin, @NotNull Runnable runnable) {
            this.plugin = plugin;
            this.runnable = runnable;
        }
        
        @NotNull
        public Builder async() {
            this.taskType = TaskType.ASYNC;
            return this;
        }
        
        @NotNull
        public Builder atLocation(@NotNull Location location) {
            this.taskType = TaskType.LOCATION;
            this.location = location;
            return this;
        }
        
        @NotNull
        public Builder onEntity(@NotNull Entity entity) {
            this.taskType = TaskType.ENTITY;
            this.entity = entity;
            return this;
        }
        
        @NotNull
        public Builder delay(long delay) {
            this.delay = delay;
            return this;
        }
        
        @NotNull
        public Builder delaySeconds(int seconds) {
            this.delay = TimeUtil.secondsToTicks(seconds);
            return this;
        }
        
        @NotNull
        public Builder interval(long interval) {
            this.interval = interval;
            return this;
        }
        
        @NotNull
        public Builder intervalSeconds(int seconds) {
            this.interval = TimeUtil.secondsToTicks(seconds);
            return this;
        }
        
        @NotNull
        public NightTaskV2 build() {
            return new NightTaskV2(this);
        }
        
        @NotNull
        public NightTaskV2 buildAndStart() {
            NightTaskV2 task = build();
            task.start();
            return task;
        }
    }
    
    /**
     * 创建简单的同步任务
     */
    @NotNull
    public static NightTaskV2 createSync(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        return new Builder(plugin, runnable).build();
    }
    
    /**
     * 创建简单的异步任务
     */
    @NotNull
    public static NightTaskV2 createAsync(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        return new Builder(plugin, runnable).async().build();
    }
    
    /**
     * 创建定时同步任务
     */
    @NotNull
    public static NightTaskV2 createTimer(@NotNull Plugin plugin, @NotNull Runnable runnable, long interval) {
        return new Builder(plugin, runnable).interval(interval).build();
    }
    
    /**
     * 创建定时异步任务
     */
    @NotNull
    public static NightTaskV2 createAsyncTimer(@NotNull Plugin plugin, @NotNull Runnable runnable, long interval) {
        return new Builder(plugin, runnable).async().interval(interval).build();
    }
    
    /**
     * 创建位置相关的任务（Folia特有）
     */
    @NotNull
    public static NightTaskV2 createAtLocation(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull Location location) {
        return new Builder(plugin, runnable).atLocation(location).build();
    }
    
    /**
     * 创建实体相关的任务（Folia特有）
     */
    @NotNull
    public static NightTaskV2 createOnEntity(@NotNull Plugin plugin, @NotNull Runnable runnable, @NotNull Entity entity) {
        return new Builder(plugin, runnable).onEntity(entity).build();
    }
}