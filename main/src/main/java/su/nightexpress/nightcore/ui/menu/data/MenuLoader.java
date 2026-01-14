package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.click.ClickKey;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.*;

@Deprecated
public class MenuLoader {

    private static final String ITEM_SECTION = "Content";

    private final Menu       menu;
    private final FileConfig config;

    private final Map<String, MenuItem>    defaultItems;
    private final Map<String, ItemHandler> handlerMap;

    public MenuLoader(@NotNull Menu menu, @NotNull FileConfig config) {
        this.menu = menu;
        this.config = config;
        this.defaultItems = new LinkedHashMap<>();
        this.handlerMap = new LinkedHashMap<>();

        this.addHandler(ItemHandler.forClose(menu));
        this.addHandler(ItemHandler.forUserSkin(menu));
        if (menu instanceof Filled<?>) {
            this.addHandler(ItemHandler.forNextPage(menu));
            this.addHandler(ItemHandler.forPreviousPage(menu));
        }
    }

    public void addDefaultItem(@NotNull MenuItem.Builder builder) {
        this.addDefaultItem(builder.build());
    }

    public void addDefaultItem(@NotNull MenuItem menuItem) {
        NightItem item = menuItem.getItem();
        ItemHandler handler = menuItem.getHandler();

        String name;
        if (handler != null) {
            name = handler.getName();
            this.addHandler(handler);
        }
        else {
            String displayName = item.getDisplayName();
            String stripped = displayName == null ? null : Strings.filterForVariable(NightMessage.stripTags(displayName));
            if (stripped != null && !stripped.isBlank()) {
                name = stripped;
            }
            else name = BukkitThing.getValue(item.getMaterial());
        }

        name = LowerCase.INTERNAL.apply(name);

        if (this.defaultItems.containsKey(name)) {
            name += "_" + UUID.randomUUID().toString().substring(0, 8);
        }

        this.defaultItems.put(name, menuItem);
    }

    @NotNull
    public ItemHandler addHandler(@NotNull String name, @NotNull ItemClick click) {
        return this.addHandler(new ItemHandler(name, click));
    }

    @NotNull
    public ItemHandler addHandler(@NotNull ItemHandler handler) {
        this.handlerMap.put(handler.getName(), handler);
        return handler;
    }

    public void loadSettings() {
        if (config.contains("Settings.Inventory_Type")) {
            InventoryType oldType = config.getEnum("Settings.Inventory_Type", InventoryType.class, InventoryType.CHEST);
            int oldSize = config.getInt("Settings.Size", 27);

            MenuType newType = getNewType(oldType, oldSize);

            config.set("Settings.MenuType", BukkitThing.getAsString(newType));
            config.remove("Settings.Inventory_Type");
            config.remove("Settings.Size");
        }

        MenuType menuType = BukkitThing.getMenuType(ConfigValue.create("Settings.MenuType", BukkitThing.getAsString(this.menu.getMenuType())).read(config));

        this.menu.setMenuType(menuType == null ? MenuType.GENERIC_9X3 : menuType);
        this.menu.setTitle(ConfigValue.create("Settings.Title", this.menu.getTitle()).read(config));
        this.menu.setAutoRefreshInterval(ConfigValue.create("Settings.Auto_Refresh", this.menu.getAutoRefreshInterval()).read(config));
        this.menu.setApplyPlaceholderAPI(ConfigValue.create("Settings.PlaceholderAPI.Enabled", this.menu.isApplyPlaceholderAPI()).read(config));
    }

    private static MenuType getNewType(InventoryType oldType, int oldSize) {
        MenuType newType;
        if (oldType == InventoryType.CHEST) {
            if (oldSize == 54) newType = MenuType.GENERIC_9X6;
            else if (oldSize == 45) newType = MenuType.GENERIC_9X5;
            else if (oldSize == 36) newType = MenuType.GENERIC_9X4;
            else if (oldSize == 27) newType = MenuType.GENERIC_9X3;
            else if (oldSize == 18) newType = MenuType.GENERIC_9X2;
            else newType = MenuType.GENERIC_9X1;
        }
        else {
            newType = switch (oldType) {
                case CARTOGRAPHY -> MenuType.CARTOGRAPHY_TABLE;
                case ANVIL -> MenuType.ANVIL;
                case LOOM -> MenuType.LOOM;
                case SMITHING -> MenuType.SMITHING;
                case GRINDSTONE -> MenuType.GRINDSTONE;
                case STONECUTTER -> MenuType.STONECUTTER;
                case SMOKER -> MenuType.SMOKER;
                case BEACON -> MenuType.BEACON;
                case HOPPER -> MenuType.HOPPER;
                case BREWING -> MenuType.BREWING_STAND;
                case DROPPER, DISPENSER -> MenuType.GENERIC_3X3;
                case FURNACE -> MenuType.FURNACE;
                case LECTERN -> MenuType.LECTERN;
                case ENCHANTING -> MenuType.ENCHANTMENT;
                case BLAST_FURNACE -> MenuType.BLAST_FURNACE;
                default -> MenuType.GENERIC_9X3;
            };
        }
        return newType;
    }

