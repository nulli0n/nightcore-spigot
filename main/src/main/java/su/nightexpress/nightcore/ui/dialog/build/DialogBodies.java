package su.nightexpress.nightcore.ui.dialog.build;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class DialogBodies {

    /*@NonNull
    public static WrappedItemDialogBody item(@NonNull ItemStack item,
                                             @Nullable WrappedPlainMessageDialogBody description,
                                             boolean showDecorations,
                                             boolean showTooltip,
                                             int width,
                                             int height) {
        return item(item).description(description).showDecorations(showDecorations).showTooltip(showTooltip).width(width).height(height).build();
    }*/

    public static WrappedItemDialogBody.@NonNull Builder item(@NonNull NightItem item) {
        return new WrappedItemDialogBody.Builder(item.getItemStack());
    }

    public static WrappedItemDialogBody.@NonNull Builder item(@NonNull NightItem item, @NonNull DialogElementLocale description) {
        return new WrappedItemDialogBody.Builder(item.getItemStack()).description(plainMessage(description));
    }

    public static WrappedItemDialogBody.@NonNull Builder item(@NonNull ItemStack itemStack) {
        return new WrappedItemDialogBody.Builder(itemStack);
    }

    public static WrappedItemDialogBody.@NonNull Builder item(@NonNull ItemStack itemStack, @NonNull DialogElementLocale description) {
        return new WrappedItemDialogBody.Builder(itemStack).description(plainMessage(description));
    }


    @NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull DialogElementLocale locale) {
        return new WrappedPlainMessageDialogBody(locale.contents(), locale.width());
    }

    @NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull TextLocale locale) {
        return plainMessage(locale.text());
    }

    /*@NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull String contents) {
        return plainMessage(NightMessage.parse(contents));
    }*/

    @NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull String contents) {
        return new WrappedPlainMessageDialogBody(contents);
    }

    @NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull TextLocale locale, int width) {
        return plainMessage(locale.text(), width);
    }

    /*@NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull String contents, int width) {
        return plainMessage(NightMessage.parse(contents), width);
    }*/

    @NonNull
    public static WrappedPlainMessageDialogBody plainMessage(@NonNull String contents, int width) {
        return new WrappedPlainMessageDialogBody(contents, width);
    }
}
