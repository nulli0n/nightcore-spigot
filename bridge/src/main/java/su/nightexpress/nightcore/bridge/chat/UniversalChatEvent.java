package su.nightexpress.nightcore.bridge.chat;

import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface UniversalChatEvent {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    @NonNull
    Player getPlayer();

    @NonNull
    Set<CommandSender> viewers();

    void editViewers(@NonNull Consumer<Set<CommandSender>> consumer);

    void renderer(@NonNull UniversalChatRenderer renderer);

    @NonNull
    String message();

    void message(@NonNull NightComponent component);
}
