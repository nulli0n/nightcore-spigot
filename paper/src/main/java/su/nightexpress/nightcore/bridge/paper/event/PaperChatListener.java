package su.nightexpress.nightcore.bridge.paper.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;

public class PaperChatListener implements Listener {

    private final PaperBridge                   bridge;
    private final UniversalChatListenerCallback callback;

    public PaperChatListener(@NonNull PaperBridge bridge, @NonNull UniversalChatListenerCallback callback) {
        this.bridge = bridge;
        this.callback = callback;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLowest(AsyncChatEvent event) {
        this.handleChat(EventPriority.LOWEST, event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChatLowe(AsyncChatEvent event) {
        this.handleChat(EventPriority.LOW, event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatNormal(AsyncChatEvent event) {
        this.handleChat(EventPriority.NORMAL, event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatHigh(AsyncChatEvent event) {
        this.handleChat(EventPriority.HIGH, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHighest(AsyncChatEvent event) {
        this.handleChat(EventPriority.HIGHEST, event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatMonitor(AsyncChatEvent event) {
        this.handleChat(EventPriority.MONITOR, event);
    }

    private void handleChat(@NonNull EventPriority eventPriority, @NonNull AsyncChatEvent event) {
        this.callback.run(eventPriority, () -> new PaperChatEvent(this.bridge, event));
    }
}
