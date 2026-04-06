package su.nightexpress.nightcore.manager;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.util.bukkit.NightTask;
import su.nightexpress.nightcore.util.wrapper.UniTask;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractManager<P extends NightCorePlugin> extends SimpleManager<P> {

    protected final Set<SimpeListener> listeners;
    @Deprecated protected final Set<Menu> menus;
    @Deprecated protected final List<UniTask>   tasks;
    protected final List<NightTask> taskList;

    public AbstractManager(@NonNull P plugin) {
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

    protected void addListener(@NonNull SimpeListener listener) {
        if (this.listeners.add(listener)) {
            listener.registerListeners();
        }
    }

    @NonNull
    @Deprecated
    protected <T extends Menu> T addMenu(@NonNull T menu) {
        this.menus.add(menu);
        return menu;
    }

    @NonNull
    @Deprecated
    protected <T extends Menu & ConfigBased> T addMenu(@NonNull T menu, @NonNull String path, @NonNull String file) {
        this.menus.add(menu);
        menu.load(FileConfig.loadOrExtract(this.plugin, path, file));
        return menu;
    }

    @NonNull
    protected <T extends su.nightexpress.nightcore.ui.inventory.Menu> T initMenu(@NonNull T menu) {
        menu.load();
        return menu;
    }

    @NonNull
    protected <T extends su.nightexpress.nightcore.ui.inventory.Menu> T initMenu(@NonNull T menu, @NonNull Path path) {
        menu.load(path);
        return menu;
    }
    
    @NonNull
    protected <T extends su.nightexpress.nightcore.ui.inventory.Menu> T initMenu(@NonNull T menu, @NonNull FileConfig config) {
        menu.load(config);
        return menu;
    }

    protected void addTask(@NonNull Runnable runnable, int interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addTask(@NonNull Runnable runnable, long interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addAsyncTask(@NonNull Runnable runnable, int interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addAsyncTask(@NonNull Runnable runnable, long interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    @Deprecated
    protected void addTask(@NonNull UniTask task) {
        this.tasks.add(task);
        task.start();
    }

    protected void addTask(@NonNull NightTask task) {
        if (task.isValid()) {
            this.taskList.add(task);
        }
    }
}
