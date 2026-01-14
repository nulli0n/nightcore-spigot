package su.nightexpress.nightcore.bridge.paper.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEvent;
import su.nightexpress.nightcore.bridge.chat.UniversalChatRenderer;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.bridge.paper.PaperUtils;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PaperChatEvent implements UniversalChatEvent {

    private final PaperBridge bridge;
    private final AsyncChatEvent backend;

    public PaperChatEvent(@NotNull PaperBridge bridge, @NotNull AsyncChatEvent backend) {
        this.bridge = bridge;
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
        return this.backend.viewers().stream().filter(audience -> audience instanceof CommandSender).map(CommandSender.class::cast).collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void editViewers(@NotNull Consumer<Set<CommandSender>> consumer) {
        Set<CommandSender> viewers = this.viewers();
        consumer.accept(viewers);
        this.backend.viewers().clear();
        this.backend.viewers().addAll(viewers);
    }

    @Override
    public void renderer(@NotNull UniversalChatRenderer renderer) {
        this.backend.renderer((source, sourceDisplayName, message, viewer) -> {
            String rawDisplayName = PaperUtils.serializeComponent(sourceDisplayName);
            String rawMessage = PaperUtils.serializeComponent(message);

            NightComponent component = renderer.render(source, rawDisplayName, rawMessage, (CommandSender) viewer);
            return this.bridge.getTextComponentAdapter().adaptComponent(component);
        });
    }

    @Override
    @NotNull
    public String message() {
        return PaperUtils.serializeComponent(this.backend.message());
    }

    @Override
    public void message(@NotNull NightComponent component) {
        this.backend.message(this.bridge.getTextComponentAdapter().adaptComponent(component));
    }
}
