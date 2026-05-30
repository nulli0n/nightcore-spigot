package su.nightexpress.nightcore.ui.menu;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class MenuRegistry {

    private static final Map<UUID, MenuViewer> VIEWER_BY_ID = new HashMap<>();

    public static void closeAll() {
        getViewers().stream().map(MenuViewer::getMenu).distinct().forEach(Menu::clear);
        VIEWER_BY_ID.clear();
    }

    @NonNull
    public static Set<MenuViewer> getViewers() {
        return new HashSet<>(VIEWER_BY_ID.values());
    }

    @NonNull
    public static Set<MenuViewer> getViewers(@NonNull Menu menu) {
        return getViewers().stream().filter(viewer -> viewer.isMenu(menu)).collect(Collectors.toSet());
    }

    public static void closeMenu(@NonNull Player player) {
        if (isViewer(player)) {
            player.closeInventory();
        }
    }

    public static void assign(@NonNull MenuViewer viewer) {
        VIEWER_BY_ID.put(viewer.getPlayer().getUniqueId(), viewer);
    }

    public static void terminate(@NonNull Player player) {
        VIEWER_BY_ID.remove(player.getUniqueId());
    }

    @Nullable
    public static Menu getMenu(@NonNull Player player) {
        MenuViewer viewer = getViewer(player);
        return viewer == null ? null : viewer.getMenu();
    }

    @Nullable
    public static MenuViewer getViewer(@NonNull Player player) {
        return VIEWER_BY_ID.get(player.getUniqueId());
    }

    public static boolean isViewer(@NonNull Player player) {
        return getViewer(player) != null;
    }
}
