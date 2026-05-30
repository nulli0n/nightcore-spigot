package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedServerLinksType(@Nullable WrappedActionButton exitAction, int columns,
                                     int buttonWidth) implements WrappedDialogType {

    @Override
    @NonNull
    public <T> T adapt(@NonNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NonNull
    public WrappedServerLinksType replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedServerLinksType(this.exitAction == null ? null : this.exitAction.replace(
            operator), this.columns, this.buttonWidth);
    }
}
