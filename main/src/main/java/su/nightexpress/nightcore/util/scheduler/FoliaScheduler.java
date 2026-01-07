package su.nightexpress.nightcore.util.scheduler;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Folia调度器实现
 */
public class FoliaScheduler implements NightScheduler {
    
    private final Plugin plugin;
    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;
    
    public FoliaScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.globalScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }
    
    @Override
    public void runTask(@NotNull Runnable runnable) {
        globalScheduler.run(plugin, task -> runnable.run());
    }
    
    @Override
    public void runTaskAsync(@NotNull Runnable runnable) {
        asyncScheduler.runNow(plugin, task -> runnable.run());
    }
    
    @Override
    public void runTaskLater(@NotNull Runnable runnable, long delay) {
        globalScheduler.runDelayed(plugin, task -> runnable.run(), delay);
    }
    
    @Override
    public void runTaskLaterAsync(@NotNull Runnable runnable, long delay) {
        asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void runTaskTimer(@NotNull Runnable runnable, long delay, long interval) {
        // 确保delay和interval至少为1 tick
        long actualDelay = Math.max(1, delay);
        long actualInterval = Math.max(1, interval);
        globalScheduler.runAtFixedRate(plugin, task -> runnable.run(), actualDelay, actualInterval);
    }
    
    @Override
    public void runTaskTimerAsync(@NotNull Runnable runnable, long delay, long interval) {
        // 确保delay和interval至少为1 tick，并转换为毫秒
        long actualDelay = Math.max(1, delay) * 50L;
        long actualInterval = Math.max(1, interval) * 50L;
        asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), 
            actualDelay, actualInterval, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void runTaskAtLocation(@NotNull Runnable runnable, @NotNull Location location) {
        RegionScheduler regionScheduler = plugin.getServer().getRegionScheduler();
        regionScheduler.run(plugin, location, task -> runnable.run());
    }
    
    @Override
    public void runTaskLaterAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay) {
        RegionScheduler regionScheduler = plugin.getServer().getRegionScheduler();
        regionScheduler.runDelayed(plugin, location, task -> runnable.run(), delay);
    }
    
    @Override
    public void runTaskTimerAtLocation(@NotNull Runnable runnable, @NotNull Location location, long delay, long interval) {
        RegionScheduler regionScheduler = plugin.getServer().getRegionScheduler();
        regionScheduler.runAtFixedRate(plugin, location, task -> runnable.run(), delay, interval);
    }
    
    @Override
    public void runTaskOnEntity(@NotNull Runnable runnable, @NotNull Entity entity) {
        EntityScheduler entityScheduler = entity.getScheduler();
        entityScheduler.run(plugin, task -> runnable.run(), null);
    }
    
    @Override
    public void runTaskLaterOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay) {
        EntityScheduler entityScheduler = entity.getScheduler();
        entityScheduler.runDelayed(plugin, task -> runnable.run(), null, delay);
    }
    
    @Override
    public void runTaskTimerOnEntity(@NotNull Runnable runnable, @NotNull Entity entity, long delay, long interval) {
        EntityScheduler entityScheduler = entity.getScheduler();
        entityScheduler.runAtFixedRate(plugin, task -> runnable.run(), null, delay, interval);
    }
    
    @Override
    public void cancelAllTasks() {
        globalScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }
    
    @Override
    @NotNull
    public SchedulerType getSchedulerType() {
        return SchedulerType.FOLIA;
    }
}