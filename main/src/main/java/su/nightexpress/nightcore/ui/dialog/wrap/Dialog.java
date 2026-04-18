package su.nightexpress.nightcore.ui.dialog.wrap;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public abstract class Dialog<T> implements LangContainer {

    @NonNull
    public abstract WrappedDialog create(@NonNull Player player, @NonNull T data);

    public void show(@NonNull Player player, @NonNull T data, @Nullable Runnable callback) {
        Dialogs.showDialog(player, this.create(player, data), callback);
    }

    @NonNull
    protected static String title(@NonNull String prefix, @NonNull String title) {
        return TagWrappers.YELLOW.and(TagWrappers.BOLD)
            .wrap(prefix.toUpperCase()) + TagWrappers.DARK_GRAY.wrap(" » ") + TagWrappers.WHITE.wrap(title);
    }
}