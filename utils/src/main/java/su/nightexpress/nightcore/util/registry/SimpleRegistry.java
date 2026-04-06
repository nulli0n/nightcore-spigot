package su.nightexpress.nightcore.util.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SimpleRegistry<K, V> {

    private final Map<K, V>      byKey;
    private final Function<K, K> keyMapper;

    public SimpleRegistry() {
        this(Function.identity());
    }

    public SimpleRegistry(@NonNull Function<K, K> keyMapper) {
        this.byKey = new ConcurrentHashMap<>();
        this.keyMapper = keyMapper;
    }

    public void register(@NotNull K key, @NotNull V value) {
        this.byKey.put(this.keyMapper.apply(key), value);
    }

    public void unregister(@NotNull K key) {
        this.byKey.remove(this.keyMapper.apply(key));
    }

    @Nullable
    public V getByKey(@NotNull K key) {
        return this.byKey.get(this.keyMapper.apply(key));
    }

    @NotNull
    public Optional<V> byKey(@NotNull K key) {
        return Optional.ofNullable(this.getByKey(key));
    }

    @NotNull
    public Map<K, V> map() {
        return Collections.unmodifiableMap(this.byKey);
    }

    @NotNull
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.byKey.values());
    }

    @NotNull
    public Set<K> keys() {
        return Collections.unmodifiableSet(this.byKey.keySet());
    }

    public int size() {
        return this.byKey.size();
    }

    public void clear() {
        this.byKey.clear();
    }

    public boolean isEmpty() {
        return this.byKey.isEmpty();
    }
}
