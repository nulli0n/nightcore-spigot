package su.nightexpress.nightcore.commands.argument.type;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.List;

public class WorldArgumentType implements ArgumentType<World>, SuggestionsProvider {

    @Override
    @NonNull
    public World parse(@NonNull CommandContextBuilder contextBuilder,
                       @NonNull String string) throws CommandSyntaxException {
        World world = Bukkit.getWorld(string);
        if (world == null) throw CommandSyntaxException.custom(CoreLang.COMMAND_SYNTAX_INVALID_WORLD);

        return world;
    }

    @Override
    @NonNull
    public List<String> suggest(@NonNull ArgumentReader reader, @NonNull CommandContext context) {
        return BukkitThing.worldNames();
    }
}
