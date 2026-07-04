package su.nightexpress.nightcore.commands;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public interface NodeExecutor {

    boolean run(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException;
}
