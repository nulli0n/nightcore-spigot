package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedConfirmationType(@NonNull WrappedActionButton yesButton,
                                      @NonNull WrappedActionButton noButton) implements WrappedDialogType {

    @Override
    @NonNull
    public <T> T adapt(@NonNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NonNull
    public WrappedConfirmationType replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedConfirmationType(this.yesButton.replace(operator), this.noButton.replace(operator));
    }
}
