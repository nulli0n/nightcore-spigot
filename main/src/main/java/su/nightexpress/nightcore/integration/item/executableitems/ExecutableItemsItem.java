package su.nightexpress.nightcore.integration.item.executableitems;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;

import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class ExecutableItemsItem extends BaseIdentifiableItem {

    public ExecutableItemsItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        ExecutableItemInterface itemInterface = ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(itemStack)
            .orElse(null);

        return itemInterface != null && itemInterface.getId().equalsIgnoreCase(this.idData.getItemId());
    }

    @Override
    public boolean isValid() {
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(this.idData.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ExecutableItemInterface itemInterface = ExecutableItemsAPI.getExecutableItemsManager()
            .getExecutableItem(this.idData.getItemId())
            .orElse(null);
        if (itemInterface == null) {
            return Optional.empty();
        }

        return Optional.of(itemInterface.buildItem(this.idData.getAmount(), Optional.empty()));
    }
}
