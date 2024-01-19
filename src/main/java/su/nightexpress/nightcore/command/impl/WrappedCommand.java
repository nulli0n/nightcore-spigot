package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.api.NightPluginCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WrappedCommand extends Command implements PluginIdentifiableCommand {

    protected final Plugin          plugin;
    protected final CommandExecutor executor;
    protected final TabCompleter    tabCompleter;

    public WrappedCommand(@NotNull Plugin plugin, @NotNull NightPluginCommand command) {
        this(plugin, command, command, command.getAliases(), command.getDescription(), command.getUsage());
        this.setPermission(command.getPermission());
    }

    public WrappedCommand(@NotNull Plugin plugin,
                          @NotNull CommandExecutor executor,
                          @NotNull TabCompleter tabCompleter,
                          @NotNull String[] aliases,
                          @NotNull String description,
                          @NotNull String usage) {
        super(aliases[0], description, usage, Arrays.asList(aliases));
        this.plugin = plugin;
        this.executor = executor;
        this.tabCompleter = tabCompleter;
    }

    @Override
    @NotNull
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return this.executor.onCommand(sender, this, label, args);
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        List<String> list = this.tabCompleter.onTabComplete(sender, this, alias, args);
        return list == null ? Collections.emptyList() : list;
    }
}
