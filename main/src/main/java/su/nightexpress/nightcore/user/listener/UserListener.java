package su.nightexpress.nightcore.user.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.user.AbstractUserManager;
import su.nightexpress.nightcore.user.UserTemplate;

public class UserListener<P extends NightPlugin, U extends UserTemplate> extends AbstractListener<P> {

    private final AbstractUserManager<P, U> manager;

    public UserListener(@NotNull P plugin, @NotNull AbstractUserManager<P, U> manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserLogin(AsyncPlayerPreLoginEvent event) {
        this.manager.handlePreLogin(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUserJoin(PlayerJoinEvent event) {
        this.manager.handleJoin(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserQuit(PlayerQuitEvent event) {
        this.manager.handleQuit(event);
    }
}
