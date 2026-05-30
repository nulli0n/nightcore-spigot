package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogInput {

    @NonNull
    String key();

    @NonNull
    <I> I adapt(@NonNull DialogInputAdapter<I> adapter);

    @Deprecated
    @NonNull
    WrappedDialogInput replace(@NonNull UnaryOperator<String> operator);
}
