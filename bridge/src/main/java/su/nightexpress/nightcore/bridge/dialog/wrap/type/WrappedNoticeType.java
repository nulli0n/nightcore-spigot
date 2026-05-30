package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedNoticeType(@NonNull WrappedActionButton action) implements WrappedDialogType {

    @Override
    @NonNull
    public <T> T adapt(@NonNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NonNull
    public WrappedNoticeType replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedNoticeType(this.action.replace(operator));
    }
}
