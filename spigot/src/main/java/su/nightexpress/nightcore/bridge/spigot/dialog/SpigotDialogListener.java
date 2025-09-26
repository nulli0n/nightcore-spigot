package su.nightexpress.nightcore.bridge.spigot.dialog;

import com.google.gson.JsonElement;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.DialogKeys;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;

public class SpigotDialogListener implements Listener {

    private final DialogClickHandler handler;

    public SpigotDialogListener(@NotNull DialogClickHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomClick(PlayerCustomClickEvent event) {
        NamespacedKey id = event.getId();
        if (!DialogKeys.isRightNamespace(id)) return;

        Player player = event.getPlayer();
        JsonElement element = event.getData();

        NightNbtHolder response = element == null ? null : NightNbtHolder.fromJson(element);

        DialogClickResult result = new DialogClickResult(player, id, response);
        this.handler.handleClick(result);
    }
}
