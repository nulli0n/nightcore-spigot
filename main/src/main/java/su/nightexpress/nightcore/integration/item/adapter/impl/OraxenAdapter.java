package su.nightexpress.nightcore.integration.item.adapter.impl;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class OraxenAdapter extends IdentifiableItemAdapter {

    public OraxenAdapter() {
        super("oraxen");
    }

    @Override
    @Nullable
    public ItemStack createItem(@NonNull String itemId) {
        ItemBuilder builder = OraxenItems.getItemById(itemId);
        return builder == null ? null : builder.build();
    }

    @Override
    public boolean canHandle(@NonNull ItemStack itemStack) {
        return OraxenItems.exists(itemStack);
    }

    @Override
    public boolean canHandle(@NonNull ItemIdData data) {
        return OraxenItems.exists(data.getItemId());
    }

    @Override
    @Nullable
    public String getItemId(@NonNull ItemStack itemStack) {
        return OraxenItems.getIdByItem(itemStack);
    }
}
