package su.nightexpress.nightcore.bridge.spigot.dialog;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.jspecify.annotations.NonNull;

import com.google.gson.JsonElement;

import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;

public class SpigotDialogListener implements Listener {

    private final DialogClickHandler handler;

    public SpigotDialogListener(@NonNull DialogClickHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomClick(PlayerCustomClickEvent event) {
        NamespacedKey id = event.getId();
        Player player = event.getPlayer();
        JsonElement element = event.getData();

        NightNbtHolder response = element == null ? null : NightNbtHolder.fromJson(element);

        DialogClickResult result = new DialogClickResult(player, id, response);
        this.handler.handleClick(result);
    }
}
