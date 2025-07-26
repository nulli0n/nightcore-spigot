package su.nightexpress.nightcore.ui.dialog.build;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.NightMessage;

public class DialogBodies {

    @NotNull
    public static WrappedItemDialogBody item(@NotNull ItemStack item,
                                             @Nullable WrappedPlainMessageDialogBody description,
                                             boolean showDecorations,
                                             boolean showTooltip,
                                             int width,
                                             int height) {
        return item(item).description(description).showDecorations(showDecorations).showTooltip(showTooltip).width(width).height(height).build();
    }

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull NightItem item) {
        return item(item.getItemStack());
    }

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull ItemStack item) {
        return new WrappedItemDialogBody.Builder(item);
    }



    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents) {
        return plainMessage(NightMessage.parse(contents));
    }

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull NightComponent contents) {
        return new WrappedPlainMessageDialogBody(contents);
    }

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents, int width) {
        return plainMessage(NightMessage.parse(contents), width);
    }

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull NightComponent contents, int width) {
        return new WrappedPlainMessageDialogBody(contents, width);
    }
}
