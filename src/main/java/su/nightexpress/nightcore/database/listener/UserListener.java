package su.nightexpress.nightcore.database.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightDataPlugin;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.database.DataUser;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.database.AbstractUserManager;

import java.util.UUID;

public class UserListener<P extends NightDataPlugin<U>, U extends DataUser> extends AbstractListener<P> {

    private final AbstractUserManager<? extends NightDataPlugin<U>, U> userManager;

    public UserListener(@NotNull P plugin) {
        super(plugin);
        this.userManager = plugin.getUserManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        UUID uuid = event.getUniqueId();
        U user;
        if (!this.userManager.isCreated(uuid)) {
            user = this.userManager.createUserData(uuid, event.getName());
            plugin.getData().addUser(user);
            if (CoreConfig.USER_DEBUG_ENABLED.get()) {
                plugin.info("Created new data for: '" + uuid + "'");
            }
        }
        else user = this.userManager.getUserData(uuid);

        if (user != null) {
            this.userManager.cachePermanent(user);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserQuit(PlayerQuitEvent event) {
        this.userManager.unload(event.getPlayer());
    }
}
