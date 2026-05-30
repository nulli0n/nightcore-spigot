package su.nightexpress.nightcore.bridge.paper.scheduler;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedTask;

public class FoliaScheduledTask implements AdaptedTask {

    private final ScheduledTask backend;

    public FoliaScheduledTask(@NonNull ScheduledTask backend) {
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
        return this.backend.getOwningPlugin();
    }

    @Override
    public boolean isCurrentlyRunning() {
        ScheduledTask.ExecutionState state = this.backend.getExecutionState();
        return state == ScheduledTask.ExecutionState.RUNNING || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    @Override
    public boolean isRepeatingTask() {
        return this.backend.isRepeatingTask();
    }
}
