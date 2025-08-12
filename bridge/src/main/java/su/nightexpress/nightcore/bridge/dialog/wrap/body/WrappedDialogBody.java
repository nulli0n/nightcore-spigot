package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

public interface WrappedDialogBody {

    @NotNull <D> D adapt(@NotNull DialogBodyAdapter<D> adapter);
}
