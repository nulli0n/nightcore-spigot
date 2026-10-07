package su.nightexpress.nightcore.integration.item.mmoitems;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.Indyuce.mmoitems.MMOItems;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class MMOItemsItemProvider extends BaseIdentifiableItemProvider {

    @Override
    protected AdaptedItem createItem(ItemIdData idData) {
        return new MMOItemsItem(idData);
    }

    @Override
    protected Optional<ItemIdData> extractIdData(ItemStack itemStack) {
        String itemId = getItemId(itemStack);
        if (itemId != null) {
            return Optional.of(new ItemIdData(itemId, itemStack.getAmount()));
        }
        return Optional.empty();
    }

    @Override
    public String getId() {
        return ItemProviderKeys.MMOITEMS;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("MMOItems");
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        return getItemId(itemStack) != null;
    }

    public @Nullable String getItemId(ItemStack itemStack) {
        String type = MMOItems.getTypeName(itemStack);
        String id = MMOItems.getID(itemStack);
        if (type == null || id == null) return null;

        return type + ":" + id;
    }
}
