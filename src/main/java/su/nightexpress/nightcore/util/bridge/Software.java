package su.nightexpress.nightcore.util.bridge;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public interface Software {

    boolean initialize(@NotNull NightCore core);

    @NotNull String getName();

    boolean isSpigot();

    boolean isPaper();

    @NotNull InventoryView createView(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player);

    @NotNull SimpleCommandMap getCommandMap();

    @NotNull Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap);

    int nextEntityId();
}
