package su.nightexpress.nightcore.integration.item.nexo;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;

import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class NexoItem extends BaseProviderDataItem<ItemIdData> {

    public NexoItem(ItemIdData idData) {
        super(ItemProviderKeys.NEXO, idData);
    }

    @Override
    public int getAmount() {
        return this.data.getAmount();
    }

    @Override
    public boolean isValid() {
        return NexoItems.exists(this.data.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ItemBuilder builder = NexoItems.itemFromId(this.data.getItemId());
        if (builder != null) {
            ItemStack itemStack = builder.build();
            itemStack.setAmount(this.data.getAmount());
            return Optional.of(itemStack);
        }

        return Optional.empty();
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        String itemId = NexoItems.idFromItem(itemStack);
        return itemId != null && itemId.equalsIgnoreCase(this.data.getItemId());
    }
}
