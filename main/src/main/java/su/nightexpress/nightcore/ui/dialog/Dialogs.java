package su.nightexpress.nightcore.ui.dialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.DialogViewer;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.bridge.dialog.response.DialogResponseHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.Software;

public class Dialogs {

    private static final Map<UUID, DialogViewer> ACTIVE_DIALOGS = new HashMap<>();

    public static void clearDialogs() {
        new HashSet<>(ACTIVE_DIALOGS.keySet()).stream().map(Players::getPlayer).filter(Objects::nonNull).forEach(
            Dialogs::exitDialog);
        ACTIVE_DIALOGS.clear();
    }

    public static boolean isInDialog(@NonNull Player player) {
        return ACTIVE_DIALOGS.containsKey(player.getUniqueId());
    }

    @Nullable
    public static DialogViewer getViewer(@NonNull Player player) {
        return ACTIVE_DIALOGS.get(player.getUniqueId());
    }

    public static void handleClick(@NonNull DialogClickResult result) {
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

    public static void exitDialog(@NonNull Player player) {
        DialogViewer viewer = ACTIVE_DIALOGS.remove(player.getUniqueId());
        if (viewer == null) return;

        Players.closeDialog(player);
    }

    public static void showDialog(@NonNull Player player, @NonNull WrappedDialog dialog) {
        showDialog(player, dialog, null);
    }

    public static void showDialog(@NonNull Player player, @NonNull WrappedDialog dialog, @Nullable Runnable callback) {
        DialogUser user = new DialogUser(player, dialog, callback);
        ACTIVE_DIALOGS.put(player.getUniqueId(), user);

        Software.get().showDialog(player, dialog);
    }

    @NonNull
    public static WrappedDialog create(@NonNull Consumer<WrappedDialog.Builder> consumer) {
        WrappedDialog.Builder builder = builder();

        consumer.accept(builder);

        return builder.build();
    }

    public static void createAndShow(@NonNull Player player, @NonNull Consumer<WrappedDialog.Builder> consumer) {
        createAndShow(player, consumer, null);
    }

    public static void createAndShow(@NonNull Player player, @NonNull Consumer<WrappedDialog.Builder> consumer, @Nullable Runnable callback) {
        WrappedDialog dialog = create(consumer);
        showDialog(player, dialog, callback);
    }

    public static WrappedDialog.@NonNull Builder builder() {
        return new WrappedDialog.Builder();
    }
}
