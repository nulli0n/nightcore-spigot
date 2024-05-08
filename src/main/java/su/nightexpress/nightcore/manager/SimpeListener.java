package su.nightexpress.nightcore.manager;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Deprecated
public interface SimpeListener extends Listener {

    void registerListeners();

    default void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }
}
