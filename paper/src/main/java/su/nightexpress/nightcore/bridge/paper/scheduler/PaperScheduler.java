package su.nightexpress.nightcore.bridge.paper.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.scheduler.DefaultBukkitScheduler;

public class PaperScheduler extends DefaultBukkitScheduler {

    public PaperScheduler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }
}
