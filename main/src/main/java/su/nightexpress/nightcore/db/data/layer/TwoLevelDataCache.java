package su.nightexpress.nightcore.db.data.layer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.db.data.DataCache;
import su.nightexpress.nightcore.db.state.Stateful;

public class TwoLevelDataCache<P, K, V extends Stateful> implements DataCache<V> {

    private final Function<V, P> parentKeyFunction;
    private final Function<V, K> dataKeyFunction;

    private final Map<P, Map<K, V>> ownedCacheMap;
    private final Set<P>            activeHolders;

    public TwoLevelDataCache(@NonNull Function<V, P> parentKeyFunction, @NonNull Function<V, K> dataKeyFunction) {
        this.parentKeyFunction = parentKeyFunction;
        this.dataKeyFunction = dataKeyFunction;

        this.ownedCacheMap = new ConcurrentHashMap<>();
        this.activeHolders = ConcurrentHashMap.newKeySet();
    }

    public static <P, K, V extends Stateful> @NonNull TwoLevelDataCache<P, K, V> create(@NonNull Function<V, P> parentKeyFunction,
                                                                                        @NonNull Function<V, K> dataKeyFunction) {
        return new TwoLevelDataCache<>(parentKeyFunction, dataKeyFunction);
    }

    public boolean isHolderActive(@NonNull P parentKey) {
        return this.activeHolders.contains(parentKey);
    }

    public boolean contains(@NonNull V data) {
        P parentKey = this.parentKeyFunction.apply(data);
        K dataKey = this.dataKeyFunction.apply(data);

        return this.getOwnedCache(parentKey).containsKey(dataKey);
    }

    public void clear() {
        this.ownedCacheMap.clear();
        this.activeHolders.clear();
    }

    public void clearRemoved() {
        this.removeIf(Stateful::isRemoved);
    }

    public void clearExpired() {
        this.removeIf(Stateful::isCacheExpired);
    }

    public void clearOwned(@NonNull P parentKey) {
        this.ownedCacheMap.remove(parentKey);
    }

    public void removeIf(@NonNull Predicate<V> predicate) {
        this.ownedCacheMap.values().removeIf(map -> {
            map.values().removeIf(predicate);
            return map.isEmpty();
        });
    }

    public void activateHolder(@NonNull P parentKey) {
        this.activeHolders.add(parentKey);
    }

    public void deactivateHolder(@NonNull P parentKey) {
        this.activeHolders.remove(parentKey);
    }

    @NonNull
    public Set<V> getAll() {
        return this.streamAll().collect(Collectors.toCollection(HashSet::new));
    }

    @NonNull
    public Stream<V> streamAll() {
        return this.ownedCacheMap.values().stream()
            .flatMap(map -> map.values().stream());
    }

    @NonNull
    public Set<V> getAllByOwner(@NonNull P parentKey) {
        return Set.copyOf(this.ownedCacheMap.getOrDefault(parentKey, Collections.emptyMap()).values());
    }

    private @NonNull Map<K, V> getOwnedCache(@NonNull P parentKey) {
        return this.ownedCacheMap.getOrDefault(parentKey, Collections.emptyMap());
    }

    public void put(@NonNull V data) {
        P parentKey = this.parentKeyFunction.apply(data);
        K dataKey = this.dataKeyFunction.apply(data);

        this.ownedCacheMap
            .computeIfAbsent(parentKey, k -> new ConcurrentHashMap<>())
            .put(dataKey, data);
    }

    public void remove(@NonNull V data) {
        P parentKey = this.parentKeyFunction.apply(data);
        K dataKey = this.dataKeyFunction.apply(data);

        Map<K, V> cache = this.getOwnedCache(parentKey);
        if (cache.isEmpty()) return;

        cache.remove(dataKey);

        if (cache.isEmpty()) {
            this.ownedCacheMap.remove(parentKey);
        }
    }

    public @Nullable V get(@NonNull P parentKey, @NonNull K dataKey) {
        return this.getOwnedCache(parentKey).get(dataKey);
    }
}
