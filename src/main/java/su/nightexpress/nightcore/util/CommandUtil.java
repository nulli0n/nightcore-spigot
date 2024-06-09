package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.impl.WrappedCommand;

import java.util.*;

public class CommandUtil {

    private static final String FIELD_COMMAND_MAP = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    private static final SimpleCommandMap COMMAND_MAP = getCommandMap();

    private static SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
    }

    public static void register(@NotNull Plugin plugin, @NotNull NightPluginCommand command) {
        WrappedCommand wrappedCommand = new WrappedCommand(plugin, command);
        if (COMMAND_MAP.register(plugin.getName(), wrappedCommand)) {
            command.setBackend(wrappedCommand);
        }
    }

    public static boolean register(@NotNull Plugin plugin, @NotNull WrappedCommand wrappedCommand) {
        return COMMAND_MAP.register(plugin.getName(), wrappedCommand);
    }

    /*public static void syncCommands() {
        // Fix tab completer when registerd on runtime
        Server server = Bukkit.getServer();
        Method method = Reflex.getMethod(server.getClass(), "syncCommands");
        if (method == null) return;

        Reflex.invokeMethod(method, server);
    }*/

    @SuppressWarnings("unchecked")
    public static boolean unregister(@NotNull String name) {
        Command command = getCommand(name).orElse(null);
        if (command == null) return false;

        Map<String, Command> knownCommands = (HashMap<String, Command>) Reflex.getFieldValue(COMMAND_MAP, FIELD_KNOWN_COMMANDS);
        if (knownCommands == null) return false;
        if (!command.unregister(COMMAND_MAP)) return false;

        return knownCommands.keySet().removeIf(key -> key.equalsIgnoreCase(command.getName()) || command.getAliases().contains(key));
    }

    @NotNull
    public static Set<String> getAliases(@NotNull String name) {
        return getAliases(name, false);
    }

    @NotNull
    public static Set<String> getAliases(@NotNull String name, boolean inclusive) {
        Command command = getCommand(name).orElse(null);
        if (command == null) return Collections.emptySet();

        Set<String> aliases = new HashSet<>(command.getAliases());
        if (inclusive) aliases.add(command.getName());
        return aliases;
    }

    @NotNull
    public static Optional<Command> getCommand(@NotNull String name) {
        return COMMAND_MAP.getCommands().stream()
            .filter(command -> command.getName().equalsIgnoreCase(name) || command.getLabel().equalsIgnoreCase(name) || command.getAliases().contains(name))
            .findFirst();
    }

    @NotNull
    public static String getCommandName(@NotNull String str) {
        String name = Colorizer.strip(str).split(" ")[0].substring(1);

        String[] pluginPrefix = name.split(":");
        if (pluginPrefix.length == 2) {
            name = pluginPrefix[1];
        }

        return name;
    }

    @Nullable
    public static Player getPlayerOrSender(@NotNull CommandContext context, @NotNull ParsedArguments arguments, @NotNull String name) {
        Player player;
        if (arguments.hasArgument(name)) {
            player = arguments.getPlayerArgument(name);
        }
        else {
            if (context.getExecutor() == null) {
                context.errorPlayerOnly();
                return null;
            }
            player = context.getExecutor();
        }
        return player;
    }
}
