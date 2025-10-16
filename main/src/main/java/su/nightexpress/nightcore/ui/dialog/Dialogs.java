package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.DialogViewer;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.bridge.dialog.response.DialogResponseHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.*;
import java.util.function.Consumer;

public class Dialogs {

    private static final Map<UUID, DialogViewer> ACTIVE_DIALOGS = new HashMap<>();

    public static void clearDialogs() {
        new HashSet<>(ACTIVE_DIALOGS.keySet()).stream().map(Players::getPlayer).filter(Objects::nonNull).forEach(Dialogs::exitDialog);
        ACTIVE_DIALOGS.clear();
    }

    public static boolean isInDialog(@NotNull Player player) {
        return ACTIVE_DIALOGS.containsKey(player.getUniqueId());
    }

    @Nullable
    public static DialogViewer getViewer(@NotNull Player player) {
        return ACTIVE_DIALOGS.get(player.getUniqueId());
    }

    public static void handleClick(@NotNull DialogClickResult result) {
        Player player = result.getPlayer();

        // We can't know when a player's dialog was closed by API calls or other causes,
        // so to prevent it staying in the map, we do remove it on any button click.
        // This should be safe as dialogs are either closed or reopened on any clicks/changes.
        DialogViewer viewer = ACTIVE_DIALOGS.remove(player.getUniqueId());
        if (viewer == null) return;

        WrappedDialog dialog = viewer.getDialog();

        NamespacedKey identifier = result.getIdentifier();
        NightNbtHolder nbtHolder = result.getNbtHolder();

        DialogResponseHandler handler = dialog.responseHandlers().get(identifier.getKey());
        if (handler == null) return;

        handler.handle(viewer, identifier, nbtHolder);
    }

    public static void exitDialog(@NotNull Player player) {
        DialogViewer viewer = ACTIVE_DIALOGS.remove(player.getUniqueId());
        if (viewer == null) return;

        Players.closeDialog(player);
    }

    public static void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog) {
        showDialog(player, dialog, null);
    }

    public static void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog, @Nullable Runnable callback) {
        DialogUser user = new DialogUser(player, dialog, callback);
        ACTIVE_DIALOGS.put(player.getUniqueId(), user);

        Software.get().showDialog(player, dialog);
    }

    @NotNull
    public static WrappedDialog create(@NotNull Consumer<WrappedDialog.Builder> consumer) {
        WrappedDialog.Builder builder = builder();

        consumer.accept(builder);

        return builder.build();
    }

    public static void createAndShow(@NotNull Player player, @NotNull Consumer<WrappedDialog.Builder> consumer) {
        createAndShow(player, consumer, null);
    }

    public static void createAndShow(@NotNull Player player, @NotNull Consumer<WrappedDialog.Builder> consumer, @Nullable Runnable callback) {
        WrappedDialog dialog = create(consumer);
        showDialog(player, dialog, callback);
    }

    @NotNull
    public static WrappedDialog.Builder builder() {
        return new WrappedDialog.Builder();
    }
}
