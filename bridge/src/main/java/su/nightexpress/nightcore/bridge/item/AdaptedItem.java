package su.nightexpress.nightcore.bridge.item;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface AdaptedItem {

    default @Nullable ItemStack getItemStack() {
        return itemStack().orElse(null);
    }

    Optional<ItemStack> itemStack();

    @Deprecated
    default boolean isSimilar(ItemStack other) {
        return this.isChildren(other);
    }

    boolean isChildren(ItemStack itemStack);

    boolean isValid();
}
