package su.nightexpress.nightcore.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.Plugins;

public class CoreListener extends AbstractListener<NightCore> {

    public CoreListener(@NotNull NightCore plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onServiceRegisterEvent(ServiceRegisterEvent event) {
        if (Plugins.isLoaded(Plugins.VAULT)) {
            VaultHook.onServiceRegisterEvent(event);
        }
    }
}
