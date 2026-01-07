package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * 调度器使用示例
 */
public class SchedulerExample {
    
    private final Plugin plugin;
    
    public SchedulerExample(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 示例1：基本任务使用
     */
    public void exampleBasicTasks() {
        // 使用新的调度器系统
        NightScheduler scheduler = SchedulerFactory.createScheduler(plugin);
        
        // 同步任务
        scheduler.runTask(() -> {
            plugin.getLogger().info("这是一个同步任务");
        });
        
        // 异步任务
        scheduler.runTaskAsync(() -> {
            plugin.getLogger().info("这是一个异步任务");
        });
        
        // 延迟任务
        scheduler.runTaskLater(() -> {
            plugin.getLogger().info("这是一个延迟20tick的同步任务");
        }, 20);
        
        // 定时任务
        scheduler.runTaskTimer(() -> {
            plugin.getLogger().info("这是一个每100tick执行一次的定时任务");
        }, 0, 100);
    }
    
    /**
     * 示例2：使用NightTaskV2（推荐方式）
     */
    public void exampleNightTaskV2() {
        // 创建并启动一个同步任务
        NightTaskV2 task1 = NightTaskV2.createSync(plugin, () -> {
            plugin.getLogger().info("同步任务执行");
        });
        task1.start();
        
        // 创建并启动一个异步定时任务
        NightTaskV2 task2 = NightTaskV2.createAsyncTimer(plugin, () -> {
            plugin.getLogger().info("异步定时任务执行");
        }, 100);
        task2.start();
        
        // 使用构建器模式创建复杂任务
        NightTaskV2 task3 = new NightTaskV2.Builder(plugin, () -> {
            plugin.getLogger().info("使用构建器创建的任务");
        })
            .async()
            .delaySeconds(5)  // 延迟5秒
            .intervalSeconds(10)  // 每10秒执行一次
            .buildAndStart();  // 构建并立即启动
    }
    
    /**
     * 示例3：Folia特有功能（位置和实体相关任务）
     */
    public void exampleFoliaSpecific(@NotNull Player player, @NotNull Location location) {
        NightScheduler scheduler = SchedulerFactory.createScheduler(plugin);
        
        // 位置相关任务（只在Folia服务器上有效）
        if (SchedulerDetector.isFolia()) {
            scheduler.runTaskAtLocation(() -> {
                plugin.getLogger().info("在指定位置执行的任务");
            }, location);
            
            scheduler.runTaskTimerAtLocation(() -> {
                plugin.getLogger().info("在指定位置执行的定时任务");
            }, location, 0, 100);
        }
        
        // 实体相关任务（只在Folia服务器上有效）
        if (SchedulerDetector.isFolia()) {
            scheduler.runTaskOnEntity(() -> {
                plugin.getLogger().info("在实体上执行的任务");
            }, player);
            
            scheduler.runTaskTimerOnEntity(() -> {
                plugin.getLogger().info("在实体上执行的定时任务");
            }, player, 0, 100);
        }
    }
    
    /**
     * 示例4：使用NightCorePlugin接口的便捷方法
     */
    public void examplePluginInterface(@NotNull su.nightexpress.nightcore.NightCorePlugin nightPlugin) {
        // 使用便捷方法创建任务
        NightTaskV2 task1 = nightPlugin.createNightTask(() -> {
            nightPlugin.info("使用便捷方法创建的同步任务");
        });
        task1.start();
        
        NightTaskV2 task2 = nightPlugin.createNightAsyncTimer(() -> {
            nightPlugin.info("使用便捷方法创建的异步定时任务");
        }, 200);
        task2.start();
        
        // 检查服务器类型
        if (nightPlugin.isFolia()) {
            nightPlugin.info("当前服务器使用Folia调度器");
        } else if (nightPlugin.isBukkit()) {
            nightPlugin.info("当前服务器使用传统Bukkit调度器");
        }
    }
    
    /**
     * 示例5：任务管理和控制
     */
    public void exampleTaskManagement() {
        NightTaskV2 task = NightTaskV2.createAsyncTimer(plugin, () -> {
            plugin.getLogger().info("定时任务执行中...");
        }, 50);
        
        // 启动任务
        task.start();
        
        // 检查任务状态
        if (task.isRunning()) {
            plugin.getLogger().info("任务正在运行");
        }
        
        // 停止任务
        task.stop();
        
        // 重启任务
        task.restart();
        
        // 获取调度器类型
        plugin.getLogger().info("调度器类型: " + task.getSchedulerType());
    }
}