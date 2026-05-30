package su.nightexpress.nightcore.bridge.dialog.wrap.button;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogButtonAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;

import java.util.function.UnaryOperator;

public record WrappedActionButton(@NonNull String label,
                                  @Nullable String tooltip,
                                  int width,
                                  @Nullable WrappedDialogAction action) {

    @NonNull
    public <B> B adapt(@NonNull DialogButtonAdapter<B> adapter) {
        return adapter.adaptButton(this);
    }

    @NonNull
    public WrappedActionButton replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedActionButton(operator.apply(this.label), this.tooltip == null ? null : operator.apply(
            this.tooltip), this.width, this.action);
    }

    public static final class Builder {

        private final String label;

        private String              tooltip;
        private int                 width = DialogDefaults.DEFAULT_BUTTON_WIDTH;
        private WrappedDialogAction action;

        public Builder(@NonNull String label) {
            this(label, null);
        }

        public Builder(@NonNull String label, @Nullable String tooltip) {
            this.label = label;
            this.tooltip(tooltip);
        }

        @NonNull
        public Builder tooltip(@Nullable String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @NonNull
        public Builder width(int width) {
            this.width = DialogDefaults.clampWidth(width);
            return this;
        }

        @NonNull
        public Builder action(@Nullable WrappedDialogAction action) {
            this.action = action;
            return this;
        }

        @NonNull
        public WrappedActionButton build() {
            return new WrappedActionButton(this.label, this.tooltip, this.width, this.action);
        }
    }
}
