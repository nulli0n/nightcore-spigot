package su.nightexpress.nightcore.util.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class SimpleRegistry<K, V> {

    protected final Map<K, V>        byKey;
    protected final UnaryOperator<K> keyMapper;

    public SimpleRegistry() {
        this(UnaryOperator.identity());
    }

    public SimpleRegistry(@NonNull UnaryOperator<K> keyMapper) {
        this.byKey = new ConcurrentHashMap<>();
        this.keyMapper = keyMapper;
    }

    @Deprecated
    public void register(@NonNull K key, @NonNull V value) {
        this.add(key, value);
    }

    public void add(@NonNull K key, @NonNull V value) {
        this.byKey.put(this.keyMapper.apply(key), value);
    }

    @Deprecated
    public void unregister(@NonNull K key) {
        this.remove(key);
    }

    public void remove(@NonNull K key) {
        this.byKey.remove(this.keyMapper.apply(key));
    }

    @Nullable
    public V getByKey(@NonNull K key) {
        return this.byKey.get(this.keyMapper.apply(key));
    }

    @NonNull
    public Optional<V> byKey(@NonNull K key) {
        return Optional.ofNullable(this.getByKey(key));
    }

    @NonNull
    public Map<K, V> map() {
        return Collections.unmodifiableMap(this.byKey);
    }

    public @NonNull Stream<V> stream() {
        return this.byKey.values().stream();
    }

    @NonNull
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.byKey.values());
    }

    @NonNull
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
