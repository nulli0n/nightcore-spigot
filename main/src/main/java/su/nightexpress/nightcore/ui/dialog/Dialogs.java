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

    /*@Deprecated
    public static void testDialog(@NotNull Player player) {
        createAndShow(player, builder -> {
            builder.base(DialogBases.builder("ARE U AGREE")
                .afterAction(WrappedDialogAfterAction.CLOSE)
                .body(
                    DialogBodies.plainMessage("Test Entry 1"),
                    DialogBodies.item(NightItem.fromType(Material.APPLE).setDisplayName(Tags.RED.wrap(Tags.BOLD.wrap("FUCK"))))
                        .description(DialogBodies.plainMessage("This is very good apple")).build()
                )
                .inputs(
                    DialogInputs.numberRange("numbertest", "Chance", 0f, 100f).initial(0f).step(1f).build()
                )
                .build());

            NightNbtHolder nbt = NightNbtHolder.builder().put("p1", 1).put("p2", "asd").build();
            *//*JsonObject object = new JsonObject();
            object.addProperty("p1", 1);
            object.addProperty("p2", "asd");*//*

            builder.type(DialogTypes.confirmation(
                DialogButtons.action("Yes", "Click for yes").action(DialogActions.customClick("yesgo")).build(),
                DialogButtons.action("No", "Click for no").action(DialogActions.customClick("noway", nbt)).build()
            ));

            builder.handleResponse("yesgo", (player1, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                float chance = nbtHolder.getFloat("numbertest", 0f);
                player1.sendMessage("Chance: " + chance);
            });

            builder.handleResponse("noway", (player1, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Float p1 = nbtHolder.getFloat("p1");
                player1.sendMessage("P1 = " + p1);
            });
        });
    }*/
}
