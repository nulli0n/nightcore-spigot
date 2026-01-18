package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

public class DefaultBukkitScheduler implements AdaptedScheduler {

    private final JavaPlugin      plugin;
    private final BukkitScheduler scheduler;

    public DefaultBukkitScheduler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void cancelTasks() {
        this.scheduler.cancelTasks(this.plugin);
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTask(@NotNull Runnable runnable) {
        return new DefaultBukkitTask(this.scheduler.runTask(this.plugin, runnable));
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTask(@NotNull Entity entity, @NotNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTask(@NotNull Location location, @NotNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTask(@NotNull Chunk chunk, @NotNull Runnable runnable) {
        return this.runTask(runnable);
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTaskAsync(@NotNull Runnable runnable) {
        return new DefaultBukkitTask(this.scheduler.runTaskAsynchronously(this.plugin, runnable));
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTaskLater(@NotNull Runnable runnable, long delay) {
        return new DefaultBukkitTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTaskLaterAsync(@NotNull Runnable runnable, long delay) {
        return new DefaultBukkitTask(this.scheduler.runTaskLaterAsynchronously(this.plugin, runnable, delay));
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTaskTimer(@NotNull Runnable runnable, long delay, long period) {
        return new DefaultBukkitTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
    }

    @Override
    @NotNull
    public DefaultBukkitTask runTaskTimerAsync(@NotNull Runnable runnable, long delay, long period) {
        return new DefaultBukkitTask(this.scheduler.runTaskTimerAsynchronously(this.plugin, runnable, delay, period));
    }
}
