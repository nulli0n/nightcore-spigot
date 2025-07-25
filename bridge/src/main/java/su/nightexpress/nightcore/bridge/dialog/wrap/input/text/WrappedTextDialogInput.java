package su.nightexpress.nightcore.bridge.dialog.wrap.input.text;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public record WrappedTextDialogInput(@NotNull String key,
                                     @NotNull NightComponent label,
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

    public static final class Builder {

        private final String    key;
        private final NightComponent label;

        private boolean                 labelVisible = true;
        private String                  initial      = "";
        private int                     width        = 200; // TODO Config
        private int                     maxLength    = 32;
        private WrappedMultilineOptions multiline    = null;

        public Builder(@NotNull String key, @NotNull NightComponent label) {
            this.key = Strings.filterForVariable(key);
            this.label = label;
        }

        @NotNull
        public Builder width(int width) {
            this.width = Math.clamp(width, 1, 1024); // TODO Const
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
            this.maxLength = Math.max(1, maxLength); // TODO Const
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
