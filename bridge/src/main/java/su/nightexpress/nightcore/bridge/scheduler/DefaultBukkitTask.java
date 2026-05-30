package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NonNull;

public class DefaultBukkitTask implements AdaptedTask {

    private final BukkitTask backend;

    public DefaultBukkitTask(@NonNull BukkitTask backend) {
        this.backend = backend;
    }

    @Override
    public void cancel() {
        this.backend.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.backend.isCancelled();
    }

    @Override
    @NonNull
    public Plugin getOwningPlugin() {
        return this.backend.getOwner();
    }

    @Override
    public boolean isCurrentlyRunning() {
        return Bukkit.getServer().getScheduler().isCurrentlyRunning(this.backend.getTaskId());
    }

    @Override
    public boolean isRepeatingTask() {
        return false;
    }
}
