package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jspecify.annotations.NonNull;

public class DefaultBukkitScheduler implements AdaptedScheduler {

    private final JavaPlugin      plugin;
    private final BukkitScheduler scheduler;

    public DefaultBukkitScheduler(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void cancelTasks() {
        this.scheduler.cancelTasks(this.plugin);
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTask(@NonNull Runnable runnable) {
        return new DefaultBukkitTask(this.scheduler.runTask(this.plugin, runnable));
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTask(@NonNull Entity entity, @NonNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTask(@NonNull Location location, @NonNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTask(@NonNull Chunk chunk, @NonNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTaskAsync(@NonNull Runnable runnable) {
        return new DefaultBukkitTask(this.scheduler.runTaskAsynchronously(this.plugin, runnable));
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTaskLater(@NonNull Runnable runnable, long delay) {
        return new DefaultBukkitTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTaskLaterAsync(@NonNull Runnable runnable, long delay) {
        return new DefaultBukkitTask(this.scheduler.runTaskLaterAsynchronously(this.plugin, runnable, delay));
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTaskTimer(@NonNull Runnable runnable, long delay, long period) {
        return new DefaultBukkitTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
    }

    @Override
    @NonNull
    public DefaultBukkitTask runTaskTimerAsync(@NonNull Runnable runnable, long delay, long period) {
        return new DefaultBukkitTask(this.scheduler.runTaskTimerAsynchronously(this.plugin, runnable, delay, period));
    }
}
