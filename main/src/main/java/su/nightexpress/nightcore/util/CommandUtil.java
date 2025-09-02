package su.nightexpress.nightcore.util;

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
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.*;

public class CommandUtil {

    @NotNull
    private static SimpleCommandMap getCommandMap() {
        return Software.instance().getCommandMap();
    }

    @Deprecated
    public static void register(@NotNull Plugin plugin, @NotNull NightPluginCommand command) {
        WrappedCommand wrappedCommand = new WrappedCommand(plugin, command);
        if (getCommandMap().register(plugin.getName(), wrappedCommand)) {
            command.setBackend(wrappedCommand);
        }
    }

    public static boolean register(@NotNull Plugin plugin, @NotNull WrappedCommand wrappedCommand) {
        return getCommandMap().register(plugin.getName(), wrappedCommand);
    }

    public static boolean register(@NotNull Command command, @NotNull String fallbackPrefix) {
        return getCommandMap().register(fallbackPrefix, command);
    }

    /*public static void syncCommands() {
        // Fix tab completer when registerd on runtime
        Server server = Bukkit.getServer();
        Method method = Reflex.getMethod(server.getClass(), "syncCommands");
        if (method == null) return;

        Reflex.invokeMethod(method, server);
    }*/

    public static boolean unregister(@NotNull String name) {
        Command command = getCommand(name).orElse(null);
        if (command == null) return false;

        return unregister(command);
    }

    public static boolean unregister(@NotNull Command command) {
        SimpleCommandMap commandMap = getCommandMap();

        Map<String, Command> knownCommands = Software.instance().getKnownCommands(commandMap);
        if (!command.unregister(commandMap)) return false;

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
        return getCommandMap().getCommands().stream()
            .filter(command -> command.getName().equalsIgnoreCase(name) || command.getLabel().equalsIgnoreCase(name) || command.getAliases().contains(name))
            .findFirst();
    }

    @NotNull
    public static String getCommandName(@NotNull String string) {
        String name = string.split(" ")[0].substring(1);

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
