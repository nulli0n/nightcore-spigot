package su.nightexpress.nightcore.util.text.event;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.WrappedPayload;

import java.net.URL;

public class ClickEvents {
    
    @NotNull
    public static NightClickEvent openUrl(@NotNull String url) {
        return new NightClickEvent(NightClickEvent.Action.OPEN_URL, WrappedPayload.string(url));
    }
    
    @NotNull
    public static NightClickEvent openUrl(@NotNull URL url) {
        return openUrl(url.toExternalForm());
    }
    
    @NotNull
    public static NightClickEvent openFile(@NotNull String file) {
        return new NightClickEvent(NightClickEvent.Action.OPEN_FILE, WrappedPayload.string(file));
    }

    @NotNull
    public static NightClickEvent runCommand(@NotNull String command) {
        return new NightClickEvent(NightClickEvent.Action.RUN_COMMAND, WrappedPayload.string(command));
    }

    @NotNull
    public static NightClickEvent suggestCommand(@NotNull String command) {
        return new NightClickEvent(NightClickEvent.Action.SUGGEST_COMMAND, WrappedPayload.string(command));
    }
    
    @NotNull
    public static NightClickEvent changePage(int page) {
        return new NightClickEvent(NightClickEvent.Action.CHANGE_PAGE, WrappedPayload.integer(page));
    }
    
    @NotNull
    public static NightClickEvent copyToClipboard(@NotNull String text) {
        return new NightClickEvent(NightClickEvent.Action.COPY_TO_CLIPBOARD, WrappedPayload.string(text));
    }

    @NotNull
    public static NightClickEvent showDialog(@NotNull WrappedDialog dialog) {
        return new NightClickEvent(NightClickEvent.Action.SHOW_DIALOG, WrappedPayload.dialog(dialog));
    }

    @NotNull
    public static NightClickEvent custom(@NotNull NamespacedKey key, @NotNull NightNbtHolder nbt) {
        return new NightClickEvent(NightClickEvent.Action.CUSTOM, WrappedPayload.custom(key, nbt));
    }
}
