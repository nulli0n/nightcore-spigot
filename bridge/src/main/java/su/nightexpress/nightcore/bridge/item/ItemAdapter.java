package su.nightexpress.nightcore.bridge.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface ItemAdapter<T> {

    @NonNull
    String getName();

    boolean canHandle(@NonNull ItemStack itemStack);

    boolean canHandle(@NonNull T data);

    @NonNull
    Optional<? extends AdaptedItem> adapt(@NonNull ItemStack itemStack);

    @Nullable
    ItemStack toItemStack(@NonNull T data);

    @Nullable
    T fromItemStack(@NonNull ItemStack itemStack);

    boolean isVanilla();

    int getWeight();
}
