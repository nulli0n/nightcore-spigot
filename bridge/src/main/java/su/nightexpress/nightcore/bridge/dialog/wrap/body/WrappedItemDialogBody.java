package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public record WrappedItemDialogBody(@NotNull ItemStack item,
                                    @Nullable WrappedPlainMessageDialogBody description,
                                    boolean showDecorations,
                                    boolean showTooltip,
                                    int width,
                                    int height) implements WrappedDialogBody {

    @Override
    @NotNull
    public <D> D adapt(@NotNull DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }

    @Override
    @NotNull
    public WrappedItemDialogBody replace(@NotNull UnaryOperator<String> operator) {
        return new WrappedItemDialogBody(new ItemStack(this.item), this.description == null ? null : this.description.replace(operator), this.showDecorations, this.showTooltip, this.width, this.height);
    }

    public static final class Builder {

        private final ItemStack item;

        private WrappedPlainMessageDialogBody description;

        private boolean showDecorations = true;
        private boolean showTooltip     = true;
        private int     width           = 16; // TODO Config
        private int     height          = 16;

        public Builder(@NotNull ItemStack item) {
            this.item = item;
        }

        @NotNull
        public Builder description(@Nullable WrappedPlainMessageDialogBody description) {
            this.description = description;
            return this;
        }

        @NotNull
        public Builder showDecorations(boolean showDecorations) {
            this.showDecorations = showDecorations;
            return this;
        }

        @NotNull
        public Builder showTooltip(boolean showTooltip) {
            this.showTooltip = showTooltip;
            return this;
        }

        @NotNull
        public Builder width(int width) {
            this.width = Math.clamp(width, 1, 256); // TODO Const
            return this;
        }

        @NotNull
        public Builder height(int height) {
            this.height = Math.clamp(height, 1, 256); // TODO Const
            return this;
        }

        @NotNull
        public WrappedItemDialogBody build() {
            return new WrappedItemDialogBody(this.item, this.description, this.showDecorations, this.showTooltip, this.width, this.height);
        }
    }
}
