package su.nightexpress.nightcore.integration.currency;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CurrencyRegistry {

    private final Map<String, Currency> byIdMap;

    public CurrencyRegistry() {
        this.byIdMap = new ConcurrentHashMap<>();
    }

    public void register(@NonNull Currency currency) {
        this.byIdMap.put(currency.getInternalId(), currency);
    }

    public boolean unregister(@NonNull String id) {
        return this.byIdMap.remove(LowerCase.INTERNAL.apply(id)) != null;
    }

    public void clear() {
        this.byIdMap.clear();
    }

    public boolean isEmpty() {
        return this.byIdMap.isEmpty();
    }

    public boolean contains(@NonNull String id) {
        return this.byIdMap.containsKey(LowerCase.INTERNAL.apply(id));
    }

    public void forEach(@NonNull Consumer<Currency> consumer) {
        this.values().forEach(consumer);
    }

    @NonNull
    public Stream<Currency> stream() {
        return this.values().stream();
    }

    @NonNull
    public Map<String, Currency> getByIdMap() {
        return Map.copyOf(this.byIdMap);
    }

    @NonNull
    public Set<Currency> values() {
        return Set.copyOf(this.byIdMap.values());
    }

    @NonNull
    public Set<String> keys() {
        return Set.copyOf(this.byIdMap.keySet());
    }

    @Nullable
    public Currency getById(@NonNull String id) {
        return this.byIdMap.get(LowerCase.INTERNAL.apply(id));
    }

    @NonNull
    public Optional<Currency> byId(@NonNull String id) {
        return Optional.ofNullable(this.getById(id));
    }
}
