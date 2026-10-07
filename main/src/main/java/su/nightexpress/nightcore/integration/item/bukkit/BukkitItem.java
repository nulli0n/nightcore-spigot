package su.nightexpress.nightcore.integration.item.bukkit;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemTag;

@NullMarked
public class BukkitItem implements AdaptedItem {

    private final ItemTag tag;

    private @Nullable ItemStack cachedStack;
    private boolean             resolved;

    public BukkitItem(ItemTag tag) {
        this.tag = tag;
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

        ItemStack resolve = this.tag.getItemStack();
        this.cachedStack = resolve;
        this.resolved = true;

        return resolve == null ? null : new ItemStack(resolve);
    }
}
