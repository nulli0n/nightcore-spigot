package su.nightexpress.nightcore.bridge.chat;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface UniversalChatEventHandler {

    void handle(@NotNull UniversalChatEvent event);
}
