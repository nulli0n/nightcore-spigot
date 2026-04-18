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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import com.destroystokyo.paper.profile.PlayerProfile;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.dialog.DialogLike;
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
import su.nightexpress.nightcore.bridge.paper.bossbar.PaperBossBar;
import su.nightexpress.nightcore.bridge.paper.bossbar.PaperBossBarAdapter;
import su.nightexpress.nightcore.bridge.paper.dialog.PaperDialogAdapter;
import su.nightexpress.nightcore.bridge.paper.dialog.PaperDialogListener;
import su.nightexpress.nightcore.bridge.paper.event.PaperChatListener;
import su.nightexpress.nightcore.bridge.paper.event.PaperEventAdapter;
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

public class PaperBridge implements Software {

    private DialogAdapter<?>          dialogAdapter;
    private PaperTextComponentAdapter textComponentAdapter;
    private PaperEventAdapter         eventAdapter;

    private Set<DataComponentType> commonComponentsToHide;

    @Override
    @NonNull
    public String getName() {
        return "paper-bridge";
    }

    @Override
    public boolean isPaper() {
        return true;
    }

    @Override
    public boolean initialize() {
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
    @NonNull
    public PaperEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override
    @NonNull
    public AdaptedScheduler getScheduler(@NonNull JavaPlugin plugin) {
        return Version.isFolia() ? new FoliaScheduler(plugin) : new PaperScheduler(plugin);
    }

    @Override
    @NonNull
    public Listener createChatListener(@NonNull UniversalChatListenerCallback callback) {
        return new PaperChatListener(this, callback);
    }

    @Override
    @NonNull
    public Listener createDialogListener(@NonNull DialogClickHandler handler) {
        return new PaperDialogListener(handler);
    }

    @Override
    public void disallowLogin(@NonNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NonNull Result result, @NonNull NightComponent message) {
        event.disallow(result, this.textComponentAdapter.adaptComponent(message));
    }

    @Override
    public void closeDialog(@NonNull Player player) {
        player.closeDialog();
    }

    @Override
    public void showDialog(@NonNull Player player, @NonNull WrappedDialog dialog) {
        player.showDialog((DialogLike) this.dialogAdapter.adaptDialog(dialog));
    }

    @SuppressWarnings("deprecation")
    @Override
    public int nextEntityId() {
        return Bukkit.getUnsafe().nextEntityId();
    }

    @NonNull
    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
    }

