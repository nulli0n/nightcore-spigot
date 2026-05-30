package su.nightexpress.nightcore.bridge.spigot.click;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEventCustom;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.WrappedPayload;

public class SpigotClickEventAdapter {

    @NonNull
    public static ClickEvent adaptClickEvent(@NonNull NightClickEvent event, @NonNull DialogAdapter<?> dDialogAdapter) {
        WrappedPayload payload = event.payload();

        return switch (payload) {
            case WrappedPayload.Custom custom -> fromCustom(custom);
            case WrappedPayload.Dialog dialog -> fromDialog(dialog, dDialogAdapter);
            case WrappedPayload.Int(int integer) -> new ClickEvent(ClickEvent.Action.CHANGE_PAGE, String.valueOf(
                integer));
            case WrappedPayload.Text(@NonNull String value) -> {
                ClickEvent.Action action = switch (event.action()) {
                    case OPEN_URL -> ClickEvent.Action.OPEN_URL;
                    case OPEN_FILE -> ClickEvent.Action.OPEN_FILE;
                    case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
                    case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
                    case COPY_TO_CLIPBOARD -> ClickEvent.Action.COPY_TO_CLIPBOARD;
                    default -> throw new IllegalStateException("Unexpected value: " + event.action());
                };

                yield new ClickEvent(action, value);
            }
            default -> throw new IllegalStateException("Unexpected value: " + payload);
        };
    }

    @NonNull
    public static ClickEvent fromCustom(WrappedPayload.@NonNull Custom custom) {
        return new ClickEventCustom(custom.key().value(), custom.nbt().asString());
    }

    @NonNull
    public static ClickEvent fromDialog(WrappedPayload.@NonNull Dialog dialog,
                                        @NonNull DialogAdapter<?> dDialogAdapter) {
        return new ShowDialogClickEvent((Dialog) dDialogAdapter.adaptDialog(dialog.dialog()));
    }
}
