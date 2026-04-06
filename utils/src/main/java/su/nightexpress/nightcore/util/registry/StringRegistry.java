package su.nightexpress.nightcore.util.registry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.function.Function;

public class StringRegistry<V> extends SimpleRegistry<String, V> {

    public StringRegistry() {
        this(LowerCase.INTERNAL::apply);
    }

    public StringRegistry(@NonNull Function<String, String> keyMapper) {
        super(keyMapper);
    }
}
