package su.nightexpress.nightcore.integration.item.executableitems;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;

import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class ExecutableItemsItem extends BaseProviderDataItem<ItemIdData> {

    public ExecutableItemsItem(ItemIdData idData) {
        super(ItemProviderKeys.EXECUTABLE_ITEMS, idData);
    }

    @Override
    public int getAmount() {
        return this.data.getAmount();
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        ExecutableItemInterface itemInterface = ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(itemStack)
            .orElse(null);

        return itemInterface != null && itemInterface.getId().equalsIgnoreCase(this.data.getItemId());
    }

    @Override
    public boolean isValid() {
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(this.data.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ExecutableItemInterface itemInterface = ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(this.data.getItemId())
            .orElse(null);
        if (itemInterface == null) {
            return Optional.empty();
        }

        return Optional.of(itemInterface.buildItem(this.data.getAmount(), Optional.empty()));
    }
}
