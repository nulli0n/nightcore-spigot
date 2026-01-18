package su.nightexpress.nightcore.bridge.paper.scheduler;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements AdaptedScheduler {

    private final JavaPlugin            plugin;
    private final RegionScheduler       regionScheduler;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final AsyncScheduler        asyncScheduler;

    public FoliaScheduler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.regionScheduler = plugin.getServer().getRegionScheduler();
        this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    @Override
    public void cancelTasks() {
        this.globalRegionScheduler.cancelTasks(this.plugin);
        this.asyncScheduler.cancelTasks(this.plugin);
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTask(@NotNull Runnable runnable) {
        return new FoliaScheduledTask(this.globalRegionScheduler.run(this.plugin, task -> runnable.run()));
    }

    @Override
    @Nullable
    public FoliaScheduledTask runTask(@NotNull Entity entity, @NotNull Runnable runnable) {
        ScheduledTask scheduledTask = entity.getScheduler().run(this.plugin, task -> runnable.run(), null);
        return scheduledTask == null ? null : new FoliaScheduledTask(scheduledTask);
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTask(@NotNull Location location, @NotNull Runnable runnable) {
        return new FoliaScheduledTask(this.regionScheduler.run(this.plugin, location, task -> runnable.run()));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTask(@NotNull Chunk chunk, @NotNull Runnable runnable) {
        return new FoliaScheduledTask(this.regionScheduler.run(this.plugin, chunk.getWorld(), chunk.getX(), chunk.getZ(), task -> runnable.run()));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTaskAsync(@NotNull Runnable runnable) {
        return new FoliaScheduledTask(this.asyncScheduler.runNow(this.plugin, task -> runnable.run()));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTaskLater(@NotNull Runnable runnable, long delay) {
        return new FoliaScheduledTask(this.globalRegionScheduler.runDelayed(this.plugin, task -> runnable.run(), delay));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTaskLaterAsync(@NotNull Runnable runnable, long delay) {
        long delayMs = ticksToMillis(delay);

        return new FoliaScheduledTask(this.asyncScheduler.runDelayed(this.plugin, task -> runnable.run(), delayMs, TimeUnit.MILLISECONDS));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTaskTimer(@NotNull Runnable runnable, long delay, long period) {
        return new FoliaScheduledTask(this.globalRegionScheduler.runAtFixedRate(this.plugin, task -> runnable.run(), delay, period));
    }

    @Override
    @NotNull
    public FoliaScheduledTask runTaskTimerAsync(@NotNull Runnable runnable, long delay, long period) {
        long delayMs = ticksToMillis(delay);
        long periodMs = ticksToMillis(period);

        return new FoliaScheduledTask(this.asyncScheduler.runAtFixedRate(this.plugin, task -> runnable.run(), delayMs, periodMs, TimeUnit.MILLISECONDS));
    }

    private static long ticksToMillis(long ticks) {
        return ticks * 50L;
    }
}
