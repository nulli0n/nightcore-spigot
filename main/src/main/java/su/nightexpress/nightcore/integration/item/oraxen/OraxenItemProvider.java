package su.nightexpress.nightcore.integration.item.oraxen;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import io.th0rgal.oraxen.api.OraxenItems;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class OraxenItemProvider extends BaseIdentifiableItemProvider {

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new OraxenItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        String itemId = OraxenItems.getIdByItem(itemStack);
        if (itemId != null) {
            return Optional.of(new ItemIdData(itemId, itemStack.getAmount()));
        }
        return Optional.empty();
    }

    @Override
    public String getId() {
        return ItemProviderKeys.ORAXEN;
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return OraxenItems.getIdByItem(itemStack) != null;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("Oraxen");
    }
}
