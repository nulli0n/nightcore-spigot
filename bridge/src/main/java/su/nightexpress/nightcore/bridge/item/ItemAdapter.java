package su.nightexpress.nightcore.bridge.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ItemAdapter<T> {

    @NotNull String getName();

    boolean canHandle(@NotNull ItemStack itemStack);

    boolean canHandle(@NotNull T data);

    @NotNull Optional<? extends AdaptedItem> adapt(@NotNull ItemStack itemStack);

    @Nullable ItemStack toItemStack(@NotNull T data);

    @Nullable T fromItemStack(@NotNull ItemStack itemStack);

    boolean isVanilla();

    int getWeight();
}
