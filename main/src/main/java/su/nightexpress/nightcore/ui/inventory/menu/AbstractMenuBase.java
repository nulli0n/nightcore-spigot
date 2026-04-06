package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.api.event.MenuOpenEvent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.ui.inventory.Menu;
import su.nightexpress.nightcore.ui.inventory.MenuDataRegistry;
import su.nightexpress.nightcore.ui.inventory.MenuRegistry;
import su.nightexpress.nightcore.ui.inventory.action.*;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateConditions;
import su.nightexpress.nightcore.ui.inventory.condition.NamedCondition;
import su.nightexpress.nightcore.ui.inventory.display.CompositeDisplayModifier;
import su.nightexpress.nightcore.ui.inventory.display.DisplayModifiers;
import su.nightexpress.nightcore.ui.inventory.display.NamedDisplayModifier;
import su.nightexpress.nightcore.ui.inventory.item.*;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractMenuBase implements Menu, LangContainer {

    protected NightPlugin plugin;
    protected final MenuDataRegistry dataRegistry;

    protected final Map<String, MenuItem> defaultButtons;
    protected final Map<String, MenuItem> configButtons;

    protected final Map<UUID, MenuViewer> viewers;

    protected final ConfigProperty<MenuType> menuType;
    protected final ConfigProperty<String>   menuTitle;
    protected final ConfigProperty<Integer>  autoRefreshInterval;
    protected final ConfigProperty<Boolean>  papiIntegration;

    protected long autoRefreshIn;
    protected boolean configured;

    @Deprecated
    public AbstractMenuBase(@NonNull MenuType defaultType, @NonNull String  defaultTitle) {
        this(null, defaultType, defaultTitle);
    }

    public AbstractMenuBase(@Nullable NightPlugin plugin, @NonNull MenuType defaultType, @NonNull String  defaultTitle) {
        this.plugin = plugin;
        this.dataRegistry = new MenuDataRegistry();

        this.defaultButtons = new LinkedHashMap<>();
        this.configButtons = new LinkedHashMap<>();

        this.viewers = new HashMap<>();

        this.menuType = ConfigProperty.of(ConfigTypes.MENU_TYPE, "Settings.MenuType", defaultType);
        this.menuTitle = ConfigProperty.of(ConfigTypes.STRING, "Settings.Title", defaultTitle);
        this.autoRefreshInterval = ConfigProperty.of(ConfigTypes.INT, "Settings.AutoRefresh.Interval", 0);
        this.papiIntegration = ConfigProperty.of(ConfigTypes.BOOLEAN, "Settings.PlaceholderAPI.Enabled", false);

        this.dataRegistry.registerAction(MenuItemActions.NEXT_PAGE);
        this.dataRegistry.registerAction(MenuItemActions.PREVIOUS_PAGE);
        this.dataRegistry.registerAction(MenuItemActions.CLOSE);

        this.dataRegistry.registerCondition(ItemStateConditions.NEXT_PAGE);
        this.dataRegistry.registerCondition(ItemStateConditions.PREVIOUS_PAGE);

        this.dataRegistry.registerDisplayModifier(DisplayModifiers.VIEWER_SKULL);
    }

    public abstract void registerActions();

    public abstract void registerConditions();

    public abstract void defineDefaultLayout();

    @Deprecated
    public void load(@NonNull NightPlugin plugin) {
        this.load(plugin, null);
    }

    @Deprecated
    public void load(@NonNull NightPlugin plugin, @Nullable FileConfig config) {
        this.plugin = plugin;
        this.load0(config);
    }

    @Override
    public void load() {
        this.load0(null);
    }

    @Override
    public void load(@NonNull Path path) {
        this.load(FileConfig.load(path));
    }

    @Override
    public void load(@NonNull FileConfig config) {
        this.load0(config);
    }

    private void load0(@Nullable FileConfig config) {
        this.plugin.injectLang(this);
        this.registerActions();
        this.registerConditions();
        this.defineDefaultLayout();

        if (config != null) {
            this.loadFromConfig(config);
        }
    }

    private void loadFromConfig(@NonNull FileConfig config) {
        this.menuType.loadWithDefaults(config);
        this.menuTitle.loadWithDefaults(config);
        this.autoRefreshInterval.loadWithDefaults(config);
        this.papiIntegration.loadWithDefaults(config);

        this.updateLayout(config, "Items");
        this.updateLayout(config, "Content");
        this.loadItems(config, "Buttons");
        this.onLoad(config);

        config.saveChanges();
        this.configured = true;
    }

    private void updateLayout(@NonNull FileConfig config, @NonNull String contentPath) {
        if (!config.contains(contentPath)) return;

        Map<String, MenuItem> oldButtons = new LinkedHashMap<>();

        config.getSection(contentPath).forEach(sId -> {
            String itemPath = contentPath + "." + sId;

            NightItem item = config.getCosmeticItem(itemPath + ".Item");
            int[] slots = config.getIntArray(itemPath + ".Slots");
            String type = config.getString(itemPath + ".Type");

            if (type != null) {
                String validType;
                if (type.equalsIgnoreCase("page_next")) validType = "next_page";
                else if (type.equalsIgnoreCase("page_previous")) validType = "previous_page";
                else if (type.equalsIgnoreCase("return")) validType = "back";
                else if (type.isBlank() || type.equalsIgnoreCase("null")) validType = sId;
                else validType = type;

                oldButtons.put(validType, MenuItem.button().defaultState(item).slots(slots).build());
                return;
            }

            for (String sType : config.getSection(itemPath + ".Click_Commands")) {
                ClickType clickType = Enums.get(sType, ClickType.class);
                if (clickType == null) continue;

                List<String> commands = config.getStringList(itemPath + ".Click_Commands." + sType);
                if (commands.isEmpty()) continue;

                config.set("Buttons." + sId + ".States.default.Click-Commands." + sType, commands);
            }

            oldButtons.put(sId, MenuItem.custom().defaultState(item).slots(slots).build());
        });

        oldButtons.forEach((id, menuItem) -> {
            String itemPath = "Buttons." + id;

            menuItem.getStates().forEach((stateId, state) -> {
                config.set(itemPath + ".States." + stateId + ".Icon", state.getIcon());
            });

            config.setArray(itemPath + ".Slots", menuItem.getSlots());
        });
    }

    private void loadItems(@NonNull FileConfig config, @NonNull String path) {
        boolean hasSection = !config.getSection(path).isEmpty();

        this.defaultButtons.forEach((itemId, defaultItem) -> {
            ItemType type = defaultItem.getType();
            String itemPath = path + "." + itemId;

            if (!hasSection || (!config.contains(itemPath) && type.isPersistent())) {
                // Set basic data only. States will be set further.
                config.setArray(itemPath + ".Slots", defaultItem.getSlots());
            }
        });

        config.getSection(path).forEach(sId -> {
            String itemId = Strings.varStyle(sId).orElse(null);
            if (itemId == null) {
                plugin.warn("Malformed menu item ID '%s' in '%s'".formatted(sId, config.getPath()));
                return;
            }

            MenuItem defaultItem = this.defaultButtons.get(itemId);
            ItemType type = defaultItem == null ? ItemTypes.CUSTOM : defaultItem.getType();
            String itemPath = path + "." + itemId;

            MenuItem parsed = this.loadItem(config, itemPath, defaultItem, type);
            this.configButtons.put(itemId, parsed);
        });
    }

    @NonNull
    private MenuItem loadItem(@NonNull FileConfig config, @NonNull String path, @Nullable MenuItem defaultItem, @NonNull ItemType type) {
        MenuItem.Builder builder = MenuItem.builder(type);

        this.loadItemStates(config, path + ".States", builder, defaultItem, type);

        builder.slots(config.get(ConfigTypes.INT_ARRAY, path + ".Slots", defaultItem == null ? new int[0] : defaultItem.getSlots()));

        return builder.build();
    }

    private void loadItemStates(@NonNull FileConfig config,
                                @NonNull String path,
                                MenuItem.@NonNull Builder builder,
                                @Nullable MenuItem defaultItem,
                                @NonNull ItemType type) {

        boolean hasSection = !config.getSection(path).isEmpty();

        if (defaultItem != null) {
            defaultItem.getStates().forEach((stateId, defaultState) -> {
                String statePath = path + "." + stateId;

                if (!hasSection || (!config.contains(statePath) && type.isStatesLocked())) {
                    config.set(statePath + ".Icon", defaultState.getIcon());
                }
            });
        }

        config.getSection(path).forEach(stateName -> {
            String stateId = Strings.varStyle(stateName).orElse(null);
            if (stateId == null) {
                plugin.warn("Malformed item state ID '%s' in '%s'".formatted(stateName, config.getPath()));
                return;
            }

            ItemState defaultState = defaultItem == null ? null : defaultItem.getState(stateId);
            if (defaultState == null && type.isStatesLocked()) {
                plugin.warn("Unknown item state '%s' in '%s'".formatted(stateId, config.getPath()));
                return;
            }

            String statePath = path + "." + stateName;

            ItemState.Builder stateBuilder = ItemState.builder();

            stateBuilder.icon(config.get(ConfigTypes.NIGHT_ITEM, statePath + ".Icon", defaultState == null ? NightItem.fromType(Material.STONE) : defaultState.getIcon())).build();

            this.loadStateActions(config, statePath + ".Actions", stateBuilder, defaultState, type);
            this.loadStateConditions(config, statePath + ".Condition", stateBuilder, defaultState, type);
            this.loadStateDisplayModifiers(config, statePath + ".DisplayModifier", stateBuilder, defaultState, type);

            builder.state(stateId, stateBuilder.build());
        });
    }

    private void loadStateActions(@NonNull FileConfig config,
                                  @NonNull String path,
                                  ItemState.@NonNull Builder builder,
                                  @Nullable ItemState defaultState,
                                  @NonNull ItemType type) {

        if (type.isActionLocked()) {
            MenuItemAction defaultAction = defaultState == null ? null : defaultState.getAction();
            builder.action(defaultAction);
            if (defaultAction instanceof NamedAction namedAction) {
                config.set(path + ".Base", namedAction.name());
            }
            else config.remove(path + ".Base");

            return;
        }

        String actionName = config.getString(path + ".Base");
        if (actionName != null) {
            builder.action(this.dataRegistry.getAction(actionName));
            return;
        }

        if (type.isClickCommandsAllowed()) {
            Map<ClickType, List<String>> commandMap = new HashMap<>();
            for (String sType : config.getSection(path + ".Click-Commands")) {
                ClickType clickType = Enums.get(sType, ClickType.class);
                if (clickType == null) continue;

                List<String> commands = config.getStringList(path + ".Click-Commands." + sType);
                if (commands.isEmpty()) continue;

                commandMap.put(clickType, commands);
            }

            if (!commandMap.isEmpty()) {
                builder.action(new CommandsAction(commandMap));
            }
        }
    }

    private void loadStateConditions(@NonNull FileConfig config,
                                     @NonNull String path,
                                     ItemState.@NonNull Builder builder,
                                     @Nullable ItemState defaultState,
                                     @NonNull ItemType type) {

        ItemStateCondition condition = null;
        ItemStateCondition defaultCondition = defaultState == null ? null : defaultState.getCondition();
        if (type.isConditionLocked()) {
            condition = defaultCondition;
            if (defaultCondition instanceof NamedCondition namedCondition) {
                config.set(path, namedCondition.name());
            }
            else config.remove(path);
        }
        else {
            String conditionName = config.getString(path);
            if (conditionName != null) {
                condition = this.dataRegistry.getCondition(conditionName);
            }
        }

        builder.condition(condition);
    }

    private void loadStateDisplayModifiers(@NonNull FileConfig config,
                                           @NonNull String path,
                                           ItemState.@NonNull Builder builder,
                                           @Nullable ItemState defaultState,
                                           @NonNull ItemType type) {

        List<DisplayModifier> modifiers = new ArrayList<>();

        DisplayModifier defaultModifier = defaultState == null ? null : defaultState.getDisplayModifier();
        if (type.isDisplayModifierLocked()) {
            if (defaultModifier instanceof NamedDisplayModifier namedDisplayModifier) {
                config.set(path + ".Base", namedDisplayModifier.name());
            }
            else config.remove(path + ".Base");

            if (defaultModifier != null) {
                modifiers.add(defaultModifier);
            }
        }

        config.getStringList(path + ".Extras").forEach(modifierName -> {
            DisplayModifier displayModifier = this.dataRegistry.getDisplayModifier(modifierName);
            if (displayModifier != null) {
                modifiers.add(displayModifier);
            }
        });

        builder.displayModifier(modifiers.isEmpty() ? null : new CompositeDisplayModifier(modifiers));
    }

    @NonNull
    protected String getRawTitle(@NonNull ViewerContext context) {
        String title = this.menuTitle.get();

        if (this.isPlaceholderIntegrationEnabled()) {
            title = Placeholders.forPlayerWithPAPI(context.getPlayer()).apply(title);
        }

        return title;
    }

    @Override
    @NonNull
    public NightComponent getTitle(@NonNull ViewerContext context) {
        return NightMessage.parse(this.getRawTitle(context));
    }

    @Override
    @NonNull
    public MenuType getType(@NonNull MenuViewer viewer) {
        return this.menuType.get();
    }

    protected final boolean showMenu(@NonNull MenuRegistry registry, @NonNull Player player, @Nullable Consumer<MenuViewer> preRender) {
        if (player.isSleeping()) {
            this.close(player);
            return false;
        }

        MenuOpenEvent event = new MenuOpenEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            this.close(player);
            return false;
        }

        MenuViewer viewer = this.getOrCreateViewer(player);
        if (preRender != null) preRender.accept(viewer);
        //viewer.setCurrentObject(object);
        viewer.renderMenu(this/*, object*/);
        registry.registerViewer(player, this);
        return true;
    }

    protected abstract void onLoad(@NonNull FileConfig config);

    protected abstract void onClick(@NonNull ViewerContext context, @NonNull InventoryClickEvent event);

    protected abstract void onDrag(@NonNull ViewerContext context, @NonNull InventoryDragEvent event);

    protected abstract void onClose(@NonNull ViewerContext context, @NonNull InventoryCloseEvent event);

    @Override
    public void tick() {
        if (this.autoRefreshInterval.get() > 0) {
            if (this.isAutoRefreshTime()) {
                this.refresh();
                this.setAutoRefreshIn(this.autoRefreshInterval.get());
            }

            if (this.autoRefreshIn > 0) {
                this.autoRefreshIn--;
            }
        }
    }

    @Override
    public void refresh() {
        this.getViewers().forEach(MenuViewer::refresh);
    }

    @Override
    public void refresh(@NonNull Player player) {
        MenuViewer viewer = this.getViewer(player);
        if (viewer == null) return;

        viewer.refresh();
    }

    @Override
    public void close() {
        this.getViewers().forEach(viewer -> this.close(viewer.getPlayer()));
    }

    @Override
    public void close(@NonNull Player player) {
        this.close(player.getUniqueId());
    }

    @Override
    public void close(@NonNull UUID playerId) {
        MenuViewer viewer = this.getViewer(playerId);
        if (viewer == null) return;

        viewer.closeMenu();
    }

    @Override
    public void handleClick(@NonNull Player player, @NonNull InventoryClickEvent event) {
        MenuViewer viewer = this.getViewer(player);
        if (viewer == null) return;

        event.setCancelled(true);

        if (!viewer.canClickAgain()) return;

        viewer.setNextClickIn(System.currentTimeMillis() + CoreConfig.MENU_CLICK_COOLDOWN.get());

        ViewerContext context = viewer.createContext();

        this.onClick(context, event);

        // Handle menu clicks next server tick to handle them out of the InventoryClickEvent.
        plugin.runTask(() -> viewer.handleClick(event));
    }

    @Override
    public void handleDrag(@NonNull Player player, @NonNull InventoryDragEvent event) {
        MenuViewer viewer = this.getViewer(player);
        if (viewer == null) return;

        event.setCancelled(true);

        ViewerContext context = viewer.createContext();

        this.onDrag(context, event);
    }

    @Override
    public void handleClose(@NonNull Player player, @NonNull InventoryCloseEvent event, @NonNull MenuRegistry menuRegistry) {
        MenuViewer viewer = this.viewers.get(player.getUniqueId());
        if (viewer == null || viewer.isRefreshing()) return;

        ViewerContext context = viewer.createContext();

        this.viewers.remove(player.getUniqueId());

        this.onClose(context, event);
        viewer.handleClose(event);
        menuRegistry.unregisterViewer(player);
    }

    @Override
    public boolean hasViewers() {
        return !this.viewers.isEmpty();
    }

    @Override
    public boolean isViewer(@NonNull Player player) {
        return this.getViewer(player) != null;
    }

    @Override
    public boolean isViewer(@NonNull UUID playerId) {
        return this.getViewer(playerId) != null;
    }

    @NonNull
    protected MenuViewer getOrCreateViewer(@NonNull Player player) {
        return this.viewers.computeIfAbsent(player.getUniqueId(), k -> new MenuViewer(player));
    }

    @Override
    @Nullable
    public MenuViewer getViewer(@NonNull Player player) {
        return this.getViewer(player.getUniqueId());
    }

    @Override
    @Nullable
    public MenuViewer getViewer(@NonNull UUID playerId) {
        return this.viewers.get(playerId);
    }

    @Override
    @NonNull
    public Set<MenuViewer> getViewers() {
        return Set.copyOf(this.viewers.values());
    }

    protected void addBackButton(@NonNull MenuItemAction action, int... slots) {
        this.addDefaultButton("back", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.SPECTRAL_ARROW).localized(CoreLang.MENU_ICON_BACK).hideAllComponents())
                .action(action)
                .build()
            )
            .slots(slots)
            .build()
        );
    }

    protected void addNextPageButton(int... slots) {
        this.addDefaultButton("next_page", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_NEXT_PAGE).hideAllComponents())
                .action(MenuItemActions.NEXT_PAGE)
                .condition(ItemStateConditions.NEXT_PAGE)
                .build()
            )
            .slots(slots)
            .build()
        );
    }

    protected void addPreviousPageButton(int... slots) {
        this.addDefaultButton("previous_page", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_PREVIOUS_PAGE).hideAllComponents())
                .action(MenuItemActions.PREVIOUS_PAGE)
                .condition(ItemStateConditions.PREVIOUS_PAGE)
                .build()
            )
            .slots(slots)
            .build()
        );
    }

    @Deprecated
    protected void addNextPageItem(@NonNull Material material, int... slots) {
        this.addNextPageButton(slots);
    }

    @Deprecated
    protected void addPreviousPageItem(@NonNull Material material, int... slots) {
        this.addPreviousPageButton(slots);
    }

    protected void addBackgroundItem(@NonNull Material material, int... slots) {
        this.addDefaultButton(BukkitThing.getValue(material) + "_" + UUID.randomUUID().toString().substring(0, 5), MenuItem.custom()
            .defaultState(NightItem.fromType(material).hideAllComponents().setHideTooltip(true))
            .slots(slots)
            .build()
        );
    }

    protected void addDefaultButton(@NonNull String id, @NonNull MenuItem menuItem) {
        this.defaultButtons.put(id, menuItem);
    }

    @Deprecated
    protected void addDefaultItem(@NonNull String id, @NonNull MenuItem menuItem) {
        this.addDefaultButton(id, menuItem);
    }

    @Override
    @NonNull
    public MenuDataRegistry getDataRegistry() {
        return this.dataRegistry;
    }

    @Override
    @NonNull
    public Map<String, MenuItem> getItemsToDisplay() {
        Map<String, MenuItem> items = new LinkedHashMap<>();
        if (this.configured) {
            //items.putAll(this.configItems);
            items.putAll(this.configButtons);
        }
        else {
            //items.putAll(this.defaultItems);
            items.putAll(this.defaultButtons);
        }
        return items;
    }

    @Override
    @NonNull
    public Map<String, MenuItem> getDefaultButtons() {
        return this.defaultButtons;
    }

    @Override
    @NonNull
    public Map<String, MenuItem> getConfigButtons() {
        return this.configButtons;
    }

    @Override
    @NonNull
    @Deprecated
    public Map<String, MenuItem> getDefaultItems() {
        return this.getDefaultButtons();
    }

    @Override
    @NonNull
    @Deprecated
    public Map<String, MenuItem> getConfigItems() {
        return this.getConfigButtons();
    }

    @Override
    public int getAutoRefreshInterval() {
        return this.autoRefreshInterval.get();
    }

    @Override
    public long getAutoRefreshIn() {
        return this.autoRefreshIn;
    }

    @Override
    public void setAutoRefreshIn(long autoRefreshIn) {
        this.autoRefreshIn = autoRefreshIn;
    }

    @Override
    public boolean isAutoRefreshTime() {
        return this.autoRefreshIn == 0L;
    }

    @Override
    public boolean isPlaceholderIntegrationEnabled() {
        return this.papiIntegration.get();
    }
}
