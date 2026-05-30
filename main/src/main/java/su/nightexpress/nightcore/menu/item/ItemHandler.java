package su.nightexpress.nightcore.menu.item;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickAction;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Deprecated
public class ItemHandler {

    public static final String RETURN        = "return";
    public static final String CLOSE         = "close";
    public static final String NEXT_PAGE     = "page_next";
    public static final String PREVIOUS_PAGE = "page_previous";

    private final String                name;
    private final List<ClickAction>     clickActions;
    private final Predicate<MenuViewer> visibilityPolicy;

    public ItemHandler() {
        this(Placeholders.DEFAULT, null, null);
    }

    public ItemHandler(@NonNull String name) {
        this(name, null, null);
    }

    public ItemHandler(@NonNull String name, @Nullable ClickAction clickAction) {
        this(name, clickAction, null);
    }

    public ItemHandler(@NonNull String name, @Nullable ClickAction clickAction,
                       @Nullable Predicate<MenuViewer> visibilityPolicy) {
        this.name = name.toLowerCase();
        this.clickActions = new ArrayList<>();
        this.visibilityPolicy = visibilityPolicy;

        if (clickAction != null) {
            this.getClickActions().add(clickAction);
        }
    }

    /**
     * The main purpose of this method is to quickly create ItemHandler object for non-configurable GUIs.
     * <br><br>
     * Do NOT use this for items requiring specific handler name.
     * 
     * @param action Click action
     * @return ItemHandler with a random UUID as a name.
     */
    @NonNull
    public static ItemHandler forClick(@NonNull ClickAction action) {
        return new ItemHandler(UUID.randomUUID().toString(), action);
    }

    @NonNull
    public static ItemHandler forNextPage(@NonNull Menu menu) {
        return new ItemHandler(NEXT_PAGE, (viewer, event) -> {
            if (viewer.getPage() < viewer.getPages()) {
                viewer.setPage(viewer.getPage() + 1);
                viewer.setUpdateTitle(true);
                menu.open(viewer.getPlayer());
            }
        }, viewer -> viewer.getPage() < viewer.getPages());
    }

    @NonNull
    public static ItemHandler forPreviousPage(@NonNull Menu menu) {
        return new ItemHandler(PREVIOUS_PAGE, (viewer, event) -> {
            if (viewer.getPage() > 1) {
                viewer.setPage(viewer.getPage() - 1);
                viewer.setUpdateTitle(true);
                menu.open(viewer.getPlayer());
            }
        }, viewer -> viewer.getPage() > 1);
    }

    @NonNull
    public static ItemHandler forClose(@NonNull Menu menu) {
        return new ItemHandler(CLOSE, (viewer, event) -> menu.runNextTick(() -> viewer.getPlayer().closeInventory()));
    }

    @NonNull
    public static ItemHandler forReturn(@NonNull Menu menu, @NonNull ClickAction action) {
        return new ItemHandler(RETURN, action);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public List<ClickAction> getClickActions() {
        return clickActions;
    }

    @Nullable
    public Predicate<MenuViewer> getVisibilityPolicy() {
        return visibilityPolicy;
    }
}
