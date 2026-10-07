package su.nightexpress.nightcore.integration.item.mmoitems;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import su.nightexpress.nightcore.integration.item.BaseIdentifiableItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@NullMarked
public class MMOItemsItem extends BaseIdentifiableItem {

    public MMOItemsItem(ItemIdData idData) {
        super(idData);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        String itemId = getItemId(itemStack);
        return itemId != null && itemId.equalsIgnoreCase(this.idData.getItemId());
    }

    @Override
    public boolean isValid() {
        String[] split = this.idData.getItemId().split(":");

        Type type = Type.get(split[0]);
        if (type == null || split.length < 2) return false;

        String id = split[1];

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, id);
        return template != null;
    }

    @Override
    public Optional<ItemStack> itemStack() {
        String[] split = this.idData.getItemId().split(":");

        Type type = Type.get(split[0]);
        if (type == null || split.length < 2) return Optional.empty();

        String id = split[1];

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, id);
        if (template != null) {
            ItemStack itemStack = template.newBuilder().build().newBuilder().buildSilently();
            itemStack.setAmount(this.idData.getAmount());
            return Optional.of(itemStack);
        }

        return Optional.empty();
    }

    public @Nullable String getItemId(ItemStack itemStack) {
        String type = MMOItems.getTypeName(itemStack);
        String id = MMOItems.getID(itemStack);
        if (type == null || id == null) return null;

        return type + ":" + id;
    }
}
