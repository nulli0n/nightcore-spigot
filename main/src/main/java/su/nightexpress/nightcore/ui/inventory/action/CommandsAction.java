package su.nightexpress.nightcore.ui.inventory.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.Players;

import java.util.List;
import java.util.Map;

public record CommandsAction(@NonNull Map<ClickType, List<String>> commandMap) implements MenuItemAction {

    @Override
    public void execute(@NotNull ActionContext context) {
        InventoryClickEvent event = context.getEvent();
        ClickType type = event.getClick();
        List<String> commands = this.commandMap.get(type);
        if (commands == null) return;

        Player player = context.getPlayer();
        Players.dispatchCommands(player, commands);
    }
}
