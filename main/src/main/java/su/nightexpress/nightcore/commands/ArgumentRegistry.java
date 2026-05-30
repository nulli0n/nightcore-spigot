package su.nightexpress.nightcore.commands;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.commands.argument.ArgumentType;

@NullMarked
public class ArgumentRegistry {

    private final Map<Class<?>, ArgumentType<?>> types = new HashMap<>();

    public <T> void register(Class<T> type, ArgumentType<T> codec) {
        this.types.put(type, codec);
    }

    public boolean isRegistered(Class<?> type) {
        return this.types.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable ArgumentType<T> get(T object) {
        return (ArgumentType<T>) this.get(object.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable ArgumentType<T> get(Class<T> type) {
        if (this.isRegistered(type)) {
            return (ArgumentType<T>) this.types.get(type);
        }

        // See if a superclass/interface serializer is registered
        for (Map.Entry<Class<?>, ArgumentType<?>> entry : this.types.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return (ArgumentType<T>) entry.getValue();
            }
        }

        return null;
    }
}
