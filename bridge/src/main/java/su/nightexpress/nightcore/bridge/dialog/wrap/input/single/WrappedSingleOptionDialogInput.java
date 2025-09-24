package su.nightexpress.nightcore.bridge.dialog.wrap.input.single;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.Strings;

import java.util.List;
import java.util.function.UnaryOperator;

public record WrappedSingleOptionDialogInput(@NotNull String key,
                                             @NotNull String label,
                                             @NotNull List<WrappedSingleOptionEntry> entries,
                                             int width,
                                             boolean labelVisible) implements WrappedDialogInput {

    @Override
    @NotNull
    public <I> I adapt(@NotNull DialogInputAdapter<I> adapter) {
        return adapter.adaptInput(this);
    }

    @Override
    @NotNull
    public WrappedSingleOptionDialogInput replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedSingleOptionDialogInput(this.key, operator.apply(this.label), List.copyOf(this.entries), this.width, this.labelVisible);
    }

    public static final class Builder {

        private final String                         key;
        private final List<WrappedSingleOptionEntry> entries;
        private final String                         label;

        private int     width        = DialogDefaults.DEFAULT_SINGLE_INPUT_WIDTH;
        private boolean labelVisible = true;

        public Builder(@NotNull String key, @NotNull String label, @NotNull List<WrappedSingleOptionEntry> entries) {
            this.key = Strings.filterForVariable(key);
            this.entries = entries;
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
        public WrappedSingleOptionDialogInput build() {
            return new WrappedSingleOptionDialogInput(this.key, this.label, this.entries, this.width, this.labelVisible);
        }
    }
}
