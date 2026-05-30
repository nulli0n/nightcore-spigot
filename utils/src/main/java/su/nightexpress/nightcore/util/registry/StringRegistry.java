package su.nightexpress.nightcore.util.registry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class StringRegistry<V> extends SimpleRegistry<String, V> {

    public StringRegistry() {
        this(LowerCase.INTERNAL::apply);
    }

    @Deprecated
    public StringRegistry(@NonNull Function<String, String> keyMapper) {
        this(keyMapper::apply);
    }

    public StringRegistry(@NonNull UnaryOperator<String> keyMapper) {
        super(keyMapper);
    }
}
