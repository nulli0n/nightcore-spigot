package su.nightexpress.nightcore.util.scheduler;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SchedulerDetector {
    
    private static final SchedulerType DETECTED_TYPE = detectSchedulerType();
    
    @NotNull
    public static SchedulerType getSchedulerType() {
        return DETECTED_TYPE;
    }
    
    public static boolean isFolia() {
        return DETECTED_TYPE == SchedulerType.FOLIA;
    }
    
    public static boolean isBukkit() {
        return DETECTED_TYPE == SchedulerType.BUKKIT;
    }
    
    @NotNull
    private static SchedulerType detectSchedulerType() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
            return SchedulerType.FOLIA;
        } catch (ClassNotFoundException e) {
            return SchedulerType.BUKKIT;
        }
    }
}