package su.nightexpress.nightcore.bridge.scheduler;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public interface AdaptedTask {

    void cancel();

    boolean isCancelled();

    @NonNull
    Plugin getOwningPlugin();

    boolean isCurrentlyRunning();

    boolean isRepeatingTask();
}
