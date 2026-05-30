package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.util.Strings;

import java.util.function.UnaryOperator;

public record WrappedBooleanDialogInput(@NonNull String key,
                                        @NonNull String label,
                                        boolean initial,
                                        @NonNull String onTrue,
                                        @NonNull String onFalse) implements WrappedDialogInput {

    @Override
    @NonNull
    public <I> I adapt(@NonNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NonNull
    public WrappedBooleanDialogInput replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedBooleanDialogInput(this.key, operator.apply(
            this.label), this.initial, this.onTrue, this.onFalse);
    }

    public static final class Builder {

        private final String key;
        private final String label;

        private boolean initial = false;
        private String  onTrue  = "true";
        private String  onFalse = "false";

        public Builder(@NonNull String key, @NonNull String label) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
        }

        @NonNull
        public Builder initial(boolean initial) {
            this.initial = initial;
            return this;
        }

        @NonNull
        public Builder onTrue(@NonNull String onTrue) {
            this.onTrue = onTrue;
            return this;
        }

        @NonNull
        public Builder onFalse(@NonNull String onFalse) {
            this.onFalse = onFalse;
            return this;
        }

        @NonNull
        public WrappedBooleanDialogInput build() {
            return new WrappedBooleanDialogInput(this.key, this.label, this.initial, this.onTrue, this.onFalse);
        }
    }
}
