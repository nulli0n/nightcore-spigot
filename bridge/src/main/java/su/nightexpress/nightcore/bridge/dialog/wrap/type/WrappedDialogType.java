package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;

public interface WrappedDialogType {

    @NotNull <T> T adapt(@NotNull DialogTypeAdapter<T> factory);
}
