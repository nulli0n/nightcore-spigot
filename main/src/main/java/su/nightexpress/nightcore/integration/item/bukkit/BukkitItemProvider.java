package su.nightexpress.nightcore.integration.item.bukkit;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.ItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.util.ItemTag;

@NullMarked
public class BukkitItemProvider implements ItemProvider {

    public static final BukkitItemProvider INSTANCE = new BukkitItemProvider();

    @Override
    public String getId() {
        return ItemProviderKeys.BUKKIT;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return true;
    }

    @Override
    public Optional<AdaptedItem> readItem(FileConfig config, String path) {
        ItemTag tag = ItemTag.read(config, path + ".Data");
        return Optional.of(new BukkitItem(tag));
    }

    @Override
    public Optional<AdaptedItem> wrapItem(ItemStack itemStack) {
        ItemTag tag = ItemTag.of(itemStack);
        return Optional.of(new BukkitItem(tag));
    }
}
