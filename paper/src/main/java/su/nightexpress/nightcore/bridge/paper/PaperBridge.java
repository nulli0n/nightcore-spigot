package su.nightexpress.nightcore.bridge.paper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import com.destroystokyo.paper.profile.PlayerProfile;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.util.Ticks;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarFlag;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogAdapter;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.paper.bossbar.PaperBossBar;
import su.nightexpress.nightcore.bridge.paper.bossbar.PaperBossBarAdapter;
import su.nightexpress.nightcore.bridge.paper.dialog.PaperDialogAdapter;
import su.nightexpress.nightcore.bridge.paper.dialog.PaperDialogListener;
import su.nightexpress.nightcore.bridge.paper.event.PaperChatListener;
import su.nightexpress.nightcore.bridge.paper.event.PaperEventAdapter;
import su.nightexpress.nightcore.bridge.paper.key.PaperKey;
import su.nightexpress.nightcore.bridge.paper.scheduler.FoliaScheduler;
import su.nightexpress.nightcore.bridge.paper.scheduler.PaperScheduler;
import su.nightexpress.nightcore.bridge.paper.text.PaperTextComponentAdapter;
import su.nightexpress.nightcore.bridge.scheduler.AdaptedScheduler;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.RegistryType;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;
import su.nightexpress.nightcore.util.text.night.tag.TagPool;

@NullMarked
public class PaperBridge implements Software {

    private DialogAdapter<?>          dialogAdapter;
    private PaperTextComponentAdapter textComponentAdapter;
    private PaperEventAdapter         eventAdapter;

    private Set<DataComponentType> commonComponentsToHide;

    private UnsafeEntityIdProvider entityIdProvider;

    @Override
    public String getName() {
        return "paper-bridge";
    }

    @Override
    public boolean isPaper() {
        return true;
    }

    @Override
    public boolean initialize() {
        this.entityIdProvider = new ReflectionEntityIdProvider();

        this.textComponentAdapter = new PaperTextComponentAdapter(this);
        this.dialogAdapter = new PaperDialogAdapter(this);

        this.commonComponentsToHide = BukkitThing.getAll(RegistryType.Paper.DATA_COMPONENT_TYPE);
        this.commonComponentsToHide.remove(DataComponentTypes.LORE);
        this.commonComponentsToHide.remove(DataComponentTypes.ITEM_NAME);
        this.commonComponentsToHide.remove(DataComponentTypes.ITEM_MODEL);
        this.commonComponentsToHide.remove(DataComponentTypes.CUSTOM_NAME);
        this.commonComponentsToHide.remove(DataComponentTypes.CUSTOM_MODEL_DATA);
        this.commonComponentsToHide.remove(DataComponentTypes.TOOLTIP_DISPLAY);
        this.commonComponentsToHide.remove(DataComponentTypes.TOOLTIP_STYLE);

        this.eventAdapter = new PaperEventAdapter(this.textComponentAdapter);
        return true;
    }

    @Override

    public PaperEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override

    public AdaptedScheduler getScheduler(JavaPlugin plugin) {
        return Version.isFolia() ? new FoliaScheduler(plugin) : new PaperScheduler(plugin);
    }

    @Override

    public Listener createChatListener(UniversalChatListenerCallback callback) {
        return new PaperChatListener(this, callback);
    }

    @Override

    public Listener createDialogListener(DialogClickHandler handler) {
        return new PaperDialogListener(handler);
    }

    @Override
    public void disallowLogin(AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.Result result,
                              NightComponent message) {
        event.disallow(result, this.textComponentAdapter.adaptComponent(message));
    }

    @Override
    public void closeDialog(Player player) {
        player.closeDialog();
    }

    @Override
    public void showDialog(Player player, WrappedDialog dialog) {
        player.showDialog((DialogLike) this.dialogAdapter.adaptDialog(dialog));
    }

    @Override
    @Deprecated
    public int nextEntityId() {
        return this.entityIdProvider.getNextEntityId(Bukkit.getWorlds().get(0));
    }

