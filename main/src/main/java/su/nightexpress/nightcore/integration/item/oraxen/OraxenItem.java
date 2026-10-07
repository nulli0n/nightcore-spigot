package su.nightexpress.nightcore.integration.item.oraxen;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class OraxenItem extends BaseIdentifiableItem {

    public OraxenItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        String itemId = OraxenItems.getIdByItem(itemStack);
        return itemId != null && itemId.equalsIgnoreCase(this.idData.getItemId());
    }

    @Override
    public boolean isValid() {
        return OraxenItems.exists(this.idData.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ItemBuilder builder = OraxenItems.getItemById(this.idData.getItemId());
        if (builder != null) {
            builder.setAmount(this.idData.getAmount());
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }

}
