package su.nightexpress.nightcore.util.text.tag.decorator;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@Deprecated
public class ShowItemDecorator implements Decorator {

    private final String itemData;

    public static ShowItemDecorator from(@NotNull ItemStack item) {
        String content = ItemNbt.compress(item);
        return new ShowItemDecorator(content == null ? BukkitThing.toString(item.getType()) : content);
    }

    public ShowItemDecorator(@NotNull String string) {
        this.itemData = string;
    }

//    @NotNull
//    public HoverEvent createEvent() {
//        ItemStack itemStack = null;
//
//        try {
//            itemStack = Bukkit.getItemFactory().createItemStack(this.itemData);
//        }
//        catch (IllegalArgumentException exception) {
//            try {
//                itemStack = ItemNbt.decompress(this.itemData);
//            }
//            catch (NumberFormatException ignored) {
//
//            }
//        }
//        if (itemStack == null) itemStack = new ItemStack(Material.AIR);
//
//        String key = BukkitThing.toString(itemStack.getType());
//        ItemMeta meta = itemStack.getItemMeta();
//        String nbt = meta == null ? "{}" : meta.getAsString();
//
//        Item item = new Item(key, itemStack.getAmount(), ItemTag.ofNbt(nbt));
//
//        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
//    }

    @Override
    @NotNull
    public NightComponent decorate(@NotNull NightComponent component) {
        ItemStack itemStack = ItemNbt.getHoverEventItem(this.itemData);
        if (itemStack == null) return component;

        //System.out.println("itemStack = " + itemStack);

        return component.hoverEvent(NightHoverEvent.showItem(itemStack));
        //component.setHoverEvent(itemStack);

        //component.setHoverEvent(HoverEventType.SHOW_ITEM, this.itemData);
        //component.setHoverEvent(this.createEvent());
    }
}
