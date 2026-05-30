package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogType {

    @NonNull
    <T> T adapt(@NonNull DialogTypeAdapter<T> factory);

    @Deprecated
    @NonNull
    WrappedDialogType replace(@NonNull UnaryOperator<String> operator);
}
