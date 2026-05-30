package su.nightexpress.nightcore.db.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.state.Stateful;
import su.nightexpress.nightcore.manager.AbstractManager;

public abstract class AbstractBaseDataManager<V extends Stateful> extends AbstractManager<NightPlugin> {

    protected final Executor asyncExecutor;

    protected AbstractBaseDataManager(@NonNull NightPlugin plugin) {
        super(plugin);
        this.asyncExecutor = this.plugin::runTaskAsync;
    }

    protected abstract void initialize();

    public abstract @NonNull DataCache<V> getCache();

    public abstract @NonNull DataSettings getSettings();

    @Override
    protected void onLoad() {
        this.initialize();

        this.addAsyncTask(this::saveDataWithCleanup, this.getSettings().getSaveInterval());
    }

    @Override
    protected void onShutdown() {
        this.saveDataWithCleanup();
        this.getCache().clear();
    }

    public void saveDataWithCleanup() {
        Set<V> toSave = new HashSet<>();
        Set<V> toDelete = new HashSet<>();

        this.getCache().streamAll().forEach(data -> {
            if (data.isDirty()) {
                toSave.add(data);
                data.markClean();
            }
            else if (data.isRemoved()) {
                toDelete.add(data);
            }
        });

        if (!toSave.isEmpty()) {
            this.upsertData(toSave);
        }
        if (!toDelete.isEmpty()) {
            this.deleteData(toDelete);
        }

        this.getCache().clearRemoved();
        this.getCache().clearExpired();
    }

    protected void setCacheTime(@NonNull V data) {
        DataSettings settings = this.getSettings();
        int duration = settings.getCacheTimeDuration();
        if (duration <= 0) {
            data.setPermanentCache();
        }
        else {
            data.setCacheTime(duration, settings.getCacheTimeUnit());
        }
    }

    public void cacheTemporary(@NonNull V data) {
        this.setCacheTime(data);
        this.cacheData(data);
    }

    public void cachePermanently(@NonNull V data) {
        data.setPermanentCache();
        this.cacheData(data);
    }

    protected void cacheData(@NonNull V data) {
        this.getCache().put(data);
    }

    protected abstract @NonNull List<V> selectAll();

    protected abstract void upsertData(@NonNull Collection<V> datas);

    protected abstract void deleteData(@NonNull Collection<V> datas);
}
