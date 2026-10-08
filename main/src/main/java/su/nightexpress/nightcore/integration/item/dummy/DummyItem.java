package su.nightexpress.nightcore.integration.item.dummy;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class DummyItem implements AdaptedItem {

    public static final DummyItem INSTANCE = new DummyItem();

    @Override
    public String getProviderId() {
        return "dummy";
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Optional<ItemStack> itemStack() {
        return Optional.of(NightItem.fromType(Material.BARRIER)
            .localized(CoreLang.BROKEN_ITEM)
            .hideAllComponents()
            .getItemStack()
        );
    }
}