    public void loadItems() {
        if (!this.config.contains(ITEM_SECTION)) {
            this.defaultItems.forEach((id, menuItem) -> {
                this.writeItem(menuItem, ITEM_SECTION + "." + id);
            });
        }

        this.config.getSection(ITEM_SECTION).forEach(sId -> {
            MenuItem menuItem = this.readItem(ITEM_SECTION + "." + sId);
            this.menu.addItem(menuItem);
        });
    }

    public void loadComments() {
        List<String> list = Lists.newList(
            "=".repeat(20) + " [SECTION] SETTINGS " + "=".repeat(20),
            "> [MenuType] : String | Represents the menu type. Allowed values: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/MenuType.html",
            "> [Title] : String | Sets menu title.",
            "> [Auto_Refresh] : Integer | Defines menu refresh rate in seconds. Set 0 to disable.",
            "> [PlaceholderAPI -> Enabled] : Boolean | When enabled, applies " + Plugins.PLACEHOLDER_API + " placeholders for all items in the menu per player.",
            " ",
            "=".repeat(20) + " [SECTION] CONTENT " + "=".repeat(20),
            "You can freely edit items in this section as you wish (add, remove, modify items).",
            "> [Item] : Section | Item to display.",
            "    [*] Navigate to " + Placeholders.URL_WIKI_ITEMS + " for a list of available options.",
            "> [Priority] : Integer | Item priority. Higher values will override other item(s) in the same slot(s).",
            "> [Slots] : Int Array | Item slots, starts from 0. Split with commas.",
            "    Slots: '0,4,9,10'",
            "> [Type] : String | Defines item click action.",
            "    [*] Available types: [" + String.join(", ", handlerMap.keySet().stream().map(str -> "'" + str + "'").toList()) + "]",
            "> [Click_Commands] : Section | Executes commands on click with " + Plugins.PLACEHOLDER_API + " support.",
            "    [*] Works only if [Type] is not set (null).",
            "    [*] Available click types: [" + Enums.inline(ClickKey.class) + "]",
            "    [*] Use prefix '" + Players.PLAYER_COMMAND_PREFIX + "' to run command by a player.",
            "    Click_Commands:",
            "      " + ClickKey.LEFT.name() + ":",
            "      - say Hello",
            "      - give " + Placeholders.PLAYER_NAME + " diamond 1",
            "      - " + Players.PLAYER_COMMAND_PREFIX + " warp shop",
            "=".repeat(50)
        );

        this.config.setComments("Settings", list);
    }

    @NotNull
    protected MenuItem readItem(@NotNull String path) {
        NightItem item = config.getCosmeticItem(path + ".Item");
        int priority = config.getInt(path + ".Priority");
        int[] slots = config.getIntArray(path + ".Slots");
        String handlerName = config.getString(path + ".Type", Placeholders.DEFAULT);

        ItemHandler typedHandler = this.handlerMap.get(handlerName.toLowerCase());
        ItemHandler commandHandler;

        if (config.contains(path + ".Click_Commands")) {
            Map<ClickKey, List<String>> commandMap = new HashMap<>();
            for (String sType : config.getSection(path + ".Click_Commands")) {
                ClickKey clickType = Enums.get(sType, ClickKey.class);
                if (clickType == null) continue;

                List<String> commands = config.getStringList(path + ".Click_Commands." + sType);
                if (commands.isEmpty()) continue;

                commandMap.put(clickType, commands);
            }

            commandHandler = ItemHandler.forClick((viewer, event) -> {
                List<String> commands = commandMap.getOrDefault(ClickKey.from(event), Collections.emptyList());
                commands.forEach(command -> Players.dispatchCommand(viewer.getPlayer(), command));
            });
        }
        else {
            commandHandler = null;
        }

        ItemHandler handler = typedHandler == null ? commandHandler : typedHandler;

        return new MenuItem(item, priority, slots, handler);
    }

    protected void writeItem(@NotNull MenuItem menuItem, @NotNull String path) {
        this.config.set(path + ".Priority", menuItem.getPriority());
        this.config.set(path + ".Item", menuItem.getItem());
        this.config.setIntArray(path + ".Slots", menuItem.getSlots());
        this.config.set(path + ".Type", menuItem.getHandler() == null ? "null" : menuItem.getHandler().getName());
    }
}
