package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 统一的调度器接口，支持Folia和Bukkit
 */
public interface NightScheduler {
    
    /**
     * 运行同步任务
     */
    void runTask(@NotNull Runnable runnable);
    
    /**
     * 运行异步任务
     */
    void runTaskAsync(@NotNull Runnable runnable);
    
    /**
     * 延迟运行同步任务
     */
    void runTaskLater(@NotNull Runnable runnable, long delay);
    
    /**
     * 延迟运行异步任务
     */
    void runTaskLaterAsync(@NotNull Runnable runnable, long delay);
    
    /**
     * 定时运行同步任务
     */
    void runTaskTimer(@NotNull Runnable runnable, long delay, long interval);
    
    /**
     * 定时运行异步任务
     */
    void runTaskTimerAsync(@NotNull Runnable runnable, long delay, long interval);
    
    /**
     * 在指定位置运行区域任务（Folia特有）
     */
    void runTaskAtLocation(@NotNull Runnable runnable, @NotNull Location location);
    
    /**
     * 延迟在指定位置运行区域任务（Folia特有）
     */
    void runTaskLaterAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay);
    
    /**
     * 定时在指定位置运行区域任务（Folia特有）
     */
    void runTaskTimerAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay, long interval);
    
    /**
     * 在实体上运行任务（Folia特有）
     */
    void runTaskOnEntity(@NotNull Runnable runnable, @NotNull Entity entity);
    
    /**
     * 延迟在实体上运行任务（Folia特有）
     */
    void runTaskLaterOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay);
    
    /**
     * 定时在实体上运行任务（Folia特有）
     */
    void runTaskTimerOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay, long interval);
    
    /**
     * 取消所有任务
     */
    void cancelAllTasks();
    
    /**
     * 获取调度器类型
     */
    @NotNull
    SchedulerType getSchedulerType();
}