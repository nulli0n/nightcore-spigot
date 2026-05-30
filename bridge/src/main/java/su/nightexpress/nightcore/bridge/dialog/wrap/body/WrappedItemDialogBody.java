package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;

import java.util.function.UnaryOperator;

public record WrappedItemDialogBody(@NonNull ItemStack item,
                                    @Nullable WrappedPlainMessageDialogBody description,
                                    boolean showDecorations,
                                    boolean showTooltip,
                                    int width,
                                    int height) implements WrappedDialogBody {

    @Override
    @NonNull
    public <D> D adapt(@NonNull DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }

    @Override
    @NonNull
    public WrappedItemDialogBody replace(@NonNull UnaryOperator<String> operator) {
        return new WrappedItemDialogBody(new ItemStack(this.item), this.description == null ? null : this.description
            .replace(operator), this.showDecorations, this.showTooltip, this.width, this.height);
    }

    public static final class Builder {

        private final ItemStack item;

        private WrappedPlainMessageDialogBody description;

        private boolean showDecorations = true;
        private boolean showTooltip     = true;
        private int     width           = DialogDefaults.DEFAULT_ITEM_BODY_WIDTH;
        private int     height          = DialogDefaults.DEFAULT_ITEM_BODY_HEIGHT;

        public Builder(@NonNull ItemStack item) {
            this.item = item;
        }

        @NonNull
        public Builder description(@Nullable WrappedPlainMessageDialogBody description) {
            this.description = description;
            return this;
        }

        @NonNull
        public Builder showDecorations(boolean showDecorations) {
            this.showDecorations = showDecorations;
            return this;
        }

        @NonNull
        public Builder showTooltip(boolean showTooltip) {
            this.showTooltip = showTooltip;
            return this;
        }

        @NonNull
        public Builder width(int width) {
            this.width = Math.clamp(width, DialogDefaults.MIN_WIDTH, DialogDefaults.MAX_ITEM_BODY_WIDTH);
            return this;
        }

        @NonNull
        public Builder height(int height) {
            this.height = Math.clamp(height, DialogDefaults.MIN_WIDTH, DialogDefaults.MAX_ITEM_BODY_HEIGHT);
            return this;
        }

        @NonNull
        public WrappedItemDialogBody build() {
            return new WrappedItemDialogBody(this.item, this.description, this.showDecorations, this.showTooltip, this.width, this.height);
        }
    }
}
