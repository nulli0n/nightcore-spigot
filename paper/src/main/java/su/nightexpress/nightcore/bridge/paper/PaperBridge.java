package su.nightexpress.nightcore.bridge.paper;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PaperBridge implements Software {

    private DialogAdapter<?>          dialogAdapter;
    private PaperTextComponentAdapter textComponentAdapter;
    private PaperEventAdapter         eventAdapter;

    private Set<DataComponentType> commonComponentsToHide;

    @Override
    @NotNull
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

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            this.dialogAdapter = new PaperDialogAdapter(this);
        }

        if (Version.isAtLeast(Version.MC_1_21_5)) {
            this.commonComponentsToHide = BukkitThing.getAll(RegistryType.Paper.DATA_COMPONENT_TYPE);
            this.commonComponentsToHide.remove(DataComponentTypes.LORE);
            this.commonComponentsToHide.remove(DataComponentTypes.ITEM_NAME);
            this.commonComponentsToHide.remove(DataComponentTypes.ITEM_MODEL);
            this.commonComponentsToHide.remove(DataComponentTypes.CUSTOM_NAME);
            this.commonComponentsToHide.remove(DataComponentTypes.CUSTOM_MODEL_DATA);
            this.commonComponentsToHide.remove(DataComponentTypes.TOOLTIP_DISPLAY);
            this.commonComponentsToHide.remove(DataComponentTypes.TOOLTIP_STYLE);
        }

        this.eventAdapter = new PaperEventAdapter(this.textComponentAdapter);
        return true;
    }

    @Override
    @NotNull
    public PaperEventAdapter eventAdapter() {
        return this.eventAdapter;
    }

    @Override
    @NotNull
    public AdaptedScheduler getScheduler(@NotNull JavaPlugin plugin) {
        return Version.isFolia() ? new FoliaScheduler(plugin) : new PaperScheduler(plugin);
    }

    @Override
    @NotNull
    public Listener createChatListener(@NotNull UniversalChatListenerCallback callback) {
        return new PaperChatListener(this, callback);
    }

    @Override
    @NotNull
    public Listener createDialogListener(@NotNull DialogClickHandler handler) {
        return new PaperDialogListener(handler);
    }

    @Override
    public void disallowLogin(@NotNull AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.@NotNull Result result, @NotNull NightComponent message) {
        event.disallow(result, this.textComponentAdapter.adaptComponent(message));
    }

    @Override
    public void closeDialog(@NotNull Player player) {
        player.closeDialog();
    }

    @Override
    public void showDialog(@NotNull Player player, @NotNull WrappedDialog dialog) {
        player.showDialog((DialogLike) this.dialogAdapter.adaptDialog(dialog));
    }

    @SuppressWarnings("deprecation")
    @Override
    public int nextEntityId() {
        return Bukkit.getUnsafe().nextEntityId();
    }

    @NotNull
    public DialogAdapter<?> getDialogAdapter() {
        return this.dialogAdapter;
    }

    @Override
    @NotNull
    public PaperTextComponentAdapter getTextComponentAdapter() {
        return this.textComponentAdapter;
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

/*    @NotNull
    public NightKey namespacedKey(@NotNull String namespace, @NotNull String value) {
        return new PaperKey(Key.key(namespace, value));
    }*/

    @NotNull
    private Component adaptComponent(@NotNull NightComponent component) {
        return this.textComponentAdapter.adaptComponent(component);
    }

    @NotNull
    public static String serializeComponent(@NotNull Component component) {
        return NightMessage.stripTags(MiniMessage.miniMessage().serialize(component), TagPool.NO_INVERTED_DECORATIONS);
    }

    @Override
    @NotNull
    public NightProfile createProfile(@NotNull UUID uuid) {
        return new PaperProfile(Bukkit.createProfile(uuid));
    }

    @Override
    @NotNull
    public NightProfile createProfile(@NotNull String name) {
        return new PaperProfile(Bukkit.createProfile(name));
    }

    @Override
    @NotNull
    public NightProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        return new PaperProfile(Bukkit.createProfile(uuid, name));
    }

    @Override
    @NotNull
    public NightProfile getProfile(@NotNull OfflinePlayer player) {
        return new PaperProfile(player.getPlayerProfile());
    }

    @Override
    public void sendTitles(@NotNull Player player, @NotNull NightComponent title, @NotNull NightComponent subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComp = adaptComponent(title);
        Component subComp = adaptComponent(subtitle);

        Title.Times times = Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut));
        Title titles = Title.title(titleComp, subComp, times);

        player.showTitle(titles);
    }

    @Override
    @NotNull
    public InventoryView createView(@NotNull MenuType menuType, @NotNull NightComponent title, @NotNull Player player) {
        if (Version.isAtLeast(Version.MC_1_21_4)) {
            return menuType.typed().builder().title(adaptComponent(title)).build(player);
        }
        else {
            return menuType.typed().create(player, adaptComponent(title));
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
        return translatable.translationKey();
    }


    @Override
    @NotNull
    public String getDisplayNameSerialized(@NotNull Player player) {
        return serializeComponent(player.displayName());
    }

    @Override
    public void setDisplayName(@NotNull Player player, @NotNull NightComponent component) {
        player.displayName(this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getPlayerListHeaderSerialized(@NotNull Player player) {
        Component header = player.playerListHeader();
        return header == null ? null : serializeComponent(header);
    }

    @Override
    @Nullable
    public String getPlayerListFooterSerialized(@NotNull Player player) {
        Component footer = player.playerListFooter();
        return footer == null ? null : serializeComponent(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(@NotNull Player player, @Nullable NightComponent header, @Nullable NightComponent footer) {
        Component paperHeader = header == null ? Component.empty() : this.adaptComponent(header);
        Component paperFooter = footer == null ? Component.empty() : this.adaptComponent(footer);

        player.sendPlayerListHeaderAndFooter(paperHeader, paperFooter);
    }

    @Override
    @NotNull
    public String getPlayerListNameSerialized(@NotNull Player player) {
        return serializeComponent(player.playerListName());
    }

    @Override
    public void setPlayerListName(@NotNull Player player, @NotNull NightComponent name) {
        player.playerListName(this.adaptComponent(name));
    }

    @Override
    public void kick(@NotNull Player player, @Nullable NightComponent component) {
        player.kick(component == null ? null : this.adaptComponent(component));
    }

    @Override
    public void setCustomName(@NotNull Nameable entity, @Nullable NightComponent component) {
        entity.customName(component == null ? null : this.adaptComponent(component));
    }

    @Override
    @Nullable
    public String getCustomName(@NotNull Nameable entity) {
        Component component = entity.customName();
        return component == null ? null : serializeComponent(component);
    }




    @Override
    @NotNull
    public ItemStack setType(@NotNull ItemStack itemStack, @NotNull Material material) {
        return itemStack.withType(material);
    }

    @Override
    public void editMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer) {
        itemStack.editMeta(consumer);
    }

    @Override
    public <T extends ItemMeta> void editMeta(@NotNull ItemStack itemStack, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        itemStack.editMeta(clazz, consumer);
    }

    @Override
    @Nullable
    public String getCustomName(@NotNull ItemMeta meta) {
        Component component;
        if (Version.isBehind(Version.MC_1_21_5)) {
            component = meta.displayName();
        }
        else {
            component = meta.customName();
        }
        return component == null ? null : serializeComponent(component);
    }

    @Override
    public void setCustomName(@NotNull ItemMeta meta, @Nullable NightComponent name) {
        if (Version.isBehind(Version.MC_1_21_5)) {
            meta.displayName(name == null ? null : this.adaptComponent(name));
        }
        else {
            meta.customName(name == null ? null : this.adaptComponent(name));
        }
    }

    @Override
    @Nullable
    public String getItemName(@NotNull ItemMeta meta) {
        return meta.hasItemName() ? serializeComponent(meta.itemName()) : null;
    }

    @Override
    public void setItemName(@NotNull ItemMeta meta, @NotNull NightComponent name) {
        meta.itemName(adaptComponent(name));
    }

    @Override
    @Nullable
    public List<String> getLore(@NotNull ItemMeta meta) {
        List<Component> lore = meta.lore();
        return lore == null ? null : Lists.modify(lore, PaperBridge::serializeComponent);
    }

    @Override
    public void setLore(@NotNull ItemMeta meta, @Nullable List<NightComponent> lore) {
        meta.lore(lore == null ? null : Lists.modify(lore, this::adaptComponent));
    }

    @Override
    @Nullable
    public NightProfile getOwnerProfile(@NotNull ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof SkullMeta skullMeta)) return null;

        PlayerProfile profile = skullMeta.getPlayerProfile();
        return profile == null ? null : new PaperProfile(profile);
    }

    @NotNull
    public Set<String> getCommonComponentsToHide() {
        if (this.commonComponentsToHide == null) return new HashSet<>(); // Fix for <= 1.21.4

        return /*BukkitThing.allFromRegistry(Registry.DATA_COMPONENT_TYPE)*/this.commonComponentsToHide.stream().map(BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public Set<String> getHiddenComponents(@NotNull ItemStack itemStack) {
        TooltipDisplay tooltipDisplay = itemStack.getData(DataComponentTypes.TOOLTIP_DISPLAY);
        if (tooltipDisplay == null) return Collections.emptySet();

        return tooltipDisplay.hiddenComponents().stream().map(BukkitThing::getAsString).collect(Collectors.toSet());
    }

    @Override
    public void hideComponents(@NotNull ItemStack itemStack) {
//        TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(this.commonComponentsToHide).build();
//        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
//
        if (commonComponentsToHide == null) return;

        hidePaperComponents(itemStack, this.commonComponentsToHide);
    }

    @Override
    public void hideComponents(@NotNull ItemStack itemStack, @NotNull Set<String> componentNames) {
        Set<DataComponentType> componentTypes = componentNames.stream().map(name -> BukkitThing.getByString(RegistryType.Paper.DATA_COMPONENT_TYPE, name))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        hidePaperComponents(itemStack, componentTypes);
    }

    public static void hidePaperComponents(@NotNull ItemStack itemStack, @NotNull Set<DataComponentType> componentTypes) {
        try {
            TooltipDisplay tooltipDisplay = TooltipDisplay.tooltipDisplay().hiddenComponents(componentTypes).build();
            itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
        }
        catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    @NotNull
    public PaperBossBar createBossBar(@NotNull NightComponent title, @NotNull NightBarColor barColor, @NotNull NightBarOverlay barOverlay, @NotNull NightBarFlag... barFlags) {
        Component name = this.textComponentAdapter.adaptComponent(title);
        BossBar.Color color = PaperBossBarAdapter.adaptColor(barColor);
        BossBar.Overlay overlay = PaperBossBarAdapter.adaptOverlay(barOverlay);

        BossBar bar = BossBar.bossBar(name, 0F, color, overlay);
        return new PaperBossBar(this, bar).addFlags(barFlags);
    }
}
