package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.util.bukkit.NightTask;
import su.nightexpress.nightcore.util.wrapper.UniTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractManager<P extends NightCorePlugin> extends SimpleManager<P> {

    protected final Set<SimpeListener> listeners;
    protected final Set<Menu> menus;
    protected final List<UniTask>   tasks;
    protected final List<NightTask> taskList;

    public AbstractManager(@NotNull P plugin) {
        super(plugin);
        this.listeners = new HashSet<>();
        this.menus = new HashSet<>();
        this.tasks = new ArrayList<>();
        this.taskList = new ArrayList<>();
    }

    @Override
    public void shutdown() {
        this.tasks.forEach(UniTask::stop);
        this.tasks.clear();
        this.taskList.forEach(NightTask::stop);
        this.taskList.clear();
        this.menus.forEach(Menu::clear);
        this.menus.clear();
        this.listeners.forEach(SimpeListener::unregisterListeners);
        this.listeners.clear();
        super.shutdown();
    }

    protected void addListener(@NotNull SimpeListener listener) {
        if (this.listeners.add(listener)) {
            listener.registerListeners();
        }
    }

    @NotNull
    protected <T extends Menu> T addMenu(@NotNull T menu) {
        this.menus.add(menu);
        return menu;
    }

    protected void addTask(@NotNull Runnable runnable, int interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addTask(@NotNull Runnable runnable, long interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addAsyncTask(@NotNull Runnable runnable, int interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addAsyncTask(@NotNull Runnable runnable, long interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    @Deprecated
    protected void addTask(@NotNull UniTask task) {
        this.tasks.add(task);
        task.start();
    }

    protected void addTask(@NotNull NightTask task) {
        if (task.isValid()) {
            this.taskList.add(task);
        }
    }
}
