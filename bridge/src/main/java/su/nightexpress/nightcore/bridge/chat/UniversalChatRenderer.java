package su.nightexpress.nightcore.bridge.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface UniversalChatRenderer {

    @NotNull NightComponent render(@NotNull Player source, @NotNull String sourceDisplayName, @NotNull String message, @Nullable CommandSender viewer);
}
