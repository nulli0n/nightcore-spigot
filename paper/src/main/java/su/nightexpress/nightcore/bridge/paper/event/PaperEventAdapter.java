package su.nightexpress.nightcore.bridge.paper.event;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.bridge.paper.text.PaperTextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class PaperEventAdapter implements EventAdapter {

    private final PaperTextComponentAdapter componentAdapter;

    public PaperEventAdapter(@NotNull PaperTextComponentAdapter componentAdapter) {
        this.componentAdapter = componentAdapter;
    }

    @Override
    public void setJoinMessage(@NotNull PlayerJoinEvent event, @Nullable NightComponent component) {
        event.joinMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }

    @Override
    public void setQuitMessage(@NotNull PlayerQuitEvent event, @Nullable NightComponent component) {
        event.quitMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }

    @Override
    public void setDeathMessage(@NotNull PlayerDeathEvent event, @Nullable NightComponent component) {
        event.deathMessage(component == null ? null : this.componentAdapter.adaptComponent(component));
    }
}
