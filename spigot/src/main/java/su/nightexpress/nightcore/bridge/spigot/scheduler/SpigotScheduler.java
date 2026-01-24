package su.nightexpress.nightcore.bridge.spigot.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.scheduler.DefaultBukkitScheduler;

public class SpigotScheduler extends DefaultBukkitScheduler {

    public SpigotScheduler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }
}
