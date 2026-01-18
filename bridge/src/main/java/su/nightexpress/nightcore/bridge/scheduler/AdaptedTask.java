package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface AdaptedTask {

    void cancel();

    boolean isCancelled();

    @NotNull Plugin getOwningPlugin();

    boolean isCurrentlyRunning();

    boolean isRepeatingTask();
}
