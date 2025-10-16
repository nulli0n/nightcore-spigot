package su.nightexpress.nightcore.bridge.registry;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Holder<T> {

    private T value;

    public void set(@NotNull T value) {
        if (this.value != null) throw new IllegalStateException("Holder value already set: " + value);

        this.value = value;
    }

    @NotNull
    public T get() {
        if (this.value == null) throw new IllegalStateException("Holder value is not set");

        return this.value;
    }

    public void clear() {
        this.value = null;
    }

    public void ifPresent(@NotNull Consumer<T> consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public boolean isEmpty() {
        return this.value == null;
    }
}
