package su.nightexpress.nightcore.integration.item.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

public class AdaptedCustomStack extends AdaptedItemStack<ItemIdData> {

    public AdaptedCustomStack(@NotNull ItemAdapter<ItemIdData> adapter, @NotNull ItemIdData data) {
        super(adapter, data);
    }

    @Override
    public boolean isValid() {
        return this.adapter.canHandle(this.data);
    }

    @Override
    public int getAmount() {
        return this.data.getAmount();
    }

    @Override
    @Nullable
    public ItemStack getItemStack() {
        return this.adapter.toItemStack(this.data);
    }

    @Override
    public boolean isSimilar(@NotNull ItemIdData other) {
        return this.data.getItemId().equalsIgnoreCase(other.getItemId());
    }
}
