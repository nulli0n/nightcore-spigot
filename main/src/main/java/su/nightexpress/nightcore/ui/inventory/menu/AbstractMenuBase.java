package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.api.event.MenuOpenEvent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigProperty;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.Menu;
import su.nightexpress.nightcore.ui.inventory.action.ActionRegistry;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemActions;
import su.nightexpress.nightcore.ui.inventory.condition.ConditionRegistry;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateConditions;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.ui.inventory.MenuRegistry;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.*;

public abstract class AbstractMenuBase implements Menu {

    protected final ActionRegistry    actionRegistry;
    protected final ConditionRegistry conditionRegistry;

    protected final Map<String, MenuItem> defaultButtons;
    protected final Map<String, MenuItem> configButtons;
    protected final Map<String, MenuItem> defaultItems;
    protected final Map<String, MenuItem> configItems;

    protected final Map<UUID, MenuViewer> viewers;

    protected final ConfigProperty<MenuType> menuType;
    protected final ConfigProperty<String>   menuTitle;
    protected final ConfigProperty<Integer>  autoRefreshInterval;
    protected final ConfigProperty<Boolean>  papiIntegration;

    protected long autoRefreshIn;
    protected boolean configured;

    public AbstractMenuBase(@NotNull MenuType defaultType, @NotNull String  defaultTitle) {
        this.actionRegistry = new ActionRegistry();
        this.conditionRegistry = new ConditionRegistry();

        this.defaultButtons = new LinkedHashMap<>();
        this.configButtons = new HashMap<>();
        this.defaultItems = new LinkedHashMap<>();
        this.configItems = new HashMap<>();

        this.viewers = new HashMap<>();

        this.menuType = ConfigProperty.of(ConfigTypes.MENU_TYPE, "Settings.MenuType", defaultType);
        this.menuTitle = ConfigProperty.of(ConfigTypes.STRING, "Settings.Title", defaultTitle);
        this.autoRefreshInterval = ConfigProperty.of(ConfigTypes.INT, "Settings.AutoRefresh.Interval", 0);
        this.papiIntegration = ConfigProperty.of(ConfigTypes.BOOLEAN, "Settings.PlaceholderAPI.Enabled", false);

        this.registerAction("page_next", MenuItemActions.NEXT_PAGE);
        this.registerAction("page_previous", MenuItemActions.PREVIOUS_PAGE);
        this.registerAction("close", MenuItemActions.CLOSE);
        // TODO Return

        this.registerCondition("can_move_forward", ItemStateConditions.NEXT_PAGE);
        this.registerCondition("can_move_backward", ItemStateConditions.PREVIOUS_PAGE);
    }

    public abstract void registerActions();

    public abstract void registerConditions();

    public abstract void defineDefaultLayout();

    public void load(@NotNull NightPlugin plugin) {
        this.load(plugin, null);
    }

    public void load(@NotNull NightPlugin plugin, @Nullable FileConfig config) {
        this.registerActions();
        this.registerConditions();
        this.defineDefaultLayout();

        if (config != null) {
            this.loadFromConfig(plugin, config);
        }
    }

    private void loadFromConfig(@NotNull NightPlugin plugin, @NotNull FileConfig config) {
        this.menuType.read(config);
        this.menuTitle.read(config);
        this.autoRefreshInterval.read(config);
        this.papiIntegration.read(config);

        this.loadButtons(plugin, config, "Buttons");
        this.loadItems(plugin, config, "Items");
        this.onLoad(config);

        // TODO item commands

        config.saveChanges();
        this.configured = true;
    }

    private void loadButtons(@NotNull NightPlugin plugin, @NotNull FileConfig config, @NotNull String path) {
        this.defaultButtons.forEach((id, menuItem) -> {
            String itemPath = path + "." + id;

            if (config.contains(itemPath)) return;

            menuItem.getAllStates().forEach(state -> {
                this.writeItemState(config, itemPath + ".States." + state.getName(), state);
            });

            config.setArray(itemPath + ".Slots", menuItem.getSlots());
        });

        config.getSection(path).forEach(sId -> {
            String itemId = Strings.varStyle(sId).orElse(null);
            if (itemId == null) {
                plugin.warn("Malformed menu item ID '%s' in '%s'".formatted(sId, config.getPath()));
                return;
            }

            MenuItem defaultItem = this.defaultButtons.get(itemId);
            if (defaultItem == null) {
                plugin.warn("Unknown button '%s' in %s'".formatted(sId, config.getPath()));
                return;
            }

            MenuItem.Builder itemBuilder = MenuItem.builder();

            Map<String, ItemState> defaultStates = new LinkedHashMap<>();
            defaultItem.getAllStates().forEach(state -> defaultStates.put(state.getName(), state));

            String itemPath = path + "." + sId;

            this.loadItemStates(plugin, config, itemPath + ".States", defaultStates).forEach(state -> {
                if (state.getName().equalsIgnoreCase("default")) {
                    itemBuilder.defaultState(state);
                }
                else {
                    itemBuilder.state(state);
                }
            });

            int[] slots = config.getIntArray(itemPath + ".Slots");

            itemBuilder.slots(slots);

            this.configButtons.put(itemId, itemBuilder.build());
        });
    }

