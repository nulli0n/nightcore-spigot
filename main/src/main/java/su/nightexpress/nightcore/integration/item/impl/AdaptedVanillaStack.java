package su.nightexpress.nightcore.integration.item.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.adapter.impl.VanillaItemAdapter;
import su.nightexpress.nightcore.util.ItemTag;

public class AdaptedVanillaStack extends AdaptedItemStack<ItemTag> {

    private final ItemStack itemStack;

    public AdaptedVanillaStack(@NotNull ItemTag itemTag) {
        super(VanillaItemAdapter.INSTANCE, itemTag);
        this.itemStack = this.adapter.toItemStack(itemTag);
    }

    @NotNull
    public static AdaptedVanillaStack of(@NotNull ItemStack itemStack) {
        ItemTag tag = ItemTag.of(itemStack);
        return new AdaptedVanillaStack(tag);
    }

    @Override
    public boolean isValid() {
        return this.itemStack != null;
    }

    @Override
    public int getAmount() {
        return this.itemStack == null ? 0 : this.itemStack.getAmount();
    }

    @Override
    @Nullable
    public ItemStack getItemStack() {
        return this.itemStack == null ? null : new ItemStack(this.itemStack);
    }

    @Override
    public boolean isSimilar(@NotNull ItemTag other) {
        if (this.itemStack == null) return false;

        ItemStack otherStack = this.adapter.toItemStack(other);
        return otherStack != null && otherStack.isSimilar(this.itemStack);
    }
}
