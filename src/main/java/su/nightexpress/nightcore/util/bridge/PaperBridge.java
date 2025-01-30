package su.nightexpress.nightcore.util.bridge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.NightMessage;

@SuppressWarnings("UnstableApiUsage")
public class PaperBridge {

    public static InventoryView create(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player) {
        Component component = JSONComponentSerializer.json().deserialize(NightMessage.asJson(title)); // TODO Direct NightMessage to component

        return builder.title(component).build(player);
    }
}