    @NotNull
    private List<ItemState> loadItemStates(@NotNull NightPlugin plugin, @NotNull FileConfig config, @NotNull String path, @NotNull Map<String, ItemState> defaultStates) {
        List<ItemState> states = new ArrayList<>();

        defaultStates.forEach((stateId, defaultState) -> {
            if (config.contains(path + "." + stateId)) return;

            this.writeItemState(config, path + "." + stateId, defaultState);
        });

        config.getSection(path).forEach(stateName -> {
            String stateId = Strings.varStyle(stateName).orElse(null);
            if (stateId == null) {
                plugin.warn("Malformed item state ID '%s' in '%s'".formatted(stateName, config.getPath()));
                return;
            }

            ItemState defaultState = defaultStates.get(stateId);
            if (defaultState == null) {
                plugin.warn("Unknown item state '%s' in '%s'".formatted(stateId, config.getPath()));
                return;
            }

            Replacer defaultReplacer = defaultState.getIcon().getReplacer();

            NightItem configIcon = config.getCosmeticItem(path + "." + stateName + ".Icon").setReplacer(defaultReplacer);
            ItemState configState = new ItemState(defaultState.getName(), configIcon, defaultState.getAction(), defaultState.getCondition(), defaultState.getDisplayModifier());

            states.add(configState);
        });

        return states;
    }

    private void writeItemState(@NotNull FileConfig config, @NotNull String path, @NotNull ItemState defaultState) {
        config.set(path + ".Icon", defaultState.getIcon());
    }

    private void loadItems(@NotNull NightPlugin plugin, @NotNull FileConfig config, @NotNull String path) {
        if (!config.contains(path)) {
            this.defaultItems.forEach((id, menuItem) -> {
                String itemPath = path + "." + id;

                config.set(itemPath + ".Item", menuItem.getDefaultState().getIcon());
                config.setArray(itemPath + ".Slots", menuItem.getSlots());
            });
        }

        config.getSection(path).forEach(sId -> {
            String itemPath = path + "." + sId;

            NightItem item = config.getCosmeticItem(itemPath + ".Item");
            int[] slots = config.getIntArray(itemPath + ".Slots");

            this.configItems.put(sId, MenuItem.builder().defaultState(item).slots(slots).build());
        });
    }

    protected void registerAction(@NotNull String id, @NotNull MenuItemAction action) {
        this.actionRegistry.register(id, action);
    }

    protected void registerCondition(@NotNull String id, @NotNull ItemStateCondition condition) {
        this.conditionRegistry.register(id, condition);
    }

    @NotNull
    protected String getRawTitle(@NotNull ViewerContext context) {
        String title = this.menuTitle.get();

        if (this.isPlaceholderIntegrationEnabled()) {
            title = Placeholders.forPlayerWithPAPI(context.getPlayer()).apply(title);
        }

        return title;
    }

    @Override
    @NotNull
    public NightComponent getTitle(@NotNull ViewerContext context) {
        return NightMessage.parse(this.getRawTitle(context));
    }

    @Override
    @NotNull
    public MenuType getType(@NotNull MenuViewer viewer) {
        return this.menuType.get();
    }

    protected final boolean showMenu(@NotNull MenuRegistry registry, @NotNull Player player, @Nullable Object object) {
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
        viewer.setCurrentObject(object);
        viewer.renderMenu(this, object);
        registry.registerViewer(player, this);
        return true;
    }

    protected abstract void onLoad(@NotNull FileConfig config);

    protected abstract void onClick(@NotNull ViewerContext context, @NotNull InventoryClickEvent event);

    protected abstract void onDrag(@NotNull ViewerContext context, @NotNull InventoryDragEvent event);

    protected abstract void onClose(@NotNull ViewerContext context, @NotNull InventoryCloseEvent event);

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
    public void refresh(@NotNull Player player) {
        MenuViewer viewer = this.getViewer(player);
        if (viewer == null) return;

        viewer.refresh();
    }

