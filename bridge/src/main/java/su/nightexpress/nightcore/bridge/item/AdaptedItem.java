package su.nightexpress.nightcore.bridge.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface AdaptedItem {

    @NotNull ItemAdapter<?> getAdapter();

    @Nullable ItemStack getItemStack();

    @NotNull Optional<ItemStack> itemStack();

    boolean isSimilar(@NotNull ItemStack other);

    boolean isValid();

    int getAmount();
}
