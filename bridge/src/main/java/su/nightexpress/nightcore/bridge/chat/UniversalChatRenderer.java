package su.nightexpress.nightcore.bridge.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface UniversalChatRenderer {

    @NonNull
    NightComponent render(@NonNull Player source, @NonNull String sourceDisplayName, @NonNull String message,
                          @Nullable CommandSender viewer);
}
