package su.nightexpress.nightcore.bridge.spigot;

import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.*;
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
    private SpigotEventAdapter eventAdapter;

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
    @NotNull
    public SpigotEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override
    @NotNull
    public AdaptedScheduler getScheduler(@NotNull JavaPlugin plugin) {
        return new SpigotScheduler(plugin);
    }

    @Override
    @NotNull
    public Listener createChatListener(@NotNull UniversalChatListenerCallback callback) {
        return new SpigotChatListener(this, callback);
    }

    @Override
    @NotNull
    public Listener createDialogListener(@NotNull DialogClickHandler handler) {
        return new SpigotDialogListener(handler);
    }

    @Override
    public void disallowLogin(@NotNull AsyncPlayerPreLoginEvent event, @NotNull AsyncPlayerPreLoginEvent.Result result, @NotNull NightComponent message) {
        event.disallow(result, message.toLegacy());
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
    @NotNull
    public String getDisplayNameSerialized(@NotNull Player player) {
        return LegacyColors.plainColors(player.getDisplayName());
    }

    @Override
    public void setDisplayName(@NotNull Player player, @NotNull NightComponent component) {
        player.setDisplayName(component.toLegacy());
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(@NotNull Player player) {
        String header = player.getPlayerListHeader();
        return header == null ? null : LegacyColors.plainColors(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(@NotNull Player player) {
        String footer = player.getPlayerListFooter();
        return footer == null ? null : LegacyColors.plainColors(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(@NotNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        player.setPlayerListHeaderFooter(header == null ? null : header.toLegacy(), footer == null ? null : footer.toLegacy());
    }

    @Override
    @NotNull
    public String getPlayerListNameSerialized(@NotNull Player player) {
        return LegacyColors.plainColors(player.getPlayerListName());
    }

    @Override
    public void setPlayerListName(@NotNull Player player, @NotNull NightComponent name) {
        player.setPlayerListName(name.toLegacy());
    }

    @Override
    public void kick(@NotNull Player player, @Nullable NightComponent component) {
        player.kickPlayer(component == null ? null : component.toLegacy());
    }

    @Override
    public void setCustomName(@NotNull Nameable entity, @Nullable NightComponent component) {
        entity.setCustomName(component == null ? null : component.toLegacy());
    }

    @Override
    @Nullable
    public String getCustomName(@NotNull Nameable entity) {
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
