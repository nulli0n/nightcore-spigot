package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface AdaptedScheduler {

    void cancelTasks();

    @NonNull
    AdaptedTask runTask(@NonNull Runnable runnable);

    @Nullable
    AdaptedTask runTask(@NonNull Entity entity, @NonNull Runnable runnable);

    @NonNull
    AdaptedTask runTask(@NonNull Location location, @NonNull Runnable runnable);

    @NonNull
    AdaptedTask runTask(@NonNull Chunk chunk, @NonNull Runnable runnable);

    @NonNull
    AdaptedTask runTaskAsync(@NonNull Runnable runnable);

    @NonNull
    AdaptedTask runTaskLater(@NonNull Runnable runnable, long delay);

    @NonNull
    AdaptedTask runTaskLaterAsync(@NonNull Runnable runnable, long delay);

    @NonNull
    AdaptedTask runTaskTimer(@NonNull Runnable runnable, long delay, long period);

    @NonNull
    AdaptedTask runTaskTimerAsync(@NonNull Runnable runnable, long delay, long period);
}
