package su.nightexpress.nightcore.chat;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;
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

    public ChatManager(@NonNull NightCore plugin) {
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

    public void addHandler(@NonNull EventPriority priority, @NonNull UniversalChatEventHandler handler) {
        this.handlerMap.computeIfAbsent(priority, k -> new ArrayList<>()).add(handler);
    }

    public void removeHandler(@NonNull UniversalChatEventHandler handler) {
        this.handlerMap.values().forEach(handlers -> handlers.remove(handler));
    }

    private void registerListener() {
        Listener listener = Software.get().createChatListener(this::handleEvent);
        this.plugin.registerListener(listener);
    }

    private void handleEvent(@NonNull EventPriority priority, @NonNull Supplier<UniversalChatEvent> supplier) {
        List<UniversalChatEventHandler> handlers = this.handlerMap.get(priority);
        if (handlers == null || handlers.isEmpty()) return;

        UniversalChatEvent event = supplier.get();

        handlers.forEach(handler -> handler.handle(event));
    }
}
