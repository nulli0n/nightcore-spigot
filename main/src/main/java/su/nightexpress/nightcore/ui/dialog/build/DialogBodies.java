package su.nightexpress.nightcore.ui.dialog.build;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class DialogBodies {

    /*@NotNull
    public static WrappedItemDialogBody item(@NotNull ItemStack item,
                                             @Nullable WrappedPlainMessageDialogBody description,
                                             boolean showDecorations,
                                             boolean showTooltip,
                                             int width,
                                             int height) {
        return item(item).description(description).showDecorations(showDecorations).showTooltip(showTooltip).width(width).height(height).build();
    }*/

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull NightItem item) {
        return new WrappedItemDialogBody.Builder(item.getItemStack());
    }

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull NightItem item, @NotNull DialogElementLocale description) {
        return new WrappedItemDialogBody.Builder(item.getItemStack()).description(plainMessage(description));
    }

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull ItemStack itemStack) {
        return new WrappedItemDialogBody.Builder(itemStack);
    }

    @NotNull
    public static WrappedItemDialogBody.Builder item(@NotNull ItemStack itemStack, @NotNull DialogElementLocale description) {
        return new WrappedItemDialogBody.Builder(itemStack).description(plainMessage(description));
    }



    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull DialogElementLocale locale) {
        return new WrappedPlainMessageDialogBody(locale.contents(), locale.width());
    }

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull TextLocale locale) {
        return plainMessage(locale.text());
    }

    /*@NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents) {
        return plainMessage(NightMessage.parse(contents));
    }*/

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents) {
        return new WrappedPlainMessageDialogBody(contents);
    }

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull TextLocale locale, int width) {
        return plainMessage(locale.text(), width);
    }

    /*@NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents, int width) {
        return plainMessage(NightMessage.parse(contents), width);
    }*/

    @NotNull
    public static WrappedPlainMessageDialogBody plainMessage(@NotNull String contents, int width) {
        return new WrappedPlainMessageDialogBody(contents, width);
    }
}
