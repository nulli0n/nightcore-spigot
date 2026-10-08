package su.nightexpress.nightcore.integration.item;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public abstract class BaseIdentifiableItemProvider implements ItemProvider {

    protected abstract AdaptedItem createItem(ItemIdData idData);

    protected abstract Optional<ItemIdData> extractIdData(ItemStack itemStack);

    @Override
    public Optional<AdaptedItem> wrapItem(ItemStack itemStack) {
        Optional<ItemIdData> idData = this.extractIdData(itemStack);
        return idData.map(this::createItem);
    }
}
