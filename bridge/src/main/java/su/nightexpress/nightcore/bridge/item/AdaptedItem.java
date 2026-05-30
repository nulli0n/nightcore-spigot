package su.nightexpress.nightcore.bridge.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface AdaptedItem {

    @NonNull
    ItemAdapter<?> getAdapter();

    @Nullable
    ItemStack getItemStack();

    @NonNull
    Optional<ItemStack> itemStack();

    boolean isSimilar(@NonNull ItemStack other);

    boolean isValid();

    int getAmount();
}
