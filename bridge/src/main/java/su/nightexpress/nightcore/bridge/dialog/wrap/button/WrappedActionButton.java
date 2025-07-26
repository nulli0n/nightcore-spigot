package su.nightexpress.nightcore.bridge.dialog.wrap.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogButtonAdapter;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public record WrappedActionButton(@NotNull NightComponent label,
                                  @Nullable NightComponent tooltip,
                                  int width,
                                  @Nullable WrappedDialogAction action) {

    @NotNull
    public <B> B adapt(@NotNull DialogButtonAdapter<B> adapter) {
        return adapter.adaptButton(this);
    }

    public static final class Builder {

        private final NightComponent label;

        private NightComponent      tooltip;
        private int                 width = 150; // TODO Config
        private WrappedDialogAction action;

        public Builder(@NotNull NightComponent label) {
            this(label, null);
        }

        public Builder(@NotNull NightComponent label, @Nullable NightComponent tooltip) {
            this.label = label;
            this.tooltip(tooltip);
        }

        @NotNull
        public Builder tooltip(@Nullable NightComponent tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @NotNull
        public Builder width(int width) {
            this.width = Math.clamp(width, 1, 1024); // TODO Const
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
