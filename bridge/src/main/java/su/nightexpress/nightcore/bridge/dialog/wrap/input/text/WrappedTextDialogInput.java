package su.nightexpress.nightcore.bridge.dialog.wrap.input.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.Strings;

import java.util.function.UnaryOperator;

public record WrappedTextDialogInput(@NotNull String key,
                                     @NotNull String label,
                                     @NotNull String initial,
                                     boolean labelVisible,
                                     int width,
                                     int maxLength,
                                     @Nullable WrappedMultilineOptions multiline) implements WrappedDialogInput {

    @Override
    @NotNull
    public <I> I adapt(@NotNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NotNull
    public WrappedTextDialogInput replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedTextDialogInput(this.key, operator.apply(this.label), operator.apply(this.initial), this.labelVisible, this.width, this.maxLength, this.multiline);
    }

    public static final class Builder {

        private final String    key;
        private final String label;

        private boolean                 labelVisible = true;
        private String                  initial      = "";
        private int                     width        = DialogDefaults.DEFAULT_TEXT_INPUT_WIDTH;
        private int                     maxLength    = DialogDefaults.DEFAULT_TEXT_INPUT_LENGTH;
        private WrappedMultilineOptions multiline    = null;

        public Builder(@NotNull String key, @NotNull String label) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
        }

        @NotNull
        public Builder width(int width) {
            this.width = DialogDefaults.clampWidth(width);
            return this;
        }

        @NotNull
        public Builder labelVisible(boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        @NotNull
        public Builder initial(String initial) {
            this.initial = initial;
            return this;
        }

        @NotNull
        public Builder maxLength(int maxLength) {
            this.maxLength = Math.max(1, maxLength);
            return this;
        }

        @NotNull
        public Builder multiline(@Nullable WrappedMultilineOptions multiline) {
            this.multiline = multiline;
            return this;
        }

        @NotNull
        public WrappedTextDialogInput build() {
            if (this.initial.length() >= this.maxLength) {
                this.initial = this.initial.substring(0, this.maxLength);
            }

            return new WrappedTextDialogInput(this.key, this.label, this.initial, this.labelVisible, this.width, this.maxLength, this.multiline);
        }
    }
}
