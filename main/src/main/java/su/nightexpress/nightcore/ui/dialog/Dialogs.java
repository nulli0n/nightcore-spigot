package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class Dialogs {

    private static final Map<UUID, WrappedDialog> ACTIVE_DIALOGS = new HashMap<>();

    public static void clearDialogs() {
        ACTIVE_DIALOGS.keySet().stream().map(Players::getPlayer).filter(Objects::nonNull).forEach(Dialogs::exitDialog);
        ACTIVE_DIALOGS.clear();
    }

    public static boolean isInDialog(@NotNull Player player) {
        return ACTIVE_DIALOGS.containsKey(player.getUniqueId());
    }

    @Nullable
    public static WrappedDialog getDialog(@NotNull Player player) {
        return ACTIVE_DIALOGS.get(player.getUniqueId());
    }

    public static void handleClick(@NotNull Player player, @NotNull DialogClickResult result) {
        WrappedDialog dialog = getDialog(player);
        if (dialog == null) return;

        dialog.handleResponse(result);

        if (dialog.base().afterAction() == WrappedDialogAfterAction.CLOSE) {
            ACTIVE_DIALOGS.remove(player.getUniqueId());
        }
    }

    public static void exitDialog(@NotNull Player player) {
        WrappedDialog dialog = ACTIVE_DIALOGS.remove(player.getUniqueId());
        if (dialog == null) return;

        player.closeInventory();
    }

    public static void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog) {
        ACTIVE_DIALOGS.put(player.getUniqueId(), dialog);

        Software.instance().showDialog(player, dialog);
    }

    @NotNull
    public static WrappedDialog create(@NotNull Consumer<WrappedDialog.Builder> consumer) {
        WrappedDialog.Builder builder = builder();

        consumer.accept(builder);

        return builder.build();
    }

    public static void createAndShow(@NotNull Player player, @NotNull Consumer<WrappedDialog.Builder> consumer) {
        WrappedDialog dialog = create(consumer);
        showDialog(player, dialog);
    }

    @NotNull
    public static WrappedDialog.Builder builder() {
        return new WrappedDialog.Builder();
    }
}
