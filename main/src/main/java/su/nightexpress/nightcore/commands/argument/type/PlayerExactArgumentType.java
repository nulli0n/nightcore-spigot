package su.nightexpress.nightcore.commands.argument.type;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Players;

public class PlayerExactArgumentType implements ArgumentType<Player>, SuggestionsProvider {

    @Override
    public @NonNull Player parse(@NonNull CommandContextBuilder contextBuilder, @NonNull String string)
            throws CommandSyntaxException {
        Player player = Players.getExactPlayer(string);
        if (player == null) throw CommandSyntaxException.custom(CoreLang.ERROR_INVALID_PLAYER);

        return player;
    }

    @Override
    public @NonNull List<String> suggest(@NonNull ArgumentReader reader, @NonNull CommandContext context) {
        return context.getSender() instanceof Player player ? Players.playerNames(player) : Players.playerNames();
    }
}
