package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AdaptedScheduler {

    void cancelTasks();

    @NotNull AdaptedTask runTask(@NotNull Runnable runnable);

    @Nullable AdaptedTask runTask(@NotNull Entity entity, @NotNull Runnable runnable);

    @NotNull AdaptedTask runTask(@NotNull Location location, @NotNull Runnable runnable);

    @NotNull AdaptedTask runTask(@NotNull Chunk chunk, @NotNull Runnable runnable);

    @NotNull AdaptedTask runTaskAsync(@NotNull Runnable runnable);

    @NotNull AdaptedTask runTaskLater(@NotNull Runnable runnable, long delay);

    @NotNull AdaptedTask runTaskLaterAsync(@NotNull Runnable runnable, long delay);

    @NotNull AdaptedTask runTaskTimer(@NotNull Runnable runnable, long delay, long period);

    @NotNull AdaptedTask runTaskTimerAsync(@NotNull Runnable runnable, long delay, long period);
}
