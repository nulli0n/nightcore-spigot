package su.nightexpress.nightcore.bridge.spigot;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.Translatable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import net.md_5.bungee.api.dialog.Dialog;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBar;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBarAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogListener;
import su.nightexpress.nightcore.bridge.spigot.event.SpigotChatListener;
import su.nightexpress.nightcore.bridge.spigot.event.SpigotEventAdapter;
import su.nightexpress.nightcore.bridge.spigot.scheduler.SpigotScheduler;
import su.nightexpress.nightcore.bridge.spigot.text.SpigotTextComponentAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LegacyColors;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class SpigotBridge implements Software {

    private static final String FIELD_COMMAND_MAP    = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    private static SimpleCommandMap commandMap;
    private static AtomicInteger    entityCounter;

    private SpigotTextComponentAdapter textComponentAdapter;
    private DialogAdapter<?>           dialogAdapter;
    private SpigotEventAdapter         eventAdapter;

    private Set<ItemFlag> commonFlagsToHide;

    @Override
    public boolean initialize() {
        this.textComponentAdapter = new SpigotTextComponentAdapter(this);
        this.dialogAdapter = new SpigotDialogAdapter(this);

        loadCommandMap();
        loadEntityCounter();

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

        this.eventAdapter = new SpigotEventAdapter();
        return true;
    }

    private static void loadCommandMap() {
        commandMap = (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
    }

    private static void loadEntityCounter() {
        Class<?> entityClass = Reflex.findClass("net.minecraft.world.entity", "Entity").orElse(null);
        if (entityClass == null) return;

        Object object = Reflex.getFieldValue(entityClass, "c");
        if (!(object instanceof AtomicInteger atomicInteger)) return;

        entityCounter = atomicInteger;
    }

    @Override
    @NonNull
    public SpigotEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override
    @NonNull
    public AdaptedScheduler getScheduler(@NonNull JavaPlugin plugin) {
        return new SpigotScheduler(plugin);
    }

    @Override
    @NonNull
    public Listener createChatListener(@NonNull UniversalChatListenerCallback callback) {
        return new SpigotChatListener(this, callback);
    }

    @Override
    @NonNull
    public Listener createDialogListener(@NonNull DialogClickHandler handler) {
        return new SpigotDialogListener(handler);
    }

    @Override
    public void disallowLogin(@NonNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NonNull Result result, @NonNull NightComponent message) {
        event.disallow(result, message.toLegacy());
    }

    @Override
    public void closeDialog(@NonNull Player player) {
        player.clearDialog();
    }

    @Override
    public void showDialog(@NonNull Player player, @NonNull WrappedDialog dialog) {
        player.showDialog((Dialog) this.dialogAdapter.adaptDialog(dialog));
    }

    @Override
    @NonNull
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
    @NonNull
    public SpigotTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
    }

    @NonNull
    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
    }

    @NonNull
    @Override
    public SimpleCommandMap getCommandMap() {
        if (commandMap == null) throw new IllegalStateException("Command map is null!");

        return commandMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public Map<String, Command> getKnownCommands(@NonNull SimpleCommandMap commandMap) {
        Map<String, Command> knownCommands = (Map<String, Command>) Reflex.getFieldValue(commandMap,
            FIELD_KNOWN_COMMANDS);
        return knownCommands == null ? Collections.emptyMap() : knownCommands;
    }

    @Override
    @NonNull
    public NightProfile createProfile(@NonNull UUID uuid) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid));
    }

    @Override
    @NonNull
    public NightProfile createProfile(@NonNull String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(name));
    }

    @Override
    @NonNull
    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid, name));
    }

    @Override
    @NonNull
    public NightProfile getProfile(@NonNull OfflinePlayer player) {
        return new SpigotProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(@NonNull Player player, @NonNull NightComponent title, @NonNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title.toLegacy(), subtitle.toLegacy(), fadeIn, stay, fadeOut);
    }

    @Override
    @NonNull
    public InventoryView createView(@NonNull MenuType menuType, @NonNull NightComponent title, @NonNull Player player) {
        String legacy = title.toLegacy();

        var builder = menuType.typed().builder();
        Reflex.setFieldValue(builder, "title", legacy);
        return builder.build(player);
    }


    @Override
    @NonNull
    public String getTranslationKey(@NonNull Material material) {
        return getTranslation(material);
    }

    @Override
    @NonNull
    public String getTranslationKey(@NonNull Attribute attribute) {
        return getTranslation(attribute);
    }

    @Override
    @NonNull
    public String getTranslationKey(@NonNull EntityType entityType) {
        return getTranslation(entityType);
    }

    @Override
    @NonNull
    public String getTranslationKey(@NonNull Enchantment enchantment) {
        return getTranslation(enchantment);
    }

    @Override
    @NonNull
    public String getTranslationKey(@NonNull PotionEffectType effectType) {
        return getTranslation(effectType);
    }

    @NonNull
    public static String getTranslation(@NonNull Translatable translatable) {
        return translatable.getTranslationKey();
    }


    @Override
    @NonNull
    public String getDisplayNameSerialized(@NonNull Player player) {
        return LegacyColors.plainColors(player.getDisplayName());
    }

    @Override
    public void setDisplayName(@NonNull Player player, @Nullable NightComponent component) {
        player.setDisplayName(component == null ? null : component.toLegacy());
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(@NonNull Player player) {
        String header = player.getPlayerListHeader();
        return header == null ? null : LegacyColors.plainColors(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(@NonNull Player player) {
        String footer = player.getPlayerListFooter();
        return footer == null ? null : LegacyColors.plainColors(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(@NonNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        player.setPlayerListHeaderFooter(header == null ? null : header.toLegacy(), footer == null ? null : footer
            .toLegacy());
    }

    @Override
    @NonNull
    public String getPlayerListNameSerialized(@NonNull Player player) {
        return LegacyColors.plainColors(player.getPlayerListName());
    }

    @Override
    public void setPlayerListName(@NonNull Player player, @NonNull NightComponent name) {
        player.setPlayerListName(name.toLegacy());
    }

    @Override
    public void kick(@NonNull Player player, @Nullable NightComponent component) {
        player.kickPlayer(component == null ? null : component.toLegacy());
    }

    @Override
    public void setCustomName(@NonNull Nameable entity, @Nullable NightComponent component) {
        entity.setCustomName(component == null ? null : component.toLegacy());
    }

    @Override
    @Nullable
    public String getCustomName(@NonNull Nameable entity) {
        String name = entity.getCustomName();
        return name == null ? null : LegacyColors.plainColors(name);
    }


    @Override
    @NonNull
    public ItemStack setType(@NonNull ItemStack itemStack, @NonNull Material material) {
        itemStack.setType(material);
        return itemStack;
    }

    public void editMeta(@NonNull ItemStack item, @NonNull Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    public <T extends ItemMeta> void editMeta(@NonNull ItemStack item, @NonNull Class<T> clazz, @NonNull Consumer<T> consumer) {
        editItemMeta(item, clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(@NonNull ItemMeta meta) {
        return meta.hasDisplayName() ? LegacyColors.plainColors(meta.getDisplayName()) : null;
    }

    @Override
    public void setCustomName(@NonNull ItemMeta meta, @Nullable NightComponent name) {
        meta.setDisplayName(name == null ? null : name.toLegacy());
    }

    @Override
    @Nullable
    public String getItemName(@NonNull ItemMeta meta) {
        return meta.hasItemName() ? LegacyColors.plainColors(meta.getItemName()) : null;
    }

    @Override
    public void setItemName(@NonNull ItemMeta meta, @NonNull NightComponent name) {
        meta.setItemName(name.toLegacy());
    }

    @Override
    @Nullable
    public List<String> getLore(@NonNull ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? null : Lists.modify(lore, LegacyColors::plainColors);
    }

    @Override
    public void setLore(@NonNull ItemMeta meta, @Nullable List<NightComponent> lore) {
        meta.setLore(lore == null ? null : lore.stream().map(NightComponent::toLegacy).toList());
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(@NonNull ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta meta)) return null;

        PlayerProfile profile = meta.getOwnerProfile();
        return profile == null ? null : new SpigotProfile(profile);
    }

    @Override
    @NonNull
    public Set<String> getCommonComponentsToHide() {
        if (this.commonFlagsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return this.commonFlagsToHide.stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public Set<String> getHiddenComponents(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;
        if (meta == null) return Collections.emptySet();

        return meta.getItemFlags().stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(@NonNull ItemStack itemStack) {
        if (commonFlagsToHide == null) return;

        hideSpigotComponents(itemStack, this.commonFlagsToHide);
    }

    public void hideComponents(@NonNull ItemStack itemStack, @NonNull Set<String> componentNames) {
        hideComponentsByName(itemStack, componentNames);
    }

    public static void hideComponentsByName(@NonNull ItemStack itemStack, @NonNull Set<String> componentNames) {
        Set<ItemFlag> componentTypes = componentNames.stream().map(name -> Enums.parse(name, ItemFlag.class).orElse(
            null))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hideSpigotComponents(itemStack, componentTypes);
    }

    public static void hideSpigotComponents(@NonNull ItemStack itemStack, @NonNull Set<ItemFlag> componentTypes) {
        editItemMeta(itemStack, meta -> componentTypes.forEach(meta::addItemFlags));
    }

    private static void editItemMeta(@NonNull ItemStack item, @NonNull Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    private static <T extends ItemMeta> void editItemMeta(@NonNull ItemStack item, @NonNull Class<T> clazz, @NonNull Consumer<T> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!clazz.isAssignableFrom(meta.getClass())) return;

        T specific = clazz.cast(meta);

        consumer.accept(specific);
        item.setItemMeta(specific);
    }

    @Override
    @NonNull
    public SpigotBossBar createBossBar(@NonNull NightComponent title, @NonNull NightBarColor barColor, @NonNull NightBarOverlay barOverlay, @NonNull NightBarFlag... barFlags) {
        BarColor color = SpigotBossBarAdapter.adaptColor(barColor);
        BarStyle overlay = SpigotBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = Bukkit.createBossBar(title.toLegacy(), color, overlay);
        return new SpigotBossBar(bar).addFlags(barFlags);
    }
}
