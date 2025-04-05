package su.nightexpress.nightcore.util.bridge.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.ComponentBuildable;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class SpigotBridge implements Software {

    private static final String FIELD_COMMAND_MAP    = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    private static SimpleCommandMap commandMap;
    private static AtomicInteger entityCounter;

    @Override
    public boolean initialize(@NotNull NightCore core) {
        loadCommandMap();
        loadEntityCounter(core);
        return true;
    }

    private static void loadCommandMap() {
        commandMap = (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
    }

    private static void loadEntityCounter(NightCore core) {
        Class<?> entityClass = Reflex.getClass("net.minecraft.world.entity", "Entity");
        if (entityClass == null) {
            core.error("Could not find NMS Entity class!");
            return;
        }

        String fieldName = "c";
        if (Version.isAtLeast(Version.V1_19_R3) && Version.isBehind(Version.MC_1_20_6)) {
            fieldName = "d";
        }

        Object object = Reflex.getFieldValue(entityClass, fieldName);
        if (!(object instanceof AtomicInteger atomicInteger)) {
            if (object == null) {
                core.error("Could not find entity counter field!");
            }
            else core.error("Field '" + fieldName + "' in " + entityClass.getName() + " class is " + object.getClass().getName()  + " (expected AtomicInteger)");
            return;
        }

        entityCounter = atomicInteger;
    }

    @Override
    @NotNull
    public String getName() {
        return "spigot-bridge";
    }

    @Override
    public boolean isSpigot() {
        return true;
    }

    @Override
    public boolean isPaper() {
        return false;
    }

    @Override
    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }

    @NotNull
    @Override
    public SimpleCommandMap getCommandMap() {
        if (commandMap == null) throw new IllegalStateException("Command map is null!");

        return commandMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap) {
        Map<String, Command> knownCommands = (Map<String, Command>) Reflex.getFieldValue(commandMap, FIELD_KNOWN_COMMANDS);
        return knownCommands == null ? Collections.emptyMap() : knownCommands;
    }

    @Override
    @NotNull
    public NightComponent textComponent(@NotNull String text) {
        return SpigotComponent.text(text);
    }

    @Override
    @NotNull
    public NightComponent translateComponent(@NotNull String key) {
        return SpigotComponent.translate(key);
    }

    @Override
    @NotNull
    public NightComponent buildComponent(@NotNull List<ComponentBuildable> childrens) {
        return SpigotComponent.builder(childrens);
    }

    @Override
    public void sendTitles(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        title = NightMessage.asLegacy(title);
        subtitle = NightMessage.asLegacy(subtitle);

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    @NotNull
    public InventoryView createView(@NotNull MenuType menuType, @NotNull String title, @NotNull Player player) {
        String legacy = NightMessage.asLegacy(title);

        if (Version.isAtLeast(Version.MC_1_21_4)) {
            var builder = menuType.typed().builder();
            Reflex.setFieldValue(builder, "title", legacy);
            return builder.build(player);
        }
        else {
            return menuType.typed().create(player, legacy);
        }
    }

    @Override
    public void setDisplayName(@NotNull ItemMeta meta, @NotNull String name) {
        meta.setDisplayName(NightMessage.asLegacy(name));
    }

    @Override
    public void setItemName(@NotNull ItemMeta meta, @NotNull String name) {
        meta.setItemName(NightMessage.asLegacy(name));
    }

    @Override
    public void setLore(@NotNull ItemMeta meta, @NotNull List<String> lore) {
        meta.setLore(NightMessage.asLegacy(lore));
    }

    @Override
    public void hideComponents(@NotNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, meta -> {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            meta.addItemFlags(ItemFlag.valueOf("HIDE_ADDITIONAL_TOOLTIP"));
        });
    }
}
