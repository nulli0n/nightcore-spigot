package su.nightexpress.nightcore.bridge.chat;

import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface UniversalChatListenerCallback {

    void run(@NotNull EventPriority priority, @NotNull Supplier<UniversalChatEvent> event);
}
