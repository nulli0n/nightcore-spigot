package su.nightexpress.nightcore.integration.item.adapter.impl;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

public class CraftEngineAdapter extends IdentifiableItemAdapter {

    public CraftEngineAdapter() {
        super("craftengine");
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        Key itemId = CraftEngineItems.getCustomItemId(itemStack);
        return itemId != null ? itemId.asString() : null;
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.of(itemId));
        return customItem != null ? customItem.buildItemStack() : null;
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return CraftEngineItems.isCustomItem(itemStack);
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return CraftEngineItems.byId(Key.of(data.getItemId())) != null;
    }
}
