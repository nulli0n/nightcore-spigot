package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import java.util.function.UnaryOperator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

@NullMarked
public record WrappedPlainMessageDialogBody(String contents, int width) implements WrappedDialogBody {

    public WrappedPlainMessageDialogBody(String contents) {
        this(contents, DialogDefaults.DEFAULT_PLAIN_BODY_WIDTH);
    }

    public WrappedPlainMessageDialogBody(String contents, int width) {
        this.contents = contents;
        this.width = DialogDefaults.clampWidth(width);
    }

    @Override
    public <D> D adapt(DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }

    @Override
    public WrappedPlainMessageDialogBody replace(UnaryOperator<String> operator) {
        return new WrappedPlainMessageDialogBody(operator.apply(this.contents), this.width);
    }
}
