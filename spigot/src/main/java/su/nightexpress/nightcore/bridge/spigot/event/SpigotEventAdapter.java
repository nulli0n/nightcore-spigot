package su.nightexpress.nightcore.bridge.spigot.event;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class SpigotEventAdapter implements EventAdapter {

    @Override
    public void setJoinMessage(@NonNull PlayerJoinEvent event, @Nullable NightComponent component) {
        event.setJoinMessage(component == null ? null : component.toLegacy());
    }

    @Override
    public void setQuitMessage(@NonNull PlayerQuitEvent event, @Nullable NightComponent component) {
        event.setQuitMessage(component == null ? null : component.toLegacy());
    }

    @Override
    public void setDeathMessage(@NonNull PlayerDeathEvent event, @Nullable NightComponent component) {
        event.setDeathMessage(component == null ? null : component.toLegacy());
    }
}
