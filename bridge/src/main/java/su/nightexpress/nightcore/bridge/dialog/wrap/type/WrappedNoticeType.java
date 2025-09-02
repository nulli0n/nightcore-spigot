package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.function.UnaryOperator;

public record WrappedNoticeType(@NotNull WrappedActionButton action) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NotNull
    public WrappedNoticeType replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedNoticeType(this.action.replace(operator));
    }
}
