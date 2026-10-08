package su.nightexpress.nightcore.integration.item.oraxen;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class OraxenItem extends BaseProviderDataItem<ItemIdData> {

    public OraxenItem(ItemIdData idData) {
        super(ItemProviderKeys.ORAXEN, idData);
    }

    @Override
    public int getAmount() {
        return this.data.getAmount();
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        String itemId = OraxenItems.getIdByItem(itemStack);
        return itemId != null && itemId.equalsIgnoreCase(this.data.getItemId());
    }

    @Override
    public boolean isValid() {
        return OraxenItems.exists(this.data.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ItemBuilder builder = OraxenItems.getItemById(this.data.getItemId());
        if (builder != null) {
            builder.setAmount(this.data.getAmount());
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }

}
