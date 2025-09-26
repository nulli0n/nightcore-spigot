package su.nightexpress.nightcore.integration.currency.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.integration.currency.CurrencyManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class CurrencyListener extends AbstractListener<NightCore> {

    private final CurrencyManager manager;

    public CurrencyListener(@NotNull NightCore plugin, @NotNull CurrencyManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        this.manager.handlePluginLoad(event.getPlugin().getName());
    }
}
