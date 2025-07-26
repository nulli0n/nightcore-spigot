package su.nightexpress.nightcore.bridge.paper.dialog;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.util.NbtUtil;

public class PaperDialogListener implements Listener {

    private final DialogClickHandler handler;

    public PaperDialogListener(@NotNull DialogClickHandler handler) {
        this.handler = handler;
    }

    // TODO Organize

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomClick(PlayerCustomClickEvent event) {
        if (!(event.getCommonConnection() instanceof PlayerGameConnection gameConnection)) {
            //System.out.println("Not a game connection: " + event.getCommonConnection());
            return;
        }

        NightNbtHolder response = null;
        BinaryTagHolder element = event.getTag();
        if (element != null) {
            Object compound = NbtUtil.parseTag(element.string());
            //System.out.println("compound = " + compound);

            JsonElement element1 = compound == null ? null : JsonParser.parseString(compound.toString());
            //System.out.println("element1 = " + element1);

            response = element1 == null ? null : NightNbtHolder.fromJson(element1);
        }

        Key key = event.getIdentifier();
        //System.out.println("event.getIdentifier() = " + key);

        Player player = gameConnection.getPlayer();
        NamespacedKey identifier = new NamespacedKey(key.namespace(), key.value());

        DialogClickResult result = new DialogClickResult(player, identifier, response);
        this.handler.handleClick(result);
    }
}
