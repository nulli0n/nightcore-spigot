package su.nightexpress.nightcore.bridge.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PlaceholderRegistry {

    private final Map<Class<?>, PayloadResolver<?>> resolverMap;
    private final Map<String, PlaceholderHandler>   handlerMap;

    public record ParsedPlaceholder(@NonNull String key, @NonNull PlaceholderHandler handler, @NonNull String payload) {}

    public PlaceholderRegistry() {
        this.resolverMap = new HashMap<>();
        this.handlerMap = new HashMap<>();
    }

    public <T> void addResolver(@NonNull Class<T> type, @NonNull PayloadResolver<T> resolver) {
        this.resolverMap.put(type, resolver);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> PayloadResolver<T> resolver(@NonNull Class<T> type) {
        return (PayloadResolver<T>) this.resolverMap.get(type);
    }

    public <T> void registerMapped(@NonNull String key, @NonNull Class<T> payloadType, @NonNull BiFunction<Player, T, String> handler) {
        PayloadResolver<T> resolver = this.resolver(payloadType);
        if (resolver == null) throw new IllegalArgumentException("No payload resolver found for %s.".formatted(payloadType.getName()));

        this.registerRaw(key, (player, payload) -> {
            T resolved = resolver.resolve(player, payload);
            if (resolved == null) return null;

            return handler.apply(player, resolved);
        });
    }

    public void registerRaw(@NonNull String key, @NonNull PlaceholderHandler handler) {
        this.handlerMap.put(LowerCase.INTERNAL.apply(key), handler);
    }

    public boolean unregister(@NonNull String key) {
        return this.handlerMap.remove(LowerCase.INTERNAL.apply(key)) != null;
    }

    public boolean isEmpty() {
        return this.handlerMap.isEmpty();
    }

    @Nullable
    public String onPlaceholderRequest(@NonNull Player player, @NonNull String params) {
        ParsedPlaceholder parsed = this.findHandler(params);
        if (parsed == null) return null;

        return parsed.handler().handle(player, parsed.payload());
    }

    @Nullable
    private ParsedPlaceholder findHandler(@NonNull String params) {
        String currentKey = params;
        StringBuilder currentPayload = new StringBuilder();

        while (true) {
            if (this.handlerMap.containsKey(currentKey)) {
                return new ParsedPlaceholder(currentKey, this.handlerMap.get(currentKey), currentPayload.toString());
            }

            int lastUnderscoreIndex = currentKey.lastIndexOf('_');
            if (lastUnderscoreIndex == -1) return null;

            String suffix = currentKey.substring(lastUnderscoreIndex + 1);

            if (currentPayload.isEmpty()) {
                currentPayload = new StringBuilder(suffix);
            }
            else {
                currentPayload.insert(0, suffix + "_");
            }

            currentKey = currentKey.substring(0, lastUnderscoreIndex);
        }
    }
}
