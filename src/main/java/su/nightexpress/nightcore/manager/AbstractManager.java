package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.wrapper.UniTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractManager<P extends NightCorePlugin> extends SimpleManager<P> {

    protected final Set<SimpleListener> listeners;
    protected final List<UniTask>       tasks;

    public AbstractManager(@NotNull P plugin) {
        super(plugin);
        this.listeners = new HashSet<>();
        this.tasks = new ArrayList<>();
    }

    @Override
    public void shutdown() {
        this.tasks.forEach(UniTask::stop);
        this.tasks.clear();
        this.listeners.forEach(SimpleListener::unregisterListeners);
        this.listeners.clear();
        super.shutdown();
    }

    protected void addListener(@NotNull SimpleListener listener) {
        if (this.listeners.add(listener)) {
            listener.registerListeners();
        }
    }

    protected void addTask(@NotNull UniTask task) {
        this.tasks.add(task);
        task.start();
    }
}
