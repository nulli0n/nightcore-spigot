package su.nightexpress.nightcore.integration.item.adapter.impl;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class NexoAdapter extends IdentifiableItemAdapter {

    public NexoAdapter() {
        super("nexo");
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        ItemBuilder builder = NexoItems.itemFromId(itemId);
        return builder == null ? null : builder.build();
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return NexoItems.exists(itemStack);
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return NexoItems.exists(data.getItemId());
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack item) {
        return NexoItems.idFromItem(item);
    }
}
