package su.nightexpress.nightcore.ui.dialog;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface DialogHandler {

    boolean handle(@NotNull DialogInput input);
}