    @Override
    public void close() {
        this.getViewers().forEach(viewer -> this.close(viewer.getPlayer()));
    }

    @Override
    public void close(@NotNull Player player) {
        this.close(player.getUniqueId());
    }

    @Override
    public void close(@NotNull UUID playerId) {
        MenuViewer viewer = this.getViewer(playerId);
        if (viewer == null) return;

        viewer.closeMenu();
    }

    @Override
    public void handleClick(@NotNull Player player, @NotNull InventoryClickEvent event, @NotNull NightPlugin plugin) {
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
    public void handleDrag(@NotNull Player player, @NotNull InventoryDragEvent event) {
        MenuViewer viewer = this.getViewer(player);
        if (viewer == null) return;

        event.setCancelled(true);

        ViewerContext context = viewer.createContext();

        this.onDrag(context, event);
    }

    @Override
    public void handleClose(@NotNull Player player, @NotNull InventoryCloseEvent event, @NotNull MenuRegistry menuRegistry) {
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
    public boolean isViewer(@NotNull Player player) {
        return this.getViewer(player) != null;
    }

    @Override
    public boolean isViewer(@NotNull UUID playerId) {
        return this.getViewer(playerId) != null;
    }

    @NotNull
    protected MenuViewer getOrCreateViewer(@NotNull Player player) {
        return this.viewers.computeIfAbsent(player.getUniqueId(), k -> new MenuViewer(player));
    }

    @Override
    @Nullable
    public MenuViewer getViewer(@NotNull Player player) {
        return this.getViewer(player.getUniqueId());
    }

    @Override
    @Nullable
    public MenuViewer getViewer(@NotNull UUID playerId) {
        return this.viewers.get(playerId);
    }

    @Override
    @NotNull
    public Set<MenuViewer> getViewers() {
        return Set.copyOf(this.viewers.values());
    }

    protected void addNextPageItem(@NotNull Material material, int... slots) {
        this.addDefaultButton("next_page", MenuItem.builder()
            .defaultState(ItemState.defaultBuilder()
                .icon(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_NEXT_PAGE).hideAllComponents())
                .action(MenuItemActions.NEXT_PAGE)
                .condition(ItemStateConditions.NEXT_PAGE)
                .build()
            )
            .slots(slots)
            .build()
        );
    }

    protected void addPreviousPageItem(@NotNull Material material, int... slots) {
        this.addDefaultButton("previous_page", MenuItem.builder()
            .defaultState(ItemState.defaultBuilder()
                .icon(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_PREVIOUS_PAGE).hideAllComponents())
                .action(MenuItemActions.PREVIOUS_PAGE)
                .condition(ItemStateConditions.PREVIOUS_PAGE)
                .build()
            )
            .slots(slots)
            .build()
        );
    }

    protected void addBackgroundItem(@NotNull Material material, int... slots) {
        this.addDefaultItem(BukkitThing.getValue(material) + "_" + UUID.randomUUID().toString().substring(0, 5), MenuItem.builder()
            .defaultState(NightItem.fromType(material).hideAllComponents().setHideTooltip(true))
            .slots(slots)
            .build()
        );
    }

    protected void addDefaultButton(@NotNull String id, @NotNull MenuItem menuItem) {
        this.defaultButtons.put(id, menuItem);
    }

    protected void addDefaultItem(@NotNull String id, @NotNull MenuItem menuItem) {
        this.defaultItems.put(id, menuItem);
    }

    @Override
    @NotNull
    public ActionRegistry getActionRegistry() {
        return this.actionRegistry;
    }

    @Override
    @NotNull
    public ConditionRegistry getConditionRegistry() {
        return this.conditionRegistry;
    }

    @Override
    @NotNull
    public Map<String, MenuItem> getItemsToDisplay() {
        Map<String, MenuItem> items = new LinkedHashMap<>();
        if (this.configured) {
            items.putAll(this.configItems);
            items.putAll(this.configButtons);
        }
        else {
            items.putAll(this.defaultItems);
            items.putAll(this.defaultButtons);
        }
        return items;
    }

    @Override
    @NotNull
    public Map<String, MenuItem> getDefaultButtons() {
        return this.defaultButtons;
    }

    @Override
    @NotNull
    public Map<String, MenuItem> getConfigButtons() {
        return this.configButtons;
    }

    @Override
    @NotNull
    public Map<String, MenuItem> getDefaultItems() {
        return this.defaultItems;
    }

    @Override
    @NotNull
    public Map<String, MenuItem> getConfigItems() {
        return this.configItems;
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
