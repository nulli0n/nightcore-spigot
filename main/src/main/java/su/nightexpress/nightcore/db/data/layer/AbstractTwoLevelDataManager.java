package su.nightexpress.nightcore.db.data.layer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.db.data.AbstractBaseDataManager;
import su.nightexpress.nightcore.db.state.Stateful;

public abstract class AbstractTwoLevelDataManager<P, K, V extends Stateful> extends AbstractBaseDataManager<V> {

    protected final Function<V, P> parentKeyFunction;
    protected final Function<V, K> dataKeyFunction;

    protected final TwoLevelDataCache<P, K, V> cache;

    protected AbstractTwoLevelDataManager(@NonNull NightPlugin plugin,
                                          @NonNull Function<V, P> parentKeyFunction,
                                          @NonNull Function<V, K> dataKeyFunction) {
        super(plugin);
        this.parentKeyFunction = parentKeyFunction;
        this.dataKeyFunction = dataKeyFunction;

        this.cache = TwoLevelDataCache.create(parentKeyFunction, dataKeyFunction);
    }

    @Override
    public @NonNull TwoLevelDataCache<P, K, V> getCache() {
        return this.cache;
    }

    public abstract @NonNull P getGlobalKey();

    public void loadGlobalAndActivate() {
        this.selectByKey(this.getGlobalKey()).forEach(this::cachePermanently);
        this.activate(this.getGlobalKey());
    }

    public void activate(@NonNull P parentKey) {
        this.cache.activateHolder(parentKey);
        this.cache.getAllByOwner(parentKey).forEach(Stateful::setPermanentCache); // Set cache permanent for active holders.
    }

    public void deactivate(@NonNull P parentKey) {
        this.cache.deactivateHolder(parentKey);
        this.cache.getAllByOwner(parentKey).forEach(this::setCacheTime); // Make cache expirable.
    }

    public boolean isActive(@NonNull P parentKey) {
        return this.cache.isHolderActive(parentKey);
    }

    public boolean isActiveWithGlobal(@NonNull P parentKey) {
        return this.isActive(this.getGlobalKey()) && this.isActive(parentKey);
    }

    public @NonNull CompletableFuture<Optional<V>> loadAsync(@NonNull P parentKey,
                                                             @NonNull K dataKey) {
        return this.loadOrCreateAndCacheAsync(parentKey, dataKey, false, false);
    }

    public @NonNull CompletableFuture<Optional<V>> loadOrCreateAsync(@NonNull P parentKey,
                                                                     @NonNull K dataKey) {
        return this.loadOrCreateAndCacheAsync(parentKey, dataKey, true, false);
    }

    public @NonNull CompletableFuture<Optional<V>> loadAndCacheAsync(@NonNull P parentKey,
                                                                     @NonNull K dataKey) {
        return this.loadOrCreateAndCacheAsync(parentKey, dataKey, false, true);
    }

    public @NonNull CompletableFuture<Optional<V>> loadOrCreateAndCacheAsync(@NonNull P parentKey,
                                                                             @NonNull K dataKey) {
        return this.loadOrCreateAndCacheAsync(parentKey, dataKey, true, true);
    }

    protected @NonNull CompletableFuture<Optional<V>> loadOrCreateAndCacheAsync(@NonNull P parentKey,
                                                                                @NonNull K dataKey,
                                                                                boolean create,
                                                                                boolean needCache) {
        V cached = this.getData(parentKey, dataKey);
        if (cached != null) return CompletableFuture.completedFuture(Optional.of(cached));

        return CompletableFuture.supplyAsync(() -> {
            V data = this.selectByKeyAndId(parentKey, dataKey);
            if (data == null) {
                if (!create) {
                    return Optional.empty();
                }

                data = this.createData(parentKey, dataKey);
                data.markDirty();
            }

            if (needCache) this.cacheNewData(data);

            return Optional.of(data);
        }, this.asyncExecutor);
    }

    public void cacheNewData(@NonNull V data) {
        P parentKey = this.parentKeyFunction.apply(data);

        if (this.isActive(parentKey)) {
            this.cachePermanently(data);
        }
        else {
            this.cacheTemporary(data);
        }
    }

    public @Nullable V getGlobalData(@NonNull K dataKey) {
        return this.getData(this.getGlobalKey(), dataKey);
    }

    public @NonNull V getGlobalDataOrCreate(@NonNull K dataKey) {
        return this.getDataOrCreate(this.getGlobalKey(), dataKey);
    }

    public @NonNull V getGlobalDataOrCreateAndCache(@NonNull K dataKey) {
        return this.getDataOrCreateAndCache(this.getGlobalKey(), dataKey);
    }

    public @Nullable V getData(@NonNull P parentKey, @NonNull K dataKey) {
        V data = this.cache.get(parentKey, dataKey);
        if (data != null && data.getCachedUntil() >= 0L) {
            // Update cache time on query
            this.setCacheTime(data);
        }
        return data;
    }

    public @NonNull V getDataOrCreate(@NonNull P parentKey, @NonNull K dataKey) {
        return this.getDataOrCreateAndCache(parentKey, dataKey, false);
    }

    public @NonNull V getDataOrCreateAndCache(@NonNull P parentKey, @NonNull K dataKey) {
        return this.getDataOrCreateAndCache(parentKey, dataKey, true);
    }

    protected @NonNull V getDataOrCreateAndCache(@NonNull P parentKey,
                                                 @NonNull K dataKey,
                                                 boolean needCache) {
        V cached = this.getData(parentKey, dataKey);
        if (cached == null) {
            cached = this.createData(parentKey, dataKey);
            cached.markDirty(); // Auto-insert to the database on next save.
            if (needCache) this.cacheNewData(cached);
        }
        return cached;
    }

    protected abstract @NonNull V createData(@NonNull P parentKey, @NonNull K dataKey);

    protected abstract @NonNull List<V> selectByKey(@NonNull P parentKey);

    protected abstract @Nullable V selectByKeyAndId(@NonNull P parentKey, @NonNull K dataKey);
}