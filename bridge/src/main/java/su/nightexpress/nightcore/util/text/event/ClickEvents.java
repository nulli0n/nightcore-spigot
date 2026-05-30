package su.nightexpress.nightcore.util.text.event;

import java.net.URL;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.WrappedPayload;

public class ClickEvents {

    private ClickEvents() {
    }

    @NonNull
    public static NightClickEvent openUrl(@NonNull String url) {
        return new NightClickEvent(NightClickEvent.Action.OPEN_URL, WrappedPayload.string(url));
    }

    @NonNull
    public static NightClickEvent openUrl(@NonNull URL url) {
        return openUrl(url.toExternalForm());
    }

    @NonNull
    public static NightClickEvent openFile(@NonNull String file) {
        return new NightClickEvent(NightClickEvent.Action.OPEN_FILE, WrappedPayload.string(file));
    }

    @NonNull
    public static NightClickEvent runCommand(@NonNull String command) {
        return new NightClickEvent(NightClickEvent.Action.RUN_COMMAND, WrappedPayload.string(command));
    }

    @NonNull
    public static NightClickEvent suggestCommand(@NonNull String command) {
        return new NightClickEvent(NightClickEvent.Action.SUGGEST_COMMAND, WrappedPayload.string(command));
    }

    @NonNull
    public static NightClickEvent changePage(int page) {
        return new NightClickEvent(NightClickEvent.Action.CHANGE_PAGE, WrappedPayload.integer(page));
    }

    @NonNull
    public static NightClickEvent copyToClipboard(@NonNull String text) {
        return new NightClickEvent(NightClickEvent.Action.COPY_TO_CLIPBOARD, WrappedPayload.string(text));
    }

    @NonNull
    public static NightClickEvent showDialog(@NonNull WrappedDialog dialog) {
        return new NightClickEvent(NightClickEvent.Action.SHOW_DIALOG, WrappedPayload.dialog(dialog));
    }

    @NonNull
    public static NightClickEvent custom(@NonNull NightKey key, @NonNull NightNbtHolder nbt) {
        return new NightClickEvent(NightClickEvent.Action.CUSTOM, WrappedPayload.custom(key, nbt));
    }
}
