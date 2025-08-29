package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public record WrappedPlainMessageDialogBody(@NotNull String contents, int width) implements WrappedDialogBody {

    public WrappedPlainMessageDialogBody(@NotNull String contents) {
        this(contents, 200);
    }

    public WrappedPlainMessageDialogBody(@NotNull String contents, int width) {
        this.contents = contents;
        this.width = Math.clamp(width, 1, 1024); // TODO Const
    }

    @Override
    @NotNull
    public <D> D adapt(@NotNull DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }

    @Override
    @NotNull
    public WrappedPlainMessageDialogBody replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedPlainMessageDialogBody(operator.apply(this.contents), this.width);
    }
}
