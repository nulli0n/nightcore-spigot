package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public record WrappedBooleanDialogInput(@NotNull String key,
                                        @NotNull NightComponent label,
                                        boolean initial,
                                        @NotNull String onTrue,
                                        @NotNull String onFalse) implements WrappedDialogInput {

    @Override
    @NotNull
    public <I> I adapt(@NotNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    public static final class Builder {

        private final String         key;
        private final NightComponent label;

        private boolean initial = false;
        private String  onTrue  = "true";
        private String  onFalse = "false";

        public Builder(@NotNull String key, @NotNull NightComponent label) {
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
