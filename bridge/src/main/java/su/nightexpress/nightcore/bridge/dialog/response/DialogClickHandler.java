package su.nightexpress.nightcore.bridge.dialog.response;

import org.jspecify.annotations.NonNull;

public interface DialogClickHandler {

    void handleClick(@NonNull DialogClickResult result);
}
