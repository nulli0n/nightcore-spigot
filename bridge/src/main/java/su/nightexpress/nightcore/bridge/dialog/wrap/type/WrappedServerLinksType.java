package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

public record WrappedServerLinksType(@Nullable WrappedActionButton exitAction, int columns, int buttonWidth) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }
}
