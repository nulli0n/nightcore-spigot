package su.nightexpress.nightcore.bridge.paper.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.scheduler.DefaultBukkitScheduler;

public class PaperScheduler extends DefaultBukkitScheduler {

    public PaperScheduler(@NonNull JavaPlugin plugin) {
        super(plugin);
    }
}
