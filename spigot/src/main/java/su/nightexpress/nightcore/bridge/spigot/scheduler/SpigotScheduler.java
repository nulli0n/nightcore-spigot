package su.nightexpress.nightcore.bridge.spigot.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.scheduler.DefaultBukkitScheduler;

public class SpigotScheduler extends DefaultBukkitScheduler {

    public SpigotScheduler(@NonNull JavaPlugin plugin) {
        super(plugin);
    }
}
