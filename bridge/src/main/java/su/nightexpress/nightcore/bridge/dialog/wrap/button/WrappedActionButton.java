package su.nightexpress.nightcore.bridge.dialog.wrap.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogButtonAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;

import java.util.function.UnaryOperator;

public record WrappedActionButton(@NotNull String label,
                                  @Nullable String tooltip,
                                  int width,
                                  @Nullable WrappedDialogAction action) {

    @NotNull
    public <B> B adapt(@NotNull DialogButtonAdapter<B> adapter) {
        return adapter.adaptButton(this);
    }

    @NotNull
    public WrappedActionButton replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedActionButton(operator.apply(this.label), this.tooltip == null ? null : operator.apply(this.tooltip), this.width, this.action);
    }

    public static final class Builder {

        private final String label;

        private String              tooltip;
        private int                 width = DialogDefaults.DEFAULT_BUTTON_WIDTH;
        private WrappedDialogAction action;

        public Builder(@NotNull String label) {
            this(label, null);
        }

        public Builder(@NotNull String label, @Nullable String tooltip) {
            this.label = label;
            this.tooltip(tooltip);
        }

        @NotNull
        public Builder tooltip(@Nullable String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @NotNull
        public Builder width(int width) {
            this.width = DialogDefaults.clampWidth(width);
            return this;
        }

        @NotNull
        public Builder action(@Nullable WrappedDialogAction action) {
            this.action = action;
            return this;
        }

        @NotNull
        public WrappedActionButton build() {
            return new WrappedActionButton(this.label, this.tooltip, this.width, this.action);
        }
    }
}
