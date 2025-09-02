package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogBody {

    @NotNull <D> D adapt(@NotNull DialogBodyAdapter<D> adapter);

    @NotNull WrappedDialogBody replace(@NotNull UnaryOperator<String> operator);
}
