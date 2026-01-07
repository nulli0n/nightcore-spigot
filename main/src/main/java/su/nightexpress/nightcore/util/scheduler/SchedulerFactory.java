package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * 调度器工厂类
 */
public class SchedulerFactory {
    
    /**
     * 创建适合当前服务器的调度器
     */
    @NotNull
    public static NightScheduler createScheduler(@NotNull Plugin plugin) {
        if (SchedulerDetector.isFolia()) {
            return new FoliaScheduler(plugin);
        } else {
            return new BukkitScheduler(plugin);
        }
    }
    
    /**
     * 获取当前服务器的调度器类型
     */
    @NotNull
    public static SchedulerType getCurrentSchedulerType() {
        return SchedulerDetector.getSchedulerType();
    }
}