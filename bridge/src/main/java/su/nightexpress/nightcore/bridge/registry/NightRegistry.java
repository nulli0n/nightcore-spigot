package su.nightexpress.nightcore.bridge.registry;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class NightRegistry<K, V> {

    private final Map<K, V> byKey;

    private boolean frozen;

    public NightRegistry() {
        this.byKey = new ConcurrentHashMap<>();
    }

    public void register(@NonNull K key, @NonNull V value) {
        if (this.isFrozen()) throw new UnsupportedOperationException("Adding values to frozen registry");

        this.byKey.put(key, value);
    }

    public void unregister(@NonNull K key) {
        if (this.isFrozen()) throw new UnsupportedOperationException("Removing values from frozen registry");

        this.byKey.remove(key);
    }

    @Nullable
    public V byKey(@NonNull K key) {
        return this.byKey.get(key);
    }

    @NonNull
    public Optional<V> lookup(@NonNull K key) {
        return Optional.ofNullable(this.byKey(key));
    }

    @NonNull
    public Map<K, V> map() {
        return this.byKey;
    }

    @NonNull
    public Set<V> values() {
        return new HashSet<>(this.byKey.values());
    }

    @NonNull
    public Set<K> keys() {
        return new HashSet<>(this.byKey.keySet());
    }

    public int size() {
        return this.byKey.size();
    }

    public void clear() {
        this.byKey.clear();
    }

    public void freeze() {
        this.frozen = true;
    }

    public void unfreeze() {
        this.frozen = false;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public boolean isEmpty() {
        return this.byKey.isEmpty();
    }
}
