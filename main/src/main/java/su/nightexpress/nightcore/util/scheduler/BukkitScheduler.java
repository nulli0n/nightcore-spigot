package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * 传统Bukkit调度器实现
 */
public class BukkitScheduler implements NightScheduler {
    
    private final Plugin plugin;
    
    public BukkitScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void runTask(@NotNull Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    @Override
    public void runTaskAsync(@NotNull Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }
    
    @Override
    public void runTaskLater(@NotNull Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }
    
    @Override
    public void runTaskLaterAsync(@NotNull Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }
    
    @Override
    public void runTaskTimer(@NotNull Runnable runnable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }
    
    @Override
    public void runTaskTimerAsync(@NotNull Runnable runnable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }
    
    @Override
    public void runTaskAtLocation(@NotNull Runnable runnable, @NotNull Location location) {
        // 在Bukkit中，位置相关的任务使用全局调度器
        runTask(runnable);
    }
    
    @Override
    public void runTaskLaterAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay) {
        // 在Bukkit中，位置相关的任务使用全局调度器
        runTaskLater(runnable, delay);
    }
    
    @Override
    public void runTaskTimerAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay, long interval) {
        // 在Bukkit中，位置相关的任务使用全局调度器
        runTaskTimer(runnable, delay, interval);
    }
    
    @Override
    public void runTaskOnEntity(@NotNull Runnable runnable, @NotNull Entity entity) {
        // 在Bukkit中，实体相关的任务使用全局调度器
        runTask(runnable);
    }
    
    @Override
    public void runTaskLaterOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay) {
        // 在Bukkit中，实体相关的任务使用全局调度器
        runTaskLater(runnable, delay);
    }
    
    @Override
    public void runTaskTimerOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay, long interval) {
        // 在Bukkit中，实体相关的任务使用全局调度器
        runTaskTimer(runnable, delay, interval);
    }
    
    @Override
    public void cancelAllTasks() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }
    
    @Override
    @NotNull
    public SchedulerType getSchedulerType() {
        return SchedulerType.BUKKIT;
    }
}