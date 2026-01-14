package su.nightexpress.nightcore.bridge.event;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface EventAdapter {

    void setJoinMessage(@NotNull PlayerJoinEvent event, @Nullable NightComponent component);

    void setQuitMessage(@NotNull PlayerQuitEvent event, @Nullable NightComponent component);

    void setDeathMessage(@NotNull PlayerDeathEvent event, @Nullable NightComponent component);
}
