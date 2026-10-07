package su.nightexpress.nightcore.integration.item.craftengine;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.util.Key;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class CraftEngineItemProvider extends BaseIdentifiableItemProvider {

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return CraftEngineItems.isCustomItem(itemStack);
    }

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new CraftEngineItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        Key itemId = CraftEngineItems.getCustomItemId(itemStack);
        if (itemId != null) {
            return Optional.of(new ItemIdData(itemId.asString(), itemStack.getAmount()));
        }
        return Optional.empty();
    }

    @Override
    public String getId() {
        return ItemProviderKeys.CRAFT_ENGINE;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("CraftEngine");
    }
}
