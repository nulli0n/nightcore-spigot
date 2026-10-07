package su.nightexpress.nightcore.integration.item.itemsadder;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import dev.lone.itemsadder.api.CustomStack;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class ItemsAdderItem extends BaseIdentifiableItem {

    public ItemsAdderItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        if (itemStack.getType().isAir()) return false;

        CustomStack stack = CustomStack.byItemStack(itemStack);
        return stack != null && stack.getNamespacedID().equalsIgnoreCase(this.idData.getItemId());
    }

    @Override
    public boolean isValid() {
        return CustomStack.isInRegistry(this.idData.getItemId());
    }

    @Override
    public Optional<ItemStack> itemStack() {
        CustomStack stack = CustomStack.getInstance(this.idData.getItemId());
        if (stack != null) {
            ItemStack itemStack = stack.getItemStack();
            itemStack.setAmount(this.idData.getAmount());
            return Optional.of(itemStack);
        }
        return Optional.empty();
    }
}
