package su.nightexpress.nightcore.userdata;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.manager.AbstractListener;

public class UserDataListener extends AbstractListener<NightPlugin> {

    private final UserDataManager manager;

    public UserDataListener(@NonNull NightPlugin plugin, @NonNull UserDataManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        this.manager.handlePreLoginEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        this.manager.handleJoinEvent(event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        this.manager.handleQuitEvent(event);
    }
}
