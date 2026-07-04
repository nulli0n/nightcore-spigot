package su.nightexpress.nightcore.configuration.codec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.reflect.TypeReference;

@NullMarked
public class CodecRegistry {

    private final Map<Type, ConfigCodec<?>> codecByType = new HashMap<>();

    public <T> void register(Class<T> type, ConfigCodec<T> codec) {
        this.codecByType.put(type, codec);
    }

    public <T> void register(TypeReference<T> typeRef, ConfigCodec<T> codec) {
        this.codecByType.put(typeRef.getType(), codec);
    }

    public boolean isRegistered(Type type) {
        return this.resolveCodec(type) != null;
    }

    public boolean isRegistered(Class<?> type) {
        return this.isRegistered((Type) type);
    }

    public <T> @Nullable ConfigCodec<T> get(T object) {
        return this.resolveCodec(object.getClass());
    }

    public <T> @Nullable ConfigCodec<T> get(Class<T> type) {
        return this.resolveCodec(type);
    }

    public <T> @Nullable ConfigCodec<T> get(TypeReference<T> typeRef) {
        return this.resolveCodec(typeRef.getType());
    }

    @SuppressWarnings("unchecked")
    private <T> @Nullable ConfigCodec<T> resolveCodec(Type type) {
        // Try exact match first
        ConfigCodec<?> exact = this.codecByType.get(type);
        if (exact != null) {
            return (ConfigCodec<T>) exact;
        }

        // Try raw class lookups
        Class<?> rawType = getRawClass(type);
        if (rawType == null) {
            return null;
        }

        for (Map.Entry<Type, ConfigCodec<?>> entry : this.codecByType.entrySet()) {
            if (rawType.equals(getRawClass(entry.getKey()))) {
                return (ConfigCodec<T>) entry.getValue();
            }
        }

        return null;
    }

    private static @Nullable Class<?> getRawClass(Type type) {
        if (type instanceof Class<?> clazz) {
            return clazz;
        }

        if (type instanceof ParameterizedType parameterized && parameterized.getRawType() instanceof Class<?> clazz) {
            return clazz;
        }

        return null;
    }
}