package su.nightexpress.nightcore.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.Menu;

public class MenuOpenEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Menu   menu;

    private boolean cancelled;

    public MenuOpenEvent(@NonNull Player player, @NonNull Menu menu) {
        this.player = player;
        this.menu = menu;
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NonNull
    public Player getPlayer() {
        return this.player;
    }

    @NonNull
    public Menu getMenu() {
        return this.menu;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
