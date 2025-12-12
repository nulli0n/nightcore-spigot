package su.nightexpress.nightcore.ui.inventory.viewer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.Menu;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.*;

public class MenuViewer {

    private final Player                  player;
    private final Map<Integer, ItemState> currentDisplay;

    private Menu          currentMenu;
    private Object        currentObject;
    private InventoryView currentView;
    private int           currentPage;

    private int totalPages;
    private long nextClickIn;
    private boolean isRefreshing;

    public MenuViewer(@NotNull Player player/*, @Nullable Object currentObject*/) {
        this.player = player;
        //this.currentObject = currentObject;
        this.currentDisplay = new HashMap<>();
        this.setCurrentPage(1);
        this.setTotalPages(1);
    }

    public void renderMenu(@NotNull Menu menu, @Nullable Object data) {
        this.currentMenu = menu;
        this.currentObject = data;
        this.currentDisplay.clear();
        this.isRefreshing = false;

        ViewerContext context = this.createContext();

        if (this.currentView == null) {
            this.isRefreshing = true;
            this.currentView = Software.get().createView(menu.getType(this), menu.getTitle(context), this.player);
        }
        else {
            this.currentView.getTopInventory().clear();
        }

        Inventory inventory = this.currentView.getTopInventory();
        List<MenuItem> menuItems = new ArrayList<>(menu.getItemsToDisplay().values());

        menu.onPrepare(context, this.currentView, inventory, menuItems);

        menuItems.forEach(menuItem -> {
            ItemState itemState = menuItem.resolveState(context);
            if (!itemState.isVisible()) return;

            NightItem icon = itemState.getIcon();

            if (menu.isPlaceholderIntegrationEnabled()) {
                icon.replacement(replacer -> replacer.replacePlaceholderAPI(this.player));
            }

            itemState.modifyDisplay(context, icon);

            ItemStack itemStack = icon.getItemStack();

            for (int slot : menuItem.getSlots()) {
                if (slot < 0 || slot >= inventory.getSize()) continue;
                inventory.setItem(slot, itemStack);

                this.currentDisplay.put(slot, itemState);
            }
        });

        menu.onReady(context, this.currentView, inventory);

        if (this.isRefreshing) {
            this.player.openInventory(this.currentView);
            menu.onRender(context, this.currentView, inventory);
            this.isRefreshing = false;
        }
    }

    public void navigateForward() {
        if (!this.canNavigateForward()) return;

        this.setCurrentPage(this.currentPage + 1);
        this.refresh();
    }

    public void navigateBackward() {
        if (!this.canNavigateBackward()) return;

        this.setCurrentPage(this.currentPage - 1);
        this.refresh();
    }

    public void refresh() {
        if (this.currentMenu == null) return;

        this.currentView = null; // Render inventory from scratch.
        this.renderMenu(this.currentMenu, this.currentObject);
    }

    public void closeMenu() {
        if (this.currentMenu == null) return;

        this.player.closeInventory();
    }

    public void handleClose(@NotNull InventoryCloseEvent event) {
        this.currentMenu = null;
        this.currentObject = null;
        this.currentView = null;
        this.currentDisplay.clear();
    }

    public void handleClick(@NotNull InventoryClickEvent event) {
        if (this.currentMenu == null) return;

        int slot = event.getRawSlot();

        ItemState clickedState = this.currentDisplay.get(slot);
        if (clickedState != null && clickedState.hasAction()) {
            ActionContext context = new ActionContext(this, this.currentObject, event);

            // Ensure that player still has access to the button.
            if (!clickedState.isVisibleFor(context)) {
                this.refresh();
                return;
            }

            clickedState.performAction(context);
        }
    }

    public boolean canNavigateForward() {
        return this.totalPages > 1 && this.currentPage < this.totalPages;
    }

    public boolean canNavigateBackward() {
        return this.totalPages > 1 && this.currentPage > 1;
    }

    public boolean canClickAgain() {
        return TimeUtil.isPassed(this.nextClickIn);
    }

    public boolean isRefreshing() {
        return this.isRefreshing;
    }

    public void clearClickCooldown() {
        this.setNextClickIn(0L);
    }

    public void setNextClickIn(long nextClickIn) {
        this.nextClickIn = nextClickIn;
    }

    @NotNull
    public ViewerContext createContext() {
        return new ViewerContext(this, this.currentObject);
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @Nullable
    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    @NotNull
    public Optional<Menu> menu() {
        return Optional.ofNullable(this.currentMenu);
    }

    @Nullable
    public InventoryView getCurrentView() {
        return this.currentView;
    }

    public void setCurrentObject(@Nullable Object currentObject) {
        this.currentObject = currentObject;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = Math.max(1, currentPage);
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = Math.max(1, totalPages);
    }
}
