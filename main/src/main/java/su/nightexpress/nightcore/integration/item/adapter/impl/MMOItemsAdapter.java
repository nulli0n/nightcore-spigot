package su.nightexpress.nightcore.integration.item.adapter.impl;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class MMOItemsAdapter extends IdentifiableItemAdapter {

    public MMOItemsAdapter() {
        super("mmoitems");
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return MMOItems.getType(itemStack) != null && MMOItems.getID(itemStack) != null;
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        String[] split = itemId.split(":");

        Type type = Type.get(split[0]);
        if (type == null || split.length < 2) return null;

        String id = split[1];

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, id);
        if (template != null) {
            return template.newBuilder().build().newBuilder().buildSilently();
        }

        return null;
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        String[] split = data.getItemId().split(":");

        Type type = Type.get(split[0]);
        if (type == null || split.length < 2) return false;

        String id = split[1];

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, id);
        return template != null;
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        String type = MMOItems.getTypeName(itemStack);
        String id = MMOItems.getID(itemStack);
        if (type == null || id == null) return null;

        return type + ":" + id;
    }
}
