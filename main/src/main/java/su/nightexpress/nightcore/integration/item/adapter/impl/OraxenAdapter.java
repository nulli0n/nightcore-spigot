package su.nightexpress.nightcore.integration.item.adapter.impl;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class OraxenAdapter extends IdentifiableItemAdapter {

    public OraxenAdapter() {
        super("oraxen");
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        ItemBuilder builder = OraxenItems.getItemById(itemId);
        return builder == null ? null : builder.build();
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return OraxenItems.exists(itemStack);
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return OraxenItems.exists(data.getItemId());
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        return OraxenItems.getIdByItem(itemStack);
    }
}
