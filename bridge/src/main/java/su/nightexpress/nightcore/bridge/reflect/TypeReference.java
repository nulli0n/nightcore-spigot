package su.nightexpress.nightcore.bridge.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class TypeReference<T> {

    private final Type type;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new IllegalArgumentException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }
}
