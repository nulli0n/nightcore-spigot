package su.nightexpress.nightcore.dialog;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.HashSet;

public class DialogListener extends AbstractListener<NightCore> {

    public DialogListener(@NotNull NightCore plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Dialog.stop(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatText(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Dialog dialog = Dialog.get(player);
        if (dialog == null) return;

        event.getRecipients().clear();
        event.setCancelled(true);

        WrappedInput input = new WrappedInput(event);

        this.plugin.runTask(task -> {
            if (input.getTextRaw().equalsIgnoreCase(Dialog.EXIT) || dialog.getHandler().onInput(dialog, input)) {
                Dialog.stop(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Dialog dialog = Dialog.get(player);
        if (dialog == null) return;

        event.setCancelled(true);

        String raw = event.getMessage();
        String text = raw.substring(1);
        if (text.startsWith(Dialog.VALUES)) {
            String[] split = text.split(" ");
            int page = split.length >= 2 ? NumberUtil.getInteger(split[1], 0) : 0;
            boolean auto = split.length >= 3 && Boolean.parseBoolean(split[2]);
            dialog.displaySuggestions(auto, page);
            return;
        }

        AsyncPlayerChatEvent chatEvent = new AsyncPlayerChatEvent(true, player, text, new HashSet<>());
        WrappedInput input = new WrappedInput(chatEvent);

        this.plugin.runTask(task -> {
            if (input.getTextRaw().equalsIgnoreCase(Dialog.EXIT) || dialog.getHandler().onInput(dialog, input)) {
                Dialog.stop(player);
            }
        });
    }
}
