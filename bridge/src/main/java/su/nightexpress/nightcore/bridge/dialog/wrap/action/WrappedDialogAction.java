package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public interface WrappedDialogAction {

    @NonNull
    <A> A adapt(@NonNull DialogActionAdapter<A> adapter);
}
