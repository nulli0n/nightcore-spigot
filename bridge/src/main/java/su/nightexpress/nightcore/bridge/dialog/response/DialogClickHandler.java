package su.nightexpress.nightcore.bridge.dialog.response;

import org.jetbrains.annotations.NotNull;

public interface DialogClickHandler {

    void handleClick(@NotNull DialogClickResult result);
}
