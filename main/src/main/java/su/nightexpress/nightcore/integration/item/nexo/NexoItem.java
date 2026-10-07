package su.nightexpress.nightcore.integration.item.nexo;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;

import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class NexoItem extends BaseIdentifiableItem {

    public NexoItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isValid() {
        return NexoItems.exists(this.idData.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ItemBuilder builder = NexoItems.itemFromId(this.idData.getItemId());
        if (builder != null) {
            ItemStack itemStack = builder.build();
            itemStack.setAmount(this.idData.getAmount());
            return Optional.of(itemStack);
        }

        return Optional.empty();
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        String itemId = NexoItems.idFromItem(itemStack);
        return itemId != null && itemId.equalsIgnoreCase(this.idData.getItemId());
    }
}
