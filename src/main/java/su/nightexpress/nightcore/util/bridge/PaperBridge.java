package su.nightexpress.nightcore.util.bridge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class PaperBridge implements Software {

    @Override
    @NotNull
    public String getName() {
        return "paper-bridge";
    }

    @Override
    public boolean isSpigot() {
        return false;
    }

    @Override
    public boolean isPaper() {
        return true;
    }

    @Override
    public boolean initialize(@NotNull NightCore core) {
        return true;
    }

    @NotNull
    @Override
    public InventoryView createView(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player) {
        Component component = JSONComponentSerializer.json().deserialize(NightMessage.asJson(title)); // TODO Direct NightMessage to component

        return builder.title(component).build(player);
    }

    @Override
    @NotNull
    public SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Bukkit.getCommandMap();
    }

    @Override
    @NotNull
    public Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap) {
        return commandMap.getKnownCommands();
    }

    @SuppressWarnings("deprecation")
    @Override
    public int nextEntityId() {
        return Bukkit.getUnsafe().nextEntityId();
    }
}
