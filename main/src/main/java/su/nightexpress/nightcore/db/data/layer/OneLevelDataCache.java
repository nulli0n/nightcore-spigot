package su.nightexpress.nightcore.db.data.layer;

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

public class OneLevelDataCache<K, V extends Stateful> implements DataCache<V> {

    private final Function<V, K> dataIdFunction;

    private final Map<K, V> byKeyMap;

    public OneLevelDataCache(@NonNull Function<V, K> dataIdFunction) {
        this.dataIdFunction = dataIdFunction;

        this.byKeyMap = new ConcurrentHashMap<>();
    }

    public static <K, V extends Stateful> @NonNull OneLevelDataCache<K, V> create(@NonNull Function<V, K> dataIdFunction) {
        return new OneLevelDataCache<>(dataIdFunction);
    }

    @Override
    public boolean contains(@NonNull V data) {
        K dataId = this.dataIdFunction.apply(data);

        return this.byKeyMap.containsKey(dataId);
    }

    @Override
    public void clear() {
        this.byKeyMap.clear();
    }

    @Override
    public void clearRemoved() {
        this.removeIf(Stateful::isRemoved);
    }

    @Override
    public void clearExpired() {
        this.removeIf(Stateful::isCacheExpired);
    }

    @Override
    public void removeIf(@NonNull Predicate<V> predicate) {
        this.byKeyMap.values().removeIf(predicate);
    }

    @Override
    public @NonNull Set<V> getAll() {
        return this.streamAll().collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public @NonNull Stream<V> streamAll() {
        return this.byKeyMap.values().stream();
    }

    public void put(@NonNull V data) {
        K key = this.dataIdFunction.apply(data);

        this.byKeyMap.put(key, data);
    }

    public void remove(@NonNull V data) {
        K key = this.dataIdFunction.apply(data);

        this.byKeyMap.remove(key);
    }

    public @Nullable V get(@NonNull K key) {
        return this.byKeyMap.get(key);
    }
}