    @Override
    public int nextEntityId(World world) {
        return this.entityIdProvider.getNextEntityId(world);
    }


    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
    }

    @Override

    public PaperTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
    }

    @Override

    public SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Bukkit.getCommandMap();
    }

    @Override

    public Map<String, Command> getKnownCommands(SimpleCommandMap commandMap) {
        return commandMap.getKnownCommands();
    }


    private Component adaptComponent(NightComponent component) {
        return this.textComponentAdapter.adaptComponent(component);
    }


    public static String serializeComponent(Component component) {
        return NightMessage.stripTags(MiniMessage.miniMessage().serialize(component), TagPool.NO_INVERTED_DECORATIONS);
    }


    @Override
    public boolean isValidKeyNamespace(String namespace) {
        return Key.parseableNamespace(namespace);
    }

    @Override
    public boolean isValidKeyValue(String value) {
        return Key.parseableValue(value);
    }

    @Override
    public boolean allowedInNamespace(char character) {
        return Key.allowedInNamespace(character);
    }

    @Override
    public boolean allowedInValue(char character) {
        return Key.allowedInValue(character);
    }

    @Override
    public AdaptedKey createKey(Plugin plugin, String value) {
        try {
            return new PaperKey(Key.key(plugin, value));
        }
        catch (InvalidKeyException exception) {
            throw new su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey createKey(String namespace, String value) {
        try {
            return new PaperKey(Key.key(namespace, value));
        }
        catch (InvalidKeyException exception) {
            throw new su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey createKey(String string) {
        try {
            return new PaperKey(Key.key(string));
        }
        catch (InvalidKeyException exception) {
            throw new su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException(exception);
        }
    }

    @Override
    public AdaptedKey getKey(World world) {
        return new PaperKey(world.key());
    }

    @Override
    public NightProfile createProfile(UUID uuid) {
        return new PaperProfile(Bukkit.createProfile(uuid));
    }

    @Override
    public NightProfile createProfile(String name) {
        return new PaperProfile(Bukkit.createProfile(name));
    }

    @Override

    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new PaperProfile(Bukkit.createProfile(uuid, name));
    }

    @Override

    public NightProfile getProfile(OfflinePlayer player) {
        return new PaperProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(Player player, NightComponent title, NightComponent subtitle,
                           int fadeIn, int stay, int fadeOut) {
        Component titleComp = adaptComponent(title);
        Component subComp = adaptComponent(subtitle);

        Title.Times times = Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut));
        Title titles = Title.title(titleComp, subComp, times);

        player.showTitle(titles);
    }

    @Override

    public InventoryView createView(MenuType menuType, NightComponent title, Player player) {
        return menuType.typed().builder().title(adaptComponent(title)).build(player);
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
        return translatable.translationKey();
    }


    @Override

    public String getDisplayNameSerialized(Player player) {
        return serializeComponent(player.displayName());
    }

    @Override
    public void setDisplayName(Player player, @Nullable NightComponent component) {
        player.displayName(component == null ? null : this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(Player player) {
        Component header = player.playerListHeader();
        return header == null ? null : serializeComponent(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(Player player) {
        Component footer = player.playerListFooter();
        return footer == null ? null : serializeComponent(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(Player player, @Nullable NightComponent header,
                                          @Nullable NightComponent footer) {
        Component paperHeader = header == null ? Component.empty() : this.adaptComponent(header);
        Component paperFooter = footer == null ? Component.empty() : this.adaptComponent(footer);

        player.sendPlayerListHeaderAndFooter(paperHeader, paperFooter);
    }

    @Override

    public String getPlayerListNameSerialized(Player player) {
        return serializeComponent(player.playerListName());
    }

    @Override
    public void setPlayerListName(Player player, NightComponent name) {
        player.playerListName(this.adaptComponent(name));
    }

    @Override
    public void kick(Player player, @Nullable NightComponent component) {
        player.kick(component == null ? null : this.adaptComponent(component));
    }

    @Override
    public void setCustomName(Nameable entity, @Nullable NightComponent component) {
        entity.customName(component == null ? null : this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getCustomName(Nameable entity) {
        Component component = entity.customName();
        return component == null ? null : serializeComponent(component);
    }


    @Override

    public ItemStack setType(ItemStack itemStack, Material material) {
        return itemStack.withType(material);
    }

    @Override
    public void editMeta(ItemStack itemStack, Consumer<ItemMeta> consumer) {
        itemStack.editMeta(consumer);
    }

    @Override
    public <T extends ItemMeta> void editMeta(ItemStack itemStack, Class<T> clazz,
                                              Consumer<T> consumer) {
        itemStack.editMeta(clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(ItemMeta meta) {
        Component component = meta.customName();
        return component == null ? null : serializeComponent(component);
    }

    @Override
    public void setCustomName(ItemMeta meta, @Nullable NightComponent name) {
        meta.customName(name == null ? null : this.adaptComponent(name));
    }

    @Override
    @Nullable
    public String getItemName(ItemMeta meta) {
        return meta.hasItemName() ? serializeComponent(meta.itemName()) : null;
    }

    @Override
    public void setItemName(ItemMeta meta, NightComponent name) {
        meta.itemName(adaptComponent(name));
    }

    @Override
    @Nullable
    public List<String> getLore(ItemMeta meta) {
        List<Component> lore = meta.lore();
        return lore == null ? null : Lists.modify(lore, PaperBridge::serializeComponent);
    }

    @Override
    public void setLore(ItemMeta meta, @Nullable List<NightComponent> lore) {
        meta.lore(lore == null ? null : Lists.modify(lore, this::adaptComponent));
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) return null;

        PlayerProfile profile = skullMeta.getPlayerProfile();
        return profile == null ? null : new PaperProfile(profile);
    }


    public Set<String> getCommonComponentsToHide() {
        if (this.commonComponentsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return /*BukkitThing.allFromRegistry(Registry.DATA_COMPONENT_TYPE)*/this.commonComponentsToHide.stream().map(
            BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override

    public Set<String> getHiddenComponents(ItemStack itemStack) {
        TooltipDisplay tooltipDisplay = itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);
        if (tooltipDisplay == null) return Collections.emptySet();

        return tooltipDisplay.hiddenComponents().stream().map(BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(ItemStack itemStack) {
        if (commonComponentsToHide == null) return;

        hidePaperComponents(itemStack, this.commonComponentsToHide);
    }

    @Override
    public void hideComponents(ItemStack itemStack, Set<String> componentNames) {
        Set<DataComponentType> componentTypes = componentNames.stream().map(name -> BukkitThing.getByString(
            RegistryType.Paper.DATA_COMPONENT_TYPE, name))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hidePaperComponents(itemStack, componentTypes);
    }

    public static void hidePaperComponents(ItemStack itemStack,
                                           Set<DataComponentType> componentTypes) {
        try {
            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(componentTypes).build();
            itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
        }
        catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
    }

    @Override

    public PaperBossBar createBossBar(NightComponent title, NightBarColor barColor,
                                      NightBarOverlay barOverlay, NightBarFlag... barFlags) {
        Component name = this.textComponentAdapter.adaptComponent(title);
        BossBar.Color color = PaperBossBarAdapter.adaptColor(barColor);
        BossBar.Overlay overlay = PaperBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = BossBar.bossBar(name, 0F, color, overlay);
        return new PaperBossBar(this, bar).addFlags(barFlags);
    }
}