    @Override
    @NonNull
    public PaperTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
    }

    @Override
    @NonNull
    public SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Bukkit.getCommandMap();
    }

    @Override
    @NonNull
    public Map<String, Command> getKnownCommands(@NonNull SimpleCommandMap commandMap) {
        return commandMap.getKnownCommands();
    }

    @NonNull
    private Component adaptComponent(@NonNull NightComponent component) {
        return this.textComponentAdapter.adaptComponent(component);
    }

    @NonNull
    public static String serializeComponent(@NonNull Component component) {
        return NightMessage.stripTags(MiniMessage.miniMessage().serialize(component), TagPool.NO_INVERTED_DECORATIONS);
    }

    @Override
    @NonNull
    public NightProfile createProfile(@NonNull UUID uuid) {
        return new PaperProfile(Bukkit.createProfile(uuid));
    }

    @Override
    @NonNull
    public NightProfile createProfile(@NonNull String name) {
        return new PaperProfile(Bukkit.createProfile(name));
    }

    @Override
    @NonNull
    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new PaperProfile(Bukkit.createProfile(uuid, name));
    }

    @Override
    @NonNull
    public NightProfile getProfile(@NonNull OfflinePlayer player) {
        return new PaperProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(@NonNull Player player, @NonNull NightComponent title, @NonNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComp = adaptComponent(title);
        Component subComp = adaptComponent(subtitle);

        Title.Times times = Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut));
        Title titles = Title.title(titleComp, subComp, times);

        player.showTitle(titles);
    }

    @Override
    @NonNull
    public InventoryView createView(@NonNull MenuType menuType, @NonNull NightComponent title, @NonNull Player player) {
        return menuType.typed().builder().title(adaptComponent(title)).build(player);
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
        return translatable.translationKey();
    }


    @Override
    @NonNull
    public String getDisplayNameSerialized(@NonNull Player player) {
        return serializeComponent(player.displayName());
    }

    @Override
    public void setDisplayName(@NonNull Player player, @Nullable NightComponent component) {
        player.displayName(component == null ? null : this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(@NonNull Player player) {
        Component header = player.playerListHeader();
        return header == null ? null : serializeComponent(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(@NonNull Player player) {
        Component footer = player.playerListFooter();
        return footer == null ? null : serializeComponent(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(@NonNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        Component paperHeader = header == null ? Component.empty() : this.adaptComponent(header);
        Component paperFooter = footer == null ? Component.empty() : this.adaptComponent(footer);

        player.sendPlayerListHeaderAndFooter(paperHeader, paperFooter);
    }

    @Override
    @NonNull
    public String getPlayerListNameSerialized(@NonNull Player player) {
        return serializeComponent(player.playerListName());
    }

    @Override
    public void setPlayerListName(@NonNull Player player, @NonNull NightComponent name) {
        player.playerListName(this.adaptComponent(name));
    }

    @Override
    public void kick(@NonNull Player player, @Nullable NightComponent component) {
        player.kick(component == null ? null : this.adaptComponent(component));
    }

    @Override
    public void setCustomName(@NonNull Nameable entity, @Nullable NightComponent component) {
        entity.customName(component == null ? null : this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getCustomName(@NonNull Nameable entity) {
        Component component = entity.customName();
        return component == null ? null : serializeComponent(component);
    }


    @Override
    @NonNull
    public ItemStack setType(@NonNull ItemStack itemStack, @NonNull Material material) {
        return itemStack.withType(material);
    }

    @Override
    public void editMeta(@NonNull ItemStack itemStack, @NonNull Consumer<ItemMeta> consumer) {
        itemStack.editMeta(consumer);
    }

    @Override
    public <T extends ItemMeta> void editMeta(@NonNull ItemStack itemStack, @NonNull Class<T> clazz, @NonNull Consumer<T> consumer) {
        itemStack.editMeta(clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(@NonNull ItemMeta meta) {
        Component component = meta.customName();
        return component == null ? null : serializeComponent(component);
    }

    @Override
    public void setCustomName(@NonNull ItemMeta meta, @Nullable NightComponent name) {
        meta.customName(name == null ? null : this.adaptComponent(name));
    }

    @Override
    @Nullable
    public String getItemName(@NonNull ItemMeta meta) {
        return meta.hasItemName() ? serializeComponent(meta.itemName()) : null;
    }

    @Override
    public void setItemName(@NonNull ItemMeta meta, @NonNull NightComponent name) {
        meta.itemName(adaptComponent(name));
    }

    @Override
    @Nullable
    public List<String> getLore(@NonNull ItemMeta meta) {
        List<Component> lore = meta.lore();
        return lore == null ? null : Lists.modify(lore, PaperBridge::serializeComponent);
    }

    @Override
    public void setLore(@NonNull ItemMeta meta, @Nullable List<NightComponent> lore) {
        meta.lore(lore == null ? null : Lists.modify(lore, this::adaptComponent));
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(@NonNull ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) return null;

        PlayerProfile profile = skullMeta.getPlayerProfile();
        return profile == null ? null : new PaperProfile(profile);
    }

    @NonNull
    public Set<String> getCommonComponentsToHide() {
        if (this.commonComponentsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return /*BukkitThing.allFromRegistry(Registry.DATA_COMPONENT_TYPE)*/this.commonComponentsToHide.stream().map(
            BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public Set<String> getHiddenComponents(@NonNull ItemStack itemStack) {
        TooltipDisplay tooltipDisplay = itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);
        if (tooltipDisplay == null) return Collections.emptySet();

        return tooltipDisplay.hiddenComponents().stream().map(BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(@NonNull ItemStack itemStack) {
        //        TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(this.commonComponentsToHide).build();
        //        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
        //
        if (commonComponentsToHide == null) return;

        hidePaperComponents(itemStack, this.commonComponentsToHide);
    }

    @Override
    public void hideComponents(@NonNull ItemStack itemStack, @NonNull Set<String> componentNames) {
        Set<DataComponentType> componentTypes = componentNames.stream().map(name -> BukkitThing.getByString(
            RegistryType.Paper.DATA_COMPONENT_TYPE, name))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hidePaperComponents(itemStack, componentTypes);
    }

    public static void hidePaperComponents(@NonNull ItemStack itemStack, @NonNull Set<DataComponentType> componentTypes) {
        try {
            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(componentTypes).build();
            itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
        }
        catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    @NonNull
    public PaperBossBar createBossBar(@NonNull NightComponent title, @NonNull NightBarColor barColor, @NonNull NightBarOverlay barOverlay, @NonNull NightBarFlag... barFlags) {
        Component name = this.textComponentAdapter.adaptComponent(title);
        BossBar.Color color = PaperBossBarAdapter.adaptColor(barColor);
        BossBar.Overlay overlay = PaperBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = BossBar.bossBar(name, 0F, color, overlay);
        return new PaperBossBar(this, bar).addFlags(barFlags);
    }
}
