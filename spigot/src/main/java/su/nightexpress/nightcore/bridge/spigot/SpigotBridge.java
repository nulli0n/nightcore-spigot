package su.nightexpress.nightcore.bridge.spigot;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Translatable;
import org.bukkit.World;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.md_5.bungee.api.dialog.Dialog;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBar;
import su.nightexpress.nightcore.bridge.spigot.bossbar.SpigotBossBarAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogAdapter;
import su.nightexpress.nightcore.bridge.spigot.dialog.SpigotDialogListener;
import su.nightexpress.nightcore.bridge.spigot.event.SpigotChatListener;
import su.nightexpress.nightcore.bridge.spigot.event.SpigotEventAdapter;
import su.nightexpress.nightcore.bridge.spigot.key.SpigotKey;
import su.nightexpress.nightcore.bridge.spigot.scheduler.SpigotScheduler;
import su.nightexpress.nightcore.bridge.spigot.text.SpigotTextComponentAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LegacyColors;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@NullMarked
public class SpigotBridge implements Software {

    private static final String FIELD_COMMAND_MAP    = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    @Nullable
    private static MethodHandle getNextEntityIdHandle;

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
        if (Version.isAtLeast(Version.MC_26_2)) {
            try {
                Class<?> levelClass = Class.forName("net.minecraft.world.level.Level");
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodType methodType = MethodType.methodType(int.class);

                getNextEntityIdHandle = lookup.findVirtual(levelClass, "getNextEntityId", methodType);

            }
            catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
                throw new IllegalStateException("Failed to initialize reflection for Level.getNextEntityId(). Ensure mappings are correct.", e);
            }
        }
        else {
            Class<?> entityClass = Reflex.findClass("net.minecraft.world.entity", "Entity").orElse(null);
            if (entityClass == null) return;

            Object object = Reflex.getFieldValue(entityClass, "c");
            if (!(object instanceof AtomicInteger atomicInteger)) return;

            entityCounter = atomicInteger;
        }
    }

    @Override

    public SpigotEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override

    public AdaptedScheduler getScheduler(JavaPlugin plugin) {
        return new SpigotScheduler(plugin);
    }

    @Override

    public Listener createChatListener(UniversalChatListenerCallback callback) {
        return new SpigotChatListener(this, callback);
    }

    @Override

    public Listener createDialogListener(DialogClickHandler handler) {
        return new SpigotDialogListener(handler);
    }

    @Override
    public void disallowLogin(AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.Result result,
                              NightComponent message) {
        event.disallow(result, message.toLegacy());
    }

    @Override
    public void closeDialog(Player player) {
        player.clearDialog();
    }

    @Override
    public void showDialog(Player player, WrappedDialog dialog) {
        player.showDialog((Dialog) this.dialogAdapter.adaptDialog(dialog));
    }

    @Override

    public String getName() {
        return "spigot-bridge";
    }

    @Override
    public boolean isPaper() {
        return false;
    }

    @Override
    @Deprecated
    public int nextEntityId() {
        return nextEntityId(Bukkit.getWorlds().get(0));
    }

    @Override
    public int nextEntityId(World world) {
        if (getNextEntityIdHandle != null) {
            try {
                return (int) getNextEntityIdHandle.invoke(world);
            }
            catch (Throwable t) {
                t.printStackTrace();
                return 0;
            }
        }

        return entityCounter.incrementAndGet();
    }

    @Override

    public SpigotTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
    }


    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
    }


    @Override
    public SimpleCommandMap getCommandMap() {
        if (commandMap == null) throw new IllegalStateException("Command map is null!");

        return commandMap;
    }

    @SuppressWarnings("unchecked")
    @Override

    public Map<String, Command> getKnownCommands(SimpleCommandMap commandMap) {
        Map<String, Command> knownCommands = (Map<String, Command>) Reflex.getFieldValue(commandMap,
            FIELD_KNOWN_COMMANDS);
        return knownCommands == null ? Collections.emptyMap() : knownCommands;
    }


    @Override
    public boolean allowedInNamespace(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }

    @Override
    public boolean allowedInValue(char c) {
        return allowedInNamespace(c) || c == '/';
    }

    @Override
    public boolean isValidKeyNamespace(String namespace) {
        int len = namespace.length();
        if (len == 0) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (!allowedInNamespace(namespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isValidKeyValue(String key) {
        int len = key.length();
        if (len == 0) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (!allowedInValue(key.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public AdaptedKey createKey(Plugin plugin, String value) {
        try {
            return new SpigotKey(new NamespacedKey(plugin, value));
        }
        catch (IllegalArgumentException exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey createKey(String namespace, String value) {
        try {
            return new SpigotKey(new NamespacedKey(namespace, value));
        }
        catch (IllegalArgumentException exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey createKey(String string) {
        try {
            return new SpigotKey(NamespacedKey.fromString(string));
        }
        catch (IllegalArgumentException exception) {
            throw new InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey getKey(World world) {
        return new SpigotKey(world.getKey());
    }

    @Override
    public NightProfile createProfile(UUID uuid) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid));
    }

    @Override
    public NightProfile createProfile(String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(name));
    }

    @Override
    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new SpigotProfile(Bukkit.createPlayerProfile(uuid, name));
    }

    @Override
    public NightProfile getProfile(OfflinePlayer player) {
        return new SpigotProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(Player player, NightComponent title, NightComponent subtitle,
                           int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title.toLegacy(), subtitle.toLegacy(), fadeIn, stay, fadeOut);
    }

    @Override

    public InventoryView createView(MenuType menuType, NightComponent title, Player player) {
        String legacy = title.toLegacy();

        var builder = menuType.typed().builder();
        Reflex.setFieldValue(builder, "title", legacy);
        return builder.build(player);
    }


    @Override

    public String getTranslationKey(Material material) {
        return getTranslation(material);
    }

    @Override

    public String getTranslationKey(Attribute attribute) {
        return getTranslation(attribute);
    }

    @Override

    public String getTranslationKey(EntityType entityType) {
        return getTranslation(entityType);
    }

    @Override

    public String getTranslationKey(Enchantment enchantment) {
        return getTranslation(enchantment);
    }

    @Override

    public String getTranslationKey(PotionEffectType effectType) {
        return getTranslation(effectType);
    }


    public static String getTranslation(Translatable translatable) {
        return translatable.getTranslationKey();
    }


    @Override

    public String getDisplayNameSerialized(Player player) {
        return LegacyColors.plainColors(player.getDisplayName());
    }

    @Override
    public void setDisplayName(Player player, @Nullable NightComponent component) {
        player.setDisplayName(component == null ? null : component.toLegacy());
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(Player player) {
        String header = player.getPlayerListHeader();
        return header == null ? null : LegacyColors.plainColors(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(Player player) {
        String footer = player.getPlayerListFooter();
        return footer == null ? null : LegacyColors.plainColors(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(Player player, @Nullable NightComponent header,
                                          @Nullable NightComponent footer) {
        player.setPlayerListHeaderFooter(header == null ? null : header.toLegacy(), footer == null ? null : footer
            .toLegacy());
    }

    @Override

    public String getPlayerListNameSerialized(Player player) {
        return LegacyColors.plainColors(player.getPlayerListName());
    }

    @Override
    public void setPlayerListName(Player player, NightComponent name) {
        player.setPlayerListName(name.toLegacy());
    }

    @Override
    public void kick(Player player, @Nullable NightComponent component) {
        player.kickPlayer(component == null ? null : component.toLegacy());
    }

    @Override
    public void setCustomName(Nameable entity, @Nullable NightComponent component) {
        entity.setCustomName(component == null ? null : component.toLegacy());
    }

    @Override
    @Nullable
    public String getCustomName(Nameable entity) {
        String name = entity.getCustomName();
        return name == null ? null : LegacyColors.plainColors(name);
    }


    @Override

    public ItemStack setType(ItemStack itemStack, Material material) {
        itemStack.setType(material);
        return itemStack;
    }

    public void editMeta(ItemStack item, Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    public <T extends ItemMeta> void editMeta(ItemStack item, Class<T> clazz,
                                              Consumer<T> consumer) {
        editItemMeta(item, clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(ItemMeta meta) {
        return meta.hasDisplayName() ? LegacyColors.plainColors(meta.getDisplayName()) : null;
    }

    @Override
    public void setCustomName(ItemMeta meta, @Nullable NightComponent name) {
        meta.setDisplayName(name == null ? null : name.toLegacy());
    }

    @Override
    @Nullable
    public String getItemName(ItemMeta meta) {
        return meta.hasItemName() ? LegacyColors.plainColors(meta.getItemName()) : null;
    }

    @Override
    public void setItemName(ItemMeta meta, NightComponent name) {
        meta.setItemName(name.toLegacy());
    }

    @Override
    @Nullable
    public List<String> getLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? null : Lists.modify(lore, LegacyColors::plainColors);
    }

    @Override
    public void setLore(ItemMeta meta, @Nullable List<NightComponent> lore) {
        meta.setLore(lore == null ? null : lore.stream().map(NightComponent::toLegacy).toList());
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta meta)) return null;

        PlayerProfile profile = meta.getOwnerProfile();
        return profile == null ? null : new SpigotProfile(profile);
    }

    @Override

    public Set<String> getCommonComponentsToHide() {
        if (this.commonFlagsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return this.commonFlagsToHide.stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override

    public Set<String> getHiddenComponents(ItemStack itemStack) {
        ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;
        if (meta == null) return Collections.emptySet();

        return meta.getItemFlags().stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(ItemStack itemStack) {
        if (commonFlagsToHide == null) return;

        hideSpigotComponents(itemStack, this.commonFlagsToHide);
    }

    public void hideComponents(ItemStack itemStack, Set<String> componentNames) {
        hideComponentsByName(itemStack, componentNames);
    }

    public static void hideComponentsByName(ItemStack itemStack, Set<String> componentNames) {
        Set<ItemFlag> componentTypes = componentNames.stream().map(name -> Enums.parse(name, ItemFlag.class).orElse(
            null))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hideSpigotComponents(itemStack, componentTypes);
    }

    public static void hideSpigotComponents(ItemStack itemStack, Set<ItemFlag> componentTypes) {
        editItemMeta(itemStack, meta -> componentTypes.forEach(meta::addItemFlags));
    }

    private static void editItemMeta(ItemStack item, Consumer<ItemMeta> consumer) {
        editItemMeta(item, ItemMeta.class, consumer);
    }

    private static <T extends ItemMeta> void editItemMeta(ItemStack item, Class<T> clazz,
                                                          Consumer<T> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!clazz.isAssignableFrom(meta.getClass())) return;

        T specific = clazz.cast(meta);

        consumer.accept(specific);
        item.setItemMeta(specific);
    }

    @Override

    public SpigotBossBar createBossBar(NightComponent title, NightBarColor barColor,
                                       NightBarOverlay barOverlay, NightBarFlag... barFlags) {
        BarColor color = SpigotBossBarAdapter.adaptColor(barColor);
        BarStyle overlay = SpigotBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = Bukkit.createBossBar(title.toLegacy(), color, overlay);
        return new SpigotBossBar(bar).addFlags(barFlags);
    }
}
