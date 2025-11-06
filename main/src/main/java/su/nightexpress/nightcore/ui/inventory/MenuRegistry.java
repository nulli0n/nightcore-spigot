package su.nightexpress.nightcore.ui.inventory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.ui.inventory.listener.MenuInventoryListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MenuRegistry extends AbstractManager<NightCore> {

    private final Map<UUID, Menu> activeMenus;

    public MenuRegistry(@NotNull NightCore core) {
        super(core);
        this.activeMenus = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.addListener(new MenuInventoryListener(this.plugin, this));
        this.addTask(this::tickMenus, 1);
    }

    @Override
    protected void onShutdown() {
        this.getActiveMenus().forEach(Menu::close);
        this.activeMenus.clear();
    }

    public void tickMenus() {
        this.getActiveMenus().forEach(Menu::tick);
    }

    public void registerViewer(@NotNull Player player, @NotNull Menu menu) {
        this.activeMenus.put(player.getUniqueId(), menu);
    }

    public void unregisterViewer(@NotNull Player player) {
        this.activeMenus.remove(player.getUniqueId());
    }

    @Nullable
    public Menu getActiveMenu(@NotNull Player player) {
        return this.getActiveMenu(player.getUniqueId());
    }

    @Nullable
    public Menu getActiveMenu(@NotNull UUID playerId) {
        return this.activeMenus.get(playerId);
    }

    @NotNull
    public Set<Menu> getActiveMenus() {
        return Set.copyOf(this.activeMenus.values());
    }
}
