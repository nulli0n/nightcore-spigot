package su.nightexpress.nightcore.integration.item.adapter.impl;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

public class CraftEngineAdapter extends IdentifiableItemAdapter {

    public CraftEngineAdapter() {
        super("craftengine");
    }

    @Override
    @Nullable
    public String getItemId(@NonNull ItemStack itemStack) {
        Key itemId = CraftEngineItems.getCustomItemId(itemStack);
        return itemId != null ? itemId.asString() : null;
    }

    @Override
    @Nullable
    public ItemStack createItem(@NonNull String itemId) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.of(itemId));
        return customItem != null ? customItem.buildItemStack() : null;
    }

    @Override
    public boolean canHandle(@NonNull ItemStack itemStack) {
        return CraftEngineItems.isCustomItem(itemStack);
    }

    @Override
    public boolean canHandle(@NonNull ItemIdData data) {
        return CraftEngineItems.byId(Key.of(data.getItemId())) != null;
    }
}
