package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.List;
import java.util.function.UnaryOperator;

public record WrappedDialogListType(@NotNull List<WrappedDialog> dialogs,
                                    @Nullable WrappedActionButton exitAction,
                                    int columns,
                                    int buttonWidth) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NotNull
    public WrappedDialogListType replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedDialogListType(List.copyOf(this.dialogs), this.exitAction == null ? null : this.exitAction.replace(operator), this.columns, this.buttonWidth);
    }

    public static final class Builder {

        private final List<WrappedDialog> dialogs;

        private WrappedActionButton exitAction;
        private int                 columns     = 2;
        private int                 buttonWidth = 150; // TODO Config

        public Builder(@NotNull List<WrappedDialog> dialogs) {
            this.dialogs = dialogs;
        }

        @NotNull
        public Builder exitAction(@Nullable WrappedActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        @NotNull
        public Builder columns(int columns) {
            this.columns = Math.max(1, columns);
            return this;
        }

        @NotNull
        public Builder buttonWidth(int buttonWidth) {
            this.buttonWidth = Math.clamp(buttonWidth, 1, 1024); // TODO Const
            return this;
        }

        @NotNull
        public WrappedDialogListType build() {
            return new WrappedDialogListType(this.dialogs, this.exitAction, this.columns, this.buttonWidth);
        }
    }
}
