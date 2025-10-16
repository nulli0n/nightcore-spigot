package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogType {

    @NotNull <T> T adapt(@NotNull DialogTypeAdapter<T> factory);

    @Deprecated
    @NotNull WrappedDialogType replace(@NotNull UnaryOperator<String> operator);
}
