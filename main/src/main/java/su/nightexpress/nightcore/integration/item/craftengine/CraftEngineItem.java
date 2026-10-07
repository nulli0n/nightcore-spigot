package su.nightexpress.nightcore.integration.item.craftengine;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitItemDefinition;
import net.momirealms.craftengine.core.util.Key;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class CraftEngineItem extends BaseIdentifiableItem {

    public CraftEngineItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        Key itemId = CraftEngineItems.getCustomItemId(itemStack);
        return itemId != null && itemId.asString().equalsIgnoreCase(this.idData.getItemId());
    }

    @Override
    public boolean isValid() {
        return CraftEngineItems.byId(Key.of(this.idData.getItemId())) != null;
    }

    @Override
    public Optional<ItemStack> itemStack() {
        BukkitItemDefinition customItem = CraftEngineItems.byId(Key.of(this.idData.getItemId()));
        if (customItem != null) {
            ItemStack stack = customItem.buildBukkitItem();
            stack.setAmount(this.idData.getAmount());
            return Optional.of(stack);
        }

        return Optional.empty();
    }
}
