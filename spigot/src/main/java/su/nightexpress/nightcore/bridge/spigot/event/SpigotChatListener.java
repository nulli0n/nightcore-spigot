package su.nightexpress.nightcore.bridge.spigot.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatListenerCallback;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;

public class SpigotChatListener implements Listener {

    private final SpigotBridge                  bridge;
    private final UniversalChatListenerCallback callback;

    public SpigotChatListener(@NotNull SpigotBridge bridge, @NotNull UniversalChatListenerCallback callback) {
        this.bridge = bridge;
        this.callback = callback;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLowest(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.LOWEST, event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChatLowe(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.LOW, event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatNormal(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.NORMAL, event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatHigh(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.HIGH, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHighest(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.HIGHEST, event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatMonitor(AsyncPlayerChatEvent event) {
        this.handleChat(EventPriority.MONITOR, event);
    }

    private void handleChat(@NotNull EventPriority eventPriority, @NotNull AsyncPlayerChatEvent event) {
        this.callback.run(eventPriority, () -> new SpigotChatEvent(this.bridge, event));
    }
}
