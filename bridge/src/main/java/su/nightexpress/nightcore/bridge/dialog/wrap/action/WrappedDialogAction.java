package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public interface WrappedDialogAction {

    @NotNull <A> A adapt(@NotNull DialogActionAdapter<A> adapter);
}
