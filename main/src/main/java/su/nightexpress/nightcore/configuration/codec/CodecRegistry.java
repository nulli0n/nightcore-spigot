package su.nightexpress.nightcore.configuration.codec;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CodecRegistry {

    private final Map<Class<?>, ConfigCodec<?>> codecByType = new HashMap<>();

    public <T> void register(Class<T> type, ConfigCodec<T> codec) {
        this.codecByType.put(type, codec);
    }

    public boolean isRegistered(Class<?> type) {
        return this.codecByType.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable ConfigCodec<T> get(T object) {
        return (ConfigCodec<T>) this.get(object.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable ConfigCodec<T> get(Class<T> type) {
        if (this.isRegistered(type)) {
            return (ConfigCodec<T>) this.codecByType.get(type);
        }

        // See if a superclass/interface serializer is registered
        for (Map.Entry<Class<?>, ConfigCodec<?>> entry : this.codecByType.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return (ConfigCodec<T>) entry.getValue();
            }
        }

        return null;
    }
}
