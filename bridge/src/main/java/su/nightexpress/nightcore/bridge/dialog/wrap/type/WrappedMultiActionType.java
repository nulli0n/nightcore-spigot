package su.nightexpress.nightcore.bridge.dialog.wrap.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogTypeAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

import java.util.List;
import java.util.function.UnaryOperator;

public record WrappedMultiActionType(@NotNull List<WrappedActionButton> actions,
                                     @Nullable WrappedActionButton exitAction,
                                     int columns) implements WrappedDialogType {

    @Override
    @NotNull
    public <T> T adapt(@NotNull DialogTypeAdapter<T> factory) {
        return factory.adaptType(this);
    }

    @Override
    @NotNull
    public WrappedMultiActionType replace(@NotNull UnaryOperator<String> operator) {
        List<WrappedActionButton> actions = this.actions.stream().map(other -> other.replace(operator)).toList();
        WrappedActionButton exitAction = this.exitAction == null ? null : this.exitAction.replace(operator);

        return new WrappedMultiActionType(actions, exitAction, this.columns);
    }

    public static final class Builder {

        private final List<WrappedActionButton> actions;

        private WrappedActionButton exitAction = null;
        private int                 columns    = 2;

        public Builder(@NotNull List<WrappedActionButton> actions) {
            this.actions = actions;
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
        public WrappedMultiActionType build() {
            return new WrappedMultiActionType(this.actions, this.exitAction, this.columns);
        }
    }
}
