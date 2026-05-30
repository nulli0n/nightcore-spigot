package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogBody {

    @NonNull
    <D> D adapt(@NonNull DialogBodyAdapter<D> adapter);

    @Deprecated
    @NonNull
    WrappedDialogBody replace(@NonNull UnaryOperator<String> operator);
}
