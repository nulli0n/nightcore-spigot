package su.nightexpress.nightcore.integration.item.itemsadder;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomStack;
import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class ItemsAdderItem extends BaseProviderDataItem<ItemIdData> {

    public ItemsAdderItem(ItemIdData idData) {
        super(ItemProviderKeys.ITEMS_ADDER, idData);
    }

    @Override
    public int getAmount() {
        return this.data.getAmount();
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        if (itemStack.getType().isAir()) return false;

        CustomStack stack = CustomStack.byItemStack(itemStack);
        return stack != null && stack.getNamespacedID().equalsIgnoreCase(this.data.getItemId());
    }

    @Override
    public boolean isValid() {
        return CustomStack.isInRegistry(this.data.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        CustomStack stack = CustomStack.getInstance(this.data.getItemId());
        if (stack != null) {
            ItemStack itemStack = stack.getItemStack();
            itemStack.setAmount(this.data.getAmount());
            return Optional.of(itemStack);
        }
        return Optional.empty();
    }
}
