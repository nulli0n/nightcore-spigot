package su.nightexpress.nightcore.bridge.spigot.event;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.chat.UniversalChatEvent;
import su.nightexpress.nightcore.bridge.chat.UniversalChatRenderer;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class SpigotChatEvent implements UniversalChatEvent {

    private final AsyncPlayerChatEvent backend;

    public SpigotChatEvent(@NonNull SpigotBridge bridge, @NonNull AsyncPlayerChatEvent backend) {
        this.backend = backend;
    }

    @Override
    public boolean isCancelled() {
        return this.backend.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.backend.setCancelled(cancelled);
    }

    @Override
    @NonNull
    public Player getPlayer() {
        return this.backend.getPlayer();
    }

    @Override
    @NonNull
    public Set<CommandSender> viewers() {
        return this.backend.getRecipients().stream().map(player -> (CommandSender) player).collect(Collectors.toSet());
    }

    @Override
    public void editViewers(@NonNull Consumer<Set<CommandSender>> consumer) {
        consumer.accept(this.viewers());
    }

    @Override
    public void renderer(@NonNull UniversalChatRenderer renderer) {
        NightComponent component = renderer.render(this.backend.getPlayer(), this.backend.getPlayer().getDisplayName(),
            this.message(), null);
        this.backend.setFormat(component.toLegacy());
    }

    @Override
    @NonNull
    public String message() {
        return this.backend.getMessage();
    }

    @Override
    public void message(@NonNull NightComponent component) {
        this.backend.setMessage(component.toLegacy());
    }
}
