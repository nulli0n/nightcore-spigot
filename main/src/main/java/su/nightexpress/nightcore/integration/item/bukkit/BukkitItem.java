package su.nightexpress.nightcore.integration.item.bukkit;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.util.ItemTag;

@NullMarked
public class BukkitItem extends BaseProviderDataItem<ItemTag> {

    private @Nullable ItemStack cachedStack;
    private boolean             resolved;

    public BukkitItem(ItemTag tag) {
        super(ItemProviderKeys.BUKKIT, tag);
    }

    @Override
    public int getAmount() {
        return this.itemStack().map(ItemStack::getAmount).orElse(0);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isValid() {
        return this.resolve() != null;
    }

    @Override
    public Optional<ItemStack> itemStack() {
        return Optional.ofNullable(this.resolve());
    }

    private @Nullable ItemStack resolve() {
        if (this.resolved) {
            return this.cachedStack == null ? null : new ItemStack(this.cachedStack);
        }

        ItemStack resolve = this.data.getItemStack();
        this.cachedStack = resolve;
        this.resolved = true;

        return resolve == null ? null : new ItemStack(resolve);
    }
}
