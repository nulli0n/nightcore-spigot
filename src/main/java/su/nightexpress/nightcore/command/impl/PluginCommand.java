package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.api.NightCommand;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Deprecated
public abstract class PluginCommand<P extends NightCorePlugin> extends AbstractCommand<P> implements NightPluginCommand {

    private Command      backend;
    private NightCommand defaultCommand;

    public PluginCommand(@NotNull P plugin, @NotNull String[] aliases) {
        this(plugin, aliases, (String) null);
    }

    public PluginCommand(@NotNull P plugin, @NotNull String[] aliases, @Nullable Permission permission) {
        super(plugin, aliases, permission);
    }

    public PluginCommand(@NotNull P plugin, @NotNull String[] aliases, @Nullable String permission) {
        super(plugin, aliases, permission);
    }

    @Override
    public void addDefaultCommand(@NotNull NightCommand command) {
        this.addChildren(command);
        this.defaultCommand = command;
    }

    @Override
    @Nullable
    public NightCommand getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public Command getBackend() {
        return backend;
    }

    @Override
    public void setBackend(@NotNull Command backend) {
        this.backend = backend;
    }

    //@Override
    @NotNull
    private NightCommand findChildren(@NotNull String[] args) {
        NightCommand command = this;//.defaultCommand;
        int childCount = 0;
        while (args.length > childCount) {
            NightCommand child = command.getChildren(args[childCount++]);
            if (child == null) break;

            command = child;
        }
        return command;
    }

    /*private int countChildren(@NotNull String[] args) {
        AbstractCommand<P> command = this;
        int childCount = 0;
        while (args.length > childCount) {
            AbstractCommand<P> child = command.getChildren(args[childCount]);
            if (child == null) break;

            command = child;
            childCount++;
        }
        return childCount;
    }*/

    /*private String[] insertArg(@NotNull String arg, int index, String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        if (index >= list.size()) {
            list.add(arg);
        }
        else list.add(index, arg);
        return list.toArray(new String[0]);
    }*/

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        NightCommand command = this.findChildren(args);
        if (command instanceof NightPluginCommand pluginCommand) {
            if (pluginCommand.getDefaultCommand() != null) {
                command = pluginCommand.getDefaultCommand();
                //args = insertArg(command.getAliases()[0], 0, args);
            }
        }
        command.execute(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                      @NotNull String label, String[] args) {

        if (!(sender instanceof Player player) || args.length == 0) return Collections.emptyList();

        NightCommand command = this.findChildren(args);
        if (!command.hasPermission(sender)) return Collections.emptyList();

        List<String> list = new ArrayList<>();
        if (!command.getChildrens().isEmpty()) {
            command.getChildrens().stream().filter(child -> child.hasPermission(sender))
                .forEach(child -> list.addAll(Arrays.asList(child.getAliases())));
        }
        else {
            list.addAll(command.getTab(player, command.equals(this) ? (args.length) : (args.length - 1), args));
        }
        return Lists.getSequentialMatches(list, args[args.length - 1]);
    }
}
