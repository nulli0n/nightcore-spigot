package su.nightexpress.nightcore.ui.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class MenuRegistry {

    private static final Map<UUID, MenuViewer> VIEWER_BY_ID = new HashMap<>();

    public static void closeAll() {
        getViewers().stream().map(MenuViewer::getMenu).distinct().forEach(Menu::clear);
        VIEWER_BY_ID.clear();
    }

    @NotNull
    public static Set<MenuViewer> getViewers() {
        return new HashSet<>(VIEWER_BY_ID.values());
    }

    @NotNull
    public static Set<MenuViewer> getViewers(@NotNull Menu menu) {
        return getViewers().stream().filter(viewer -> viewer.isMenu(menu)).collect(Collectors.toSet());
    }

    public static void closeMenu(@NotNull Player player) {
        if (isViewer(player)) {
            player.closeInventory();
        }
    }

    public static void assign(@NotNull MenuViewer viewer) {
        VIEWER_BY_ID.put(viewer.getPlayer().getUniqueId(), viewer);
    }

    public static void terminate(@NotNull Player player) {
        VIEWER_BY_ID.remove(player.getUniqueId());
    }

    @Nullable
    public static Menu getMenu(@NotNull Player player) {
        MenuViewer viewer = getViewer(player);
        return viewer == null ? null : viewer.getMenu();
    }

    @Nullable
    public static MenuViewer getViewer(@NotNull Player player) {
        return VIEWER_BY_ID.get(player.getUniqueId());
    }

    public static boolean isViewer(@NotNull Player player) {
        return getViewer(player) != null;
    }
}
