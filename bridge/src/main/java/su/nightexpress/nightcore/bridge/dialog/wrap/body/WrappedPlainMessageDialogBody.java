package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public record WrappedPlainMessageDialogBody(@NonNull String contents, int width) implements WrappedDialogBody {

    public WrappedPlainMessageDialogBody(@NonNull String contents) {
        this(contents, DialogDefaults.DEFAULT_PLAIN_BODY_WIDTH);
    }

    public WrappedPlainMessageDialogBody(@NonNull String contents, int width) {
        this.contents = contents;
        this.width = DialogDefaults.clampWidth(width);
    }

    @Override
    @NonNull
    public <D> D adapt(@NonNull DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }

    @Override
    @NonNull
    public WrappedPlainMessageDialogBody replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedPlainMessageDialogBody(operator.apply(this.contents), this.width);
    }
}
