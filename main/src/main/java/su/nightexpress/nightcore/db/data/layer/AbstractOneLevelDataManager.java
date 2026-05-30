package su.nightexpress.nightcore.db.data.layer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.data.AbstractBaseDataManager;
import su.nightexpress.nightcore.db.state.Stateful;

public abstract class AbstractOneLevelDataManager<K, V extends Stateful> extends AbstractBaseDataManager<V> {

    protected final Function<V, K> dataKeyFunction;

    protected final OneLevelDataCache<K, V> cache;

    protected AbstractOneLevelDataManager(@NonNull NightPlugin plugin,
                                          @NonNull Function<V, K> dataKeyFunction) {
        super(plugin);
        this.dataKeyFunction = dataKeyFunction;

        this.cache = OneLevelDataCache.create(dataKeyFunction);
    }

    @Override
    public @NonNull OneLevelDataCache<K, V> getCache() {
        return this.cache;
    }

    public @NonNull CompletableFuture<Optional<V>> loadAsync(@NonNull K key) {
        return this.loadOrCreateAndCacheAsync(key, false, false);
    }

    public @NonNull CompletableFuture<Optional<V>> loadOrCreateAsync(@NonNull K key) {
        return this.loadOrCreateAndCacheAsync(key, true, false);
    }

    public @NonNull CompletableFuture<Optional<V>> loadAndCacheAsync(@NonNull K key) {
        return this.loadOrCreateAndCacheAsync(key, false, true);
    }

    public @NonNull CompletableFuture<Optional<V>> loadOrCreateAndCacheAsync(@NonNull K key) {
        return this.loadOrCreateAndCacheAsync(key, true, true);
    }

    protected @NonNull CompletableFuture<Optional<V>> loadOrCreateAndCacheAsync(@NonNull K key,
                                                                                boolean create,
                                                                                boolean needCache) {
        V cached = this.getData(key);
        if (cached != null) return CompletableFuture.completedFuture(Optional.of(cached));

        return CompletableFuture.supplyAsync(() -> {
            V data = this.selectByKey(key);
            if (data == null) {
                if (!create) {
                    return Optional.empty();
                }

                data = this.createData(key);
                data.markDirty();
            }

            if (needCache) this.cacheNewData(data);

            return Optional.of(data);
        }, this.asyncExecutor);
    }

    public @Nullable V getData(@NonNull K key) {
        V data = this.cache.get(key);
        if (data != null && data.getCachedUntil() >= 0L) {
            // Update cache time on query
            this.setCacheTime(data);
        }
        return data;
    }

    public @NonNull V getDataOrCreate(@NonNull K key) {
        return this.getDataOrCreateAndCache(key, false);
    }

    public @NonNull V getDataOrCreateAndCache(@NonNull K key) {
        return this.getDataOrCreateAndCache(key, true);
    }

    protected @NonNull V getDataOrCreateAndCache(@NonNull K key, boolean needCache) {
        V cached = this.getData(key);
        if (cached == null) {
            cached = this.createData(key);
            cached.markDirty(); // Auto-insert to the database on next save.
            if (needCache) this.cacheNewData(cached);
        }
        return cached;
    }

    public abstract void cacheNewData(@NonNull V data);

    protected abstract @NonNull V createData(@NonNull K key);

    protected abstract @Nullable V selectByKey(@NonNull K key);
}
