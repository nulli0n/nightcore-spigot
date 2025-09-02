package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedServerLinksType(@Nullable WrappedActionButton exitAction, int columns, int buttonWidth) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NotNull
    public WrappedServerLinksType replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedServerLinksType(this.exitAction == null ? null : this.exitAction.replace(operator), this.columns, this.buttonWidth);
    }
}
