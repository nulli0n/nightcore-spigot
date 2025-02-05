package su.nightexpress.nightcore.util.bridge;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Version;

@SuppressWarnings("UnstableApiUsage")
public class ServerBridge {

    @NotNull
    public static InventoryView createView(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player) {
        if (Version.isSpigot()) {
            return SpigotBridge.create(builder, title, player);
        }
        else {
            return PaperBridge.create(builder, title, player);
        }
    }
}
