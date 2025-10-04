package su.nightexpress.nightcore.bridge.spigot.click;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEventCustom;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.WrappedPayload;

public class SpigotClickEventAdapter {

    @NotNull
    public static ClickEvent adaptClickEvent(@NotNull NightClickEvent event, @NotNull DialogAdapter<?> dDialogAdapter) {
        WrappedPayload payload = event.payload();

        return switch (payload) {
            case WrappedPayload.Custom custom -> MC_1_21_7.fromCustom(custom);
            case WrappedPayload.Dialog dialog -> MC_1_21_7.fromDialog(dialog, dDialogAdapter);
            case WrappedPayload.Int(int integer) -> new ClickEvent(ClickEvent.Action.CHANGE_PAGE, String.valueOf(integer));
            case WrappedPayload.Text(@NotNull String value) -> {
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

    private static class MC_1_21_7 {

        @NotNull
        public static ClickEvent fromCustom(@NotNull WrappedPayload.Custom custom) {
            return new ClickEventCustom(custom.key().value(), custom.nbt().asString());
        }

        @NotNull
        public static ClickEvent fromDialog(@NotNull WrappedPayload.Dialog dialog, @NotNull DialogAdapter<?> dDialogAdapter) {
            return new ShowDialogClickEvent((Dialog) dDialogAdapter.adaptDialog(dialog.dialog()));
        }
    }
}
