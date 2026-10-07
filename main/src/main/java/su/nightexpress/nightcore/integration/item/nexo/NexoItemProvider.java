package su.nightexpress.nightcore.integration.item.nexo;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.nexomc.nexo.api.NexoItems;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class NexoItemProvider extends BaseIdentifiableItemProvider {

    @Override
    public String getId() {
        return ItemProviderKeys.NEXO;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("nexo");
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return NexoItems.idFromItem(itemStack) != null;
    }

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new NexoItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        String itemId = NexoItems.idFromItem(itemStack);
        int amount = itemStack.getAmount();

        return itemId != null ? Optional.of(new ItemIdData(itemId, amount)) : Optional.empty();
    }
}
