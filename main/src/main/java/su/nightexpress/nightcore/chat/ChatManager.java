package su.nightexpress.nightcore.chat;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEventHandler;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEvent;
import su.nightexpress.nightcore.manager.SimpleManager;
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ChatManager extends SimpleManager<NightCore> {

    private final Map<EventPriority, List<UniversalChatEventHandler>> handlerMap;

    public ChatManager(@NotNull NightCore plugin) {
        super(plugin);
        this.handlerMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.registerListener();
    }

    @Override
    protected void onShutdown() {
        this.handlerMap.clear();
    }

    public void addHandler(@NotNull EventPriority priority, @NotNull UniversalChatEventHandler handler) {
        this.handlerMap.computeIfAbsent(priority, k -> new ArrayList<>()).add(handler);
    }

    public void removeHandler(@NotNull UniversalChatEventHandler handler) {
        this.handlerMap.values().forEach(handlers -> handlers.remove(handler));
    }

    private void registerListener() {
        Listener listener = Software.get().createChatListener(this::handleEvent);
        this.plugin.registerListener(listener);
    }

    private void handleEvent(@NotNull EventPriority priority, @NotNull Supplier<UniversalChatEvent> supplier) {
        List<UniversalChatEventHandler> handlers = this.handlerMap.get(priority);
        if (handlers == null || handlers.isEmpty()) return;

        UniversalChatEvent event = supplier.get();

        handlers.forEach(handler -> handler.handle(event));
    }
}
