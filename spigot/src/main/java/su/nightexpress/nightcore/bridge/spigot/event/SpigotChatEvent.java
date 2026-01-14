package su.nightexpress.nightcore.bridge.spigot.event;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEvent;
import su.nightexpress.nightcore.bridge.chat.UniversalChatRenderer;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SpigotChatEvent implements UniversalChatEvent {

    //private final SpigotBridge bridge;
    private final AsyncPlayerChatEvent backend;

    public SpigotChatEvent(@NotNull SpigotBridge bridge, @NotNull AsyncPlayerChatEvent backend) {
        //this.bridge = bridge;
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
    @NotNull
    public Player getPlayer() {
        return this.backend.getPlayer();
    }

    @Override
    @NotNull
    public Set<CommandSender> viewers() {
        return this.backend.getRecipients().stream().map(player -> (CommandSender) player).collect(Collectors.toSet());
    }

    @Override
    public void editViewers(@NotNull Consumer<Set<CommandSender>> consumer) {
        consumer.accept(this.viewers());
    }

    @Override
    public void renderer(@NotNull UniversalChatRenderer renderer) {
        NightComponent component = renderer.render(this.backend.getPlayer(), this.backend.getPlayer().getDisplayName(), this.message(), null);
        this.backend.setFormat(component.toLegacy());
    }

    @Override
    @NotNull
    public String message() {
        return this.backend.getMessage();
    }

    @Override
    public void message(@NotNull NightComponent component) {
        this.backend.setMessage(component.toLegacy());
    }
}
