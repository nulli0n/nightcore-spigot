package su.nightexpress.nightcore.integration.item.itemsadder;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomStack;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class ItemsAdderItemProvider extends BaseIdentifiableItemProvider {

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new ItemsAdderItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        if (customStack != null) {
            return Optional.of(new ItemIdData(customStack.getNamespacedID(), itemStack.getAmount()));
        }

        return Optional.empty();
    }

    @Override
    public String getId() {
        return ItemProviderKeys.ITEMS_ADDER;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("ItemsAdder");
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        return customStack != null;
    }
}
