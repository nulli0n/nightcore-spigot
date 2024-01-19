package su.nightexpress.nightcore.util.text.decoration;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.ItemUtil;

public class ShowItemDecorator implements Decorator {

    private final ItemStack item;

    public ShowItemDecorator(@NotNull String string) {
        this(ItemUtil.decompress(string));
    }

    public ShowItemDecorator(ItemStack item) {
        this.item = item == null ? new ItemStack(Material.AIR) : new ItemStack(item);
    }

    @NotNull
    public HoverEvent createEvent() {
        String key = item.getType().getKey().getKey();
        ItemMeta meta = item.getItemMeta();
        Item item = new Item(key, this.item.getAmount(), ItemTag.ofNbt(meta == null ? null : meta.getAsString()));

        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setHoverEvent(this.createEvent());
    }
}
