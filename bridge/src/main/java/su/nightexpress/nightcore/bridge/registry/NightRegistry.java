package su.nightexpress.nightcore.bridge.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NightRegistry<K, V> {

    private final Map<K, V> byKey;

    private boolean frozen;

    public NightRegistry() {
        this.byKey = new ConcurrentHashMap<>();
    }

    public void register(@NotNull K key, @NotNull V value) {
        if (this.isFrozen()) throw new UnsupportedOperationException("Adding values to frozen registry");

        this.byKey.put(key, value);
    }

    public void unregister(@NotNull K key) {
        if (this.isFrozen()) throw new UnsupportedOperationException("Removing values from frozen registry");

        this.byKey.remove(key);
    }

    @Nullable
    public V byKey(@NotNull K key) {
        return this.byKey.get(key);
    }

    @NotNull
    public Optional<V> lookup(@NotNull K key) {
        return Optional.ofNullable(this.byKey(key));
    }

    @NotNull
    public Map<K, V> map() {
        return this.byKey;
    }

    @NotNull
    public Set<V> values() {
        return new HashSet<>(this.byKey.values());
    }

    @NotNull
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
