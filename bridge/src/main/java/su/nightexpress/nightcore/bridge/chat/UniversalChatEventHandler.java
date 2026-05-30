package su.nightexpress.nightcore.bridge.chat;

import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface UniversalChatEventHandler {

    void handle(@NonNull UniversalChatEvent event);
}
