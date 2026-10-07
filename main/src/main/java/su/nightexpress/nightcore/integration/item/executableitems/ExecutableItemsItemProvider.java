package su.nightexpress.nightcore.integration.item.executableitems;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class ExecutableItemsItemProvider extends BaseIdentifiableItemProvider {

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new ExecutableItemsItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        String itemId = ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(itemStack)
            .map(ExecutableItemInterface::getId)
            .orElse(null);

        if (itemId == null) {
            return Optional.empty();
        }

        return Optional.of(new ItemIdData(itemId, itemStack.getAmount()));
    }

    @Override
    public String getId() {
        return ItemProviderKeys.EXECUTABLE_ITEMS;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("ExecutableItems");
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(itemStack)
            .isPresent();
    }
}
