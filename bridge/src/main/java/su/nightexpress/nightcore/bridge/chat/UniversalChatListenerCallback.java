package su.nightexpress.nightcore.bridge.chat;

import java.util.function.Supplier;

import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NonNull;

public interface UniversalChatListenerCallback {

    void run(@NonNull EventPriority priority, @NonNull Supplier<UniversalChatEvent> event);
}
