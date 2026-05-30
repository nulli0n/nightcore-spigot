package su.nightexpress.nightcore.bridge.paper.event;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.bridge.paper.text.PaperTextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class PaperEventAdapter implements EventAdapter {

    private final PaperTextComponentAdapter componentAdapter;

    public PaperEventAdapter(@NonNull PaperTextComponentAdapter componentAdapter) {
        this.componentAdapter = componentAdapter;
    }

    @Override
    public void setJoinMessage(@NonNull PlayerJoinEvent event, @Nullable NightComponent component) {
        event.joinMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }

    @Override
    public void setQuitMessage(@NonNull PlayerQuitEvent event, @Nullable NightComponent component) {
        event.quitMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }

    @Override
    public void setDeathMessage(@NonNull PlayerDeathEvent event, @Nullable NightComponent component) {
        event.deathMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }
}
