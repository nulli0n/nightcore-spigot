package su.nightexpress.nightcore.integration.item;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface ItemProvider {

    default boolean is(String providerId) {
        return this.getId().equalsIgnoreCase(providerId);
    }

    String getId();

    boolean isAvailable();

    boolean isProducedBy(ItemStack itemStack);

    Optional<AdaptedItem> readItem(FileConfig config, String path);

    Optional<AdaptedItem> wrapItem(ItemStack itemStack);
}
