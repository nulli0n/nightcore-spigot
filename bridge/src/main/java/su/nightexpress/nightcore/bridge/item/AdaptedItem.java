package su.nightexpress.nightcore.bridge.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AdaptedItem {

    @NotNull ItemAdapter<?> getAdapter();

    @Nullable ItemStack getItemStack();

    boolean isSimilar(@NotNull ItemStack other);

    boolean isValid();

    int getAmount();
}
