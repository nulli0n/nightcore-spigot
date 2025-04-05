package su.nightexpress.nightcore.util.bridge;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.ComponentBuildable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public interface Software {

    boolean initialize(@NotNull NightCore core);

    @NotNull String getName();

    boolean isSpigot();

    boolean isPaper();

    int nextEntityId();

    @NotNull SimpleCommandMap getCommandMap();

    @NotNull Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap);

    @NotNull NightComponent textComponent(@NotNull String text);

    @NotNull NightComponent translateComponent(@NotNull String key);

    @NotNull NightComponent buildComponent(@NotNull List<ComponentBuildable> childrens);

    @NotNull InventoryView createView(@NotNull MenuType menuType, @NotNull String title, @NotNull Player player);



    void sendTitles(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut);


    void setDisplayName(@NotNull ItemMeta meta, @NotNull String name);

    void setItemName(@NotNull ItemMeta meta, @NotNull String name);

    void setLore(@NotNull ItemMeta meta, @NotNull List<String> lore);

    void hideComponents(@NotNull ItemStack itemStack);
}
