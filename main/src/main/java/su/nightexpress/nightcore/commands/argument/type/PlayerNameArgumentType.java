package su.nightexpress.nightcore.commands.argument.type;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class PlayerNameArgumentType extends StringArgumentType {

    public PlayerNameArgumentType() {
        super(false);
    }

    @Override
    @NotNull
    public List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context) {
        return context.getSender() instanceof Player player ? Players.playerNames(player) : Players.playerNames();
    }
}
