package su.nightexpress.nightcore.ui.dialog;

import org.jspecify.annotations.NonNull;

@Deprecated
public interface DialogHandler {

    boolean handle(@NonNull DialogInput input);
}
