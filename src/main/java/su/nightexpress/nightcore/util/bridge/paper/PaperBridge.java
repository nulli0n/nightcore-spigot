package su.nightexpress.nightcore.util.bridge.paper;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.ComponentBuildable;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class PaperBridge implements Software {

    private Set<DataComponentType> componentTypes;

    @Override
    @NotNull
    public String getName() {
        return "paper-bridge";
    }

    @Override
    public boolean isSpigot() {
        return false;
    }

    @Override
    public boolean isPaper() {
        return true;
    }

    @Override
    public boolean initialize(@NotNull NightCore core) {
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            this.componentTypes = BukkitThing.allFromRegistry(Registry.DATA_COMPONENT_TYPE);
            this.componentTypes.remove(DataComponentTypes.LORE);
            this.componentTypes.remove(DataComponentTypes.ITEM_NAME);
            this.componentTypes.remove(DataComponentTypes.ITEM_MODEL);
            this.componentTypes.remove(DataComponentTypes.CUSTOM_NAME);
            this.componentTypes.remove(DataComponentTypes.CUSTOM_MODEL_DATA);
            this.componentTypes.remove(DataComponentTypes.TOOLTIP_DISPLAY);
            this.componentTypes.remove(DataComponentTypes.TOOLTIP_STYLE);
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int nextEntityId() {
        return Bukkit.getUnsafe().nextEntityId();
    }

    @Override
    @NotNull
    public SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Bukkit.getCommandMap();
    }

    @Override
    @NotNull
    public Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap) {
        return commandMap.getKnownCommands();
    }

    @Override
    @NotNull
    public PaperComponent textComponent(@NotNull String text) {
        return PaperComponent.text(text);
    }

    @Override
    @NotNull
    public PaperComponent translateComponent(@NotNull String key) {
        return PaperComponent.translate(key);
    }

    @Override
    @NotNull
    public PaperComponent buildComponent(@NotNull List<ComponentBuildable> childrens) {
        return PaperComponent.builder(childrens);
    }

    @NotNull
    private static Component fromNightComponent(@NotNull String component) {
        return fromNightComponent(NightMessage.parse(component));
    }

    @NotNull
    private static Component fromNightComponent(@NotNull NightComponent component) {
        return ((PaperComponent)component).getParent();
    }

    @Override
    public void sendTitles(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComp = fromNightComponent(title);
        Component subComp = fromNightComponent(subtitle);

        Title.Times times = Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeIn));
        Title titles = Title.title(titleComp, subComp, times);

        player.showTitle(titles);
    }

    @Override
    @NotNull
    public InventoryView createView(@NotNull MenuType menuType, @NotNull String title, @NotNull Player player) {
        if (Version.isAtLeast(Version.MC_1_21_4)) {
            return menuType.typed().builder().title(fromNightComponent(title)).build(player);
        }
        else {
            return menuType.typed().create(player, fromNightComponent(title));
        }
    }

    @Override
    public void setDisplayName(@NotNull ItemMeta meta, @NotNull String name) {
        meta.displayName(fromNightComponent(name));
    }

    @Override
    public void setItemName(@NotNull ItemMeta meta, @NotNull String name) {
        meta.itemName(fromNightComponent(name));
    }

    @Override
    public void setLore(@NotNull ItemMeta meta, @NotNull List<String> lore) {
        meta.lore(Lists.modify(lore, PaperBridge::fromNightComponent));
    }

    @Override
    public void hideComponents(@NotNull ItemStack itemStack) {
        TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(this.componentTypes).build();
        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
    }
}
