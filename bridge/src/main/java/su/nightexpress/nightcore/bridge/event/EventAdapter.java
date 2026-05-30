package su.nightexpress.nightcore.bridge.event;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface EventAdapter {

    void setJoinMessage(@NonNull PlayerJoinEvent event, @Nullable NightComponent component);

    void setQuitMessage(@NonNull PlayerQuitEvent event, @Nullable NightComponent component);

    void setDeathMessage(@NonNull PlayerDeathEvent event, @Nullable NightComponent component);
}
