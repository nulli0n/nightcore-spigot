package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemNbt;

public class ShowItemDecorator implements Decorator {

    private final String itemData;

    public static ShowItemDecorator from(@NotNull ItemStack item) {
        String content = ItemNbt.compress(item);
        return new ShowItemDecorator(content == null ? BukkitThing.toString(item.getType()) : content);
    }

    public ShowItemDecorator(@NotNull String string) {
        this.itemData = string;
    }

    @NotNull
    public HoverEvent createEvent() {
        ItemStack itemStack = null;

        try {
            itemStack = Bukkit.getItemFactory().createItemStack(this.itemData);
        }
        catch (IllegalArgumentException exception) {
            try {
                itemStack = ItemNbt.decompress(this.itemData);
            }
            catch (NumberFormatException ignored) {

            }
        }
        if (itemStack == null) itemStack = new ItemStack(Material.AIR);

        String key = BukkitThing.toString(itemStack.getType());
        ItemMeta meta = itemStack.getItemMeta();
        Item item = new Item(key, itemStack.getAmount(), ItemTag.ofNbt(meta == null ? null : meta.getAsString()));

        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setHoverEvent(this.createEvent());
    }
}
