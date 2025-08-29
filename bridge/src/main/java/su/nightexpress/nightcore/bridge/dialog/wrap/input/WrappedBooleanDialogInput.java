package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.util.Strings;

import java.util.function.UnaryOperator;

public record WrappedBooleanDialogInput(@NotNull String key,
                                        @NotNull String label,
                                        boolean initial,
                                        @NotNull String onTrue,
                                        @NotNull String onFalse) implements WrappedDialogInput {

    @Override
    @NotNull
    public <I> I adapt(@NotNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NotNull
    public WrappedBooleanDialogInput replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedBooleanDialogInput(this.key, operator.apply(this.label), this.initial, this.onTrue, this.onFalse);
    }

    public static final class Builder {

        private final String key;
        private final String label;

        private boolean initial = false;
        private String  onTrue  = "true";
        private String  onFalse = "false";

        public Builder(@NotNull String key, @NotNull String label) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
        }

        @NotNull
        public Builder initial(boolean initial) {
            this.initial = initial;
            return this;
        }

        @NotNull
        public Builder onTrue(@NotNull String onTrue) {
            this.onTrue = onTrue;
            return this;
        }

        @NotNull
        public Builder onFalse(@NotNull String onFalse) {
            this.onFalse = onFalse;
            return this;
        }

        @NotNull
        public WrappedBooleanDialogInput build() {
            return new WrappedBooleanDialogInput(this.key, this.label, this.initial, this.onTrue, this.onFalse);
        }
    }
}
