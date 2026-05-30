package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.List;
import java.util.function.UnaryOperator;

public record WrappedDialogListType(@NonNull List<WrappedDialog> dialogs,
                                    @Nullable WrappedActionButton exitAction,
                                    int columns,
                                    int buttonWidth) implements WrappedDialogType {

    @Override
    @NonNull
    public <T> T adapt(@NonNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NonNull
    public WrappedDialogListType replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedDialogListType(List.copyOf(this.dialogs), this.exitAction == null ? null : this.exitAction
            .replace(operator), this.columns, this.buttonWidth);
    }

    public static final class Builder {

        private final List<WrappedDialog> dialogs;

        private WrappedActionButton exitAction;
        private int                 columns     = 2;
        private int                 buttonWidth = DialogDefaults.DEFAULT_BUTTON_WIDTH;

        public Builder(@NonNull List<WrappedDialog> dialogs) {
            this.dialogs = dialogs;
        }

        @NonNull
        public Builder exitAction(@Nullable WrappedActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        @NonNull
        public Builder columns(int columns) {
            this.columns = Math.max(1, columns);
            return this;
        }

        @NonNull
        public Builder buttonWidth(int buttonWidth) {
            this.buttonWidth = DialogDefaults.clampWidth(buttonWidth);
            return this;
        }

        @NonNull
        public WrappedDialogListType build() {
            return new WrappedDialogListType(this.dialogs, this.exitAction, this.columns, this.buttonWidth);
        }
    }
}
