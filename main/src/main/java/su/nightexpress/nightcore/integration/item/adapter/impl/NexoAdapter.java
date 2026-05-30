package su.nightexpress.nightcore.integration.item.adapter.impl;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class NexoAdapter extends IdentifiableItemAdapter {

    public NexoAdapter() {
        super("nexo");
    }

    @Override
    @Nullable
    public ItemStack createItem(@NonNull String itemId) {
        ItemBuilder builder = NexoItems.itemFromId(itemId);
        return builder == null ? null : builder.build();
    }

    @Override
    public boolean canHandle(@NonNull ItemStack itemStack) {
        return NexoItems.exists(itemStack);
    }

    @Override
    public boolean canHandle(@NonNull ItemIdData data) {
        return NexoItems.exists(data.getItemId());
    }

    @Override
    @Nullable
    public String getItemId(@NonNull ItemStack item) {
        return NexoItems.idFromItem(item);
    }
}
