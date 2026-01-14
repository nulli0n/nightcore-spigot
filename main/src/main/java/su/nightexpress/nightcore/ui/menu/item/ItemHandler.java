package su.nightexpress.nightcore.ui.menu.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.data.LinkHandler;
import su.nightexpress.nightcore.ui.menu.data.Linked;

import java.util.UUID;

@Deprecated
public class ItemHandler {

    public static final String RETURN        = "return";
    public static final String CLOSE         = "close";
    public static final String NEXT_PAGE     = "page_next";
    public static final String PREVIOUS_PAGE = "page_previous";
    public static final String USER_SKIN     = "user_skin";

    private final String      name;
    private final ItemClick   click;
    private final ItemOptions options;

    public ItemHandler(@NotNull String name) {
        this(name, null, null);
    }

    public ItemHandler(@NotNull String name, @Nullable ItemClick click) {
        this(name, click, null);
    }

    public ItemHandler(@NotNull String name, @Nullable ItemClick click, @Nullable ItemOptions options) {
        this.name = name.toLowerCase();
        this.click = click;
        this.options = options;
    }

    @NotNull
    private static String randomName() {
        return UUID.randomUUID().toString();
    }

    /**
     * The main purpose of this method is to quickly create ItemHandler object for non-configurable GUIs.
     * <br><br>
     * Do NOT use this for items requiring specific handler name.
     * @param click Click action
     * @return ItemHandler with a random UUID as a name.
     */
    @NotNull
    public static ItemHandler forClick(@NotNull ItemClick click) {
        return forClick(click, null);
    }

    /**
     * The main purpose of this method is to quickly create ItemHandler object for non-configurable GUIs.
     * <br><br>
     * Do NOT use this for items requiring specific handler name.
     * @param click Click action
     * @return ItemHandler with a random UUID as a name.
     */
    @NotNull
    public static ItemHandler forClick(@NotNull ItemClick click, @Nullable ItemOptions options) {
        return new ItemHandler(randomName(), click, options);
    }

    @NotNull
    public static ItemHandler forUserSkin(@NotNull Menu menu) {
        return new ItemHandler(USER_SKIN,
            (viewer, event) -> {},
            ItemOptions.builder().setDisplayModifier((viewer, nightItem) -> nightItem.setPlayerProfile(viewer.getPlayer())).build()
        );
    }

    @NotNull
    public static ItemHandler forNextPage(@NotNull Menu menu) {
        return new ItemHandler(NEXT_PAGE,
            (viewer, event) -> {
                if (viewer.getPage() < viewer.getPages()) {
                    viewer.setPage(viewer.getPage() + 1);
                    viewer.setRebuildMenu(true);
                    menu.flush(viewer.getPlayer());
                }
            },
            ItemOptions.builder().setVisibilityPolicy(viewer -> viewer.getPage() < viewer.getPages()).build()
        );
    }

    @NotNull
    public static ItemHandler forPreviousPage(@NotNull Menu menu) {
        return new ItemHandler(PREVIOUS_PAGE,
            (viewer, event) -> {
                if (viewer.getPage() > 1) {
                    viewer.setPage(viewer.getPage() - 1);
                    viewer.setRebuildMenu(true);
                    menu.flush(viewer.getPlayer());
                }
            },
            ItemOptions.builder().setVisibilityPolicy(viewer -> viewer.getPage() > 1).build()
        );
    }

    @NotNull
    public static ItemHandler forClose(@NotNull Menu menu) {
        return new ItemHandler(CLOSE, (viewer, event) -> menu.runNextTick(() -> viewer.getPlayer().closeInventory()));
    }

    @NotNull
    public static ItemHandler forReturn(@NotNull Menu menu, @NotNull ItemClick click) {
        return forReturn(menu, click, null);
    }

    @NotNull
    public static ItemHandler forReturn(@NotNull Menu menu, @NotNull ItemClick click, @Nullable ItemOptions options) {
        return new ItemHandler(RETURN, click, options);
    }

    @NotNull
    public static <T> ItemHandler forLink(@NotNull Linked<T> menu, @NotNull LinkHandler<T> handler) {
        return forLink(menu, handler, null);
    }

    @NotNull
    public static <T> ItemHandler forLink(@NotNull Linked<T> menu, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        return new ItemHandler(randomName(), menu.manageLink(handler), options);
    }

    @NotNull
    public static <T> ItemHandler forLink(@NotNull String name, @NotNull Linked<T> menu, @NotNull LinkHandler<T> handler) {
        return forLink(name, menu, handler, null);
    }

    @NotNull
    public static <T> ItemHandler forLink(@NotNull String name, @NotNull Linked<T> menu, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        return new ItemHandler(name, menu.manageLink(handler), options);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public ItemClick getClick() {
        return this.click;
    }

    @Nullable
    public ItemOptions getOptions() {
        return this.options;
    }
}
