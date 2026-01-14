package su.nightexpress.nightcore.bridge.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Set;
import java.util.function.Consumer;

public interface UniversalChatEvent {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    @NotNull Player getPlayer();

    @NotNull Set<CommandSender> viewers();

    void editViewers(@NotNull Consumer<Set<CommandSender>> consumer);

    void renderer(@NotNull UniversalChatRenderer renderer);

    @NotNull String message();

    void message(@NotNull NightComponent component);
}
