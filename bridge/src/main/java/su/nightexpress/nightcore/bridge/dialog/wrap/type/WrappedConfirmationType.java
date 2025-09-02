package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedConfirmationType(@NotNull WrappedActionButton yesButton, @NotNull WrappedActionButton noButton) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NotNull
    public WrappedConfirmationType replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedConfirmationType(this.yesButton.replace(operator), this.noButton.replace(operator));
    }
}
