package su.nightexpress.nightcore.bridge.spigot;

import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Translatable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBar;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBarAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogListener;
import su.nightexpress.nightcore.bridge.spigot.text.SpigotTextComponentAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SpigotBridge implements Software {

    private static final String FIELD_COMMAND_MAP    = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    private static SimpleCommandMap commandMap;
    private static AtomicInteger entityCounter;

    private SpigotTextComponentAdapter textComponentAdapter;
    private DialogAdapter<?>              dialogAdapter;

    private Set<ItemFlag>      commonFlagsToHide;

    @Override
    public boolean initialize() {
        this.textComponentAdapter = new SpigotTextComponentAdapter(this);

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            this.dialogAdapter = new SpigotDialogAdapter(this);
        }

        loadCommandMap();
        loadEntityCounter();

        if (Version.isAtLeast(Version.MC_1_21_5)) {
            this.commonFlagsToHide = Lists.newSet(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
            );
        }

        return true;
    }

    private static void loadCommandMap() {
        commandMap = (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
    }

    private static void loadEntityCounter() {
        Class<?> entityClass = Reflex.getClass("net.minecraft.world.entity", "Entity");
        if (entityClass == null) return;

        String fieldName = Version.isBehind(Version.MC_1_20_6) ? "d" : "c";

        Object object = Reflex.getFieldValue(entityClass, fieldName);
        if (!(object instanceof AtomicInteger atomicInteger)) return;

        entityCounter = atomicInteger;
    }

    @Override
    @NotNull
    public Listener createDialogListener(@NotNull DialogClickHandler handler) {
        return new SpigotDialogListener(handler);
    }

    @Override
    public void closeDialog(@NotNull Player player) {
        player.clearDialog();
    }

    @Override
    public void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog) {
        player.showDialog((Dialog) this.dialogAdapter.adaptDialog(dialog));
    }

    @Override
    @NotNull
    public String getName() {
        return "spigot-bridge";
    }

    @Override
    public boolean isPaper() {
        return false;
    }

    @Override
    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }

    @Override
    @NotNull
    public SpigotTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
    }

    @NotNull
    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
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
    public NightProfile createProfile(@NotNull UUID uuid) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid));
    }

    @Override
    @NotNull
    public NightProfile createProfile(@NotNull String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(name));
    }

    @Override
    @NotNull
    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid, name));
    }

    @Override
    @NotNull
    public NightProfile getProfile(@NotNull OfflinePlayer player) {
        return new SpigotProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(@NotNull Player player, @NotNull NightComponent title, @NotNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title.toLegacy(), subtitle.toLegacy(), fadeIn, stay, fadeOut);
    }

    @Override
    @NotNull
    public InventoryView createView(@NotNull MenuType menuType, @NotNull NightComponent title, @NotNull Player player) {
        String legacy = title.toLegacy();//NightMessage.asLegacy(title);

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
    @NotNull
    public String getTranslationKey(@NotNull Material material) {
        return getTranslation(material);
    }

    @Override
    @NotNull
    public String getTranslationKey(@NotNull Attribute attribute) {
        return getTranslation(attribute);
    }

    @Override
    @NotNull
    public String getTranslationKey(@NotNull EntityType entityType) {
        return getTranslation(entityType);
    }

    @Override
    @NotNull
    public String getTranslationKey(@NotNull Enchantment enchantment) {
        return getTranslation(enchantment);
    }

    @Override
    @NotNull
    public String getTranslationKey(@NotNull PotionEffectType effectType) {
        return getTranslation(effectType);
    }

    @NotNull
    public static String getTranslation(@NotNull Translatable translatable) {
        return translatable.getTranslationKey();
    }



    @Override
    public void setCustomName(@NotNull Entity entity, @NotNull NightComponent component) {
        entity.setCustomName(component.toLegacy());
    }

    @Override
    @Nullable
    public String getEntityName(@NotNull Entity entity) {
        String name = entity.getCustomName();
        return name == null ? null : LegacyColors.plainColors(name);
    }



    @Override
    @NotNull
    public ItemStack setType(@NotNull ItemStack itemStack, @NotNull Material material) {
        itemStack.setType(material);
        return itemStack;
    }

    public void editMeta(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    public <T extends ItemMeta> void editMeta(@NotNull ItemStack item, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        editItemMeta(item, clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(@NotNull ItemMeta meta) {
        return meta.hasDisplayName() ? LegacyColors.plainColors(meta.getDisplayName()) : null;
    }

    @Override
    public void setCustomName(@NotNull ItemMeta meta, @Nullable NightComponent name) {
        //meta.setDisplayName(NightMessage.asLegacy(name));
        meta.setDisplayName(name == null ? null : name.toLegacy());
    }

    @Override
    @Nullable
    public String getItemName(@NotNull ItemMeta meta) {
        return meta.hasItemName() ? LegacyColors.plainColors(meta.getItemName()) : null;
    }

    @Override
    public void setItemName(@NotNull ItemMeta meta, @NotNull NightComponent name) {
        //meta.setItemName(NightMessage.asLegacy(name));
        meta.setItemName(name.toLegacy());
    }

    @Override
    @Nullable
    public List<String> getLore(@NotNull ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? null : Lists.modify(lore, LegacyColors::plainColors);
    }

    @Override
    public void setLore(@NotNull ItemMeta meta, @Nullable List<NightComponent> lore) {
        //meta.setLore(NightMessage.asLegacy(lore));
        meta.setLore(lore == null ? null : lore.stream().map(NightComponent::toLegacy).toList());
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(@NotNull ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta meta)) return null;

        PlayerProfile profile = meta.getOwnerProfile();
        return profile == null ? null : new SpigotProfile(profile);
    }

    @Override
    @NotNull
    public Set<String> getCommonComponentsToHide() {
        if (this.commonFlagsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return this.commonFlagsToHide.stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public Set<String> getHiddenComponents(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;
        if (meta == null) return Collections.emptySet();

        return meta.getItemFlags().stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(@NotNull ItemStack itemStack) {
//        ItemUtil.editMeta(itemStack, meta -> {
//            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
//            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
//            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
//            meta.addItemFlags(ItemFlag.HIDE_DYE);
//            meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
//            meta.addItemFlags(ItemFlag.valueOf("HIDE_ADDITIONAL_TOOLTIP"));
//        });

        if (commonFlagsToHide == null) return;

        hideSpigotComponents(itemStack, this.commonFlagsToHide);
    }

    public void hideComponents(@NotNull ItemStack itemStack, @NotNull Set<String> componentNames) {
        hideComponentsByName(itemStack, componentNames);
    }

    public static void hideComponentsByName(@NotNull ItemStack itemStack, @NotNull Set<String> componentNames) {
        Set<ItemFlag> componentTypes = componentNames.stream().map(name -> Enums.parse(name, ItemFlag.class).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hideSpigotComponents(itemStack, componentTypes);
    }

    public static void hideSpigotComponents(@NotNull ItemStack itemStack, @NotNull Set<ItemFlag> componentTypes) {
        editItemMeta(itemStack, meta -> componentTypes.forEach(meta::addItemFlags));
    }

    private static void editItemMeta(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    private static <T extends ItemMeta> void editItemMeta(@NotNull ItemStack item, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!clazz.isAssignableFrom(meta.getClass())) return;

        T specific = clazz.cast(meta);

        consumer.accept(specific);
        item.setItemMeta(specific);
    }

    @Override
    @NotNull
    public SpigotBossBar createBossBar(@NotNull NightComponent title, @NotNull NightBarColor barColor, @NotNull NightBarOverlay barOverlay, @NotNull NightBarFlag... barFlags) {
        BarColor color = SpigotBossBarAdapter.adaptColor(barColor);
        BarStyle overlay = SpigotBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = Bukkit.createBossBar(title.toLegacy(), color, overlay);
        return new SpigotBossBar(bar).addFlags(barFlags);
    }
}
