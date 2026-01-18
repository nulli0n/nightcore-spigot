package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class DefaultBukkitTask implements AdaptedTask {

    private final BukkitTask backend;

    public DefaultBukkitTask(@NotNull BukkitTask backend) {
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
    @NotNull
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
