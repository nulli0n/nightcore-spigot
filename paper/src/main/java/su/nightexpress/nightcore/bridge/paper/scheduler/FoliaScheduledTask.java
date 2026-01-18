package su.nightexpress.nightcore.bridge.paper.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedTask;

public class FoliaScheduledTask implements AdaptedTask {

    private final ScheduledTask backend;

    public FoliaScheduledTask(@NotNull ScheduledTask backend) {
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
