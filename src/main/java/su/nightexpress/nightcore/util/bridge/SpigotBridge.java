package su.nightexpress.nightcore.util.bridge;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.text.NightMessage;

@SuppressWarnings("UnstableApiUsage")
public class SpigotBridge {

    public static InventoryView create(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player) {
        Reflex.setFieldValue(builder, "title", NightMessage.asLegacy(title));
        return builder.build(player);//menuType.typed().builder().title(title).build(player);
    }
}
