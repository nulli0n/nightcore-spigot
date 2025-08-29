package su.nightexpress.nightcore.bridge.paper.dialog;

import com.google.gson.JsonElement;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.util.NbtUtil;

public class PaperDialogListener implements Listener {

    private final DialogClickHandler handler;

    public PaperDialogListener(@NotNull DialogClickHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomClick(PlayerCustomClickEvent event) {
        if (!(event.getCommonConnection() instanceof PlayerGameConnection gameConnection)) return;

        // No thanks paper, callbacks are not always good and the only way.

        NightNbtHolder response = null;
        BinaryTagHolder tagHolder = event.getTag();
        if (tagHolder != null) {
            Object compound = NbtUtil.parseTag(tagHolder.string());
            JsonElement converted = compound == null ? null : NbtUtil.snbtToJson(compound);

            response = converted == null ? null : NightNbtHolder.fromJson(converted);
        }

        Key key = event.getIdentifier();

        Player player = gameConnection.getPlayer();
        NamespacedKey identifier = new NamespacedKey(key.namespace(), key.value());

        DialogClickResult result = new DialogClickResult(player, identifier, response);
        this.handler.handleClick(result);
    }
}
