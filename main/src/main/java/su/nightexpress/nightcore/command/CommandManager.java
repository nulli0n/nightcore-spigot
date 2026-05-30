package su.nightexpress.nightcore.command;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.command.api.NightPluginCommand;
import su.nightexpress.nightcore.command.base.HelpSubCommand;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.ServerCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.impl.BaseCommand;
import su.nightexpress.nightcore.manager.SimpleManager;
import su.nightexpress.nightcore.util.CommandUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class CommandManager extends SimpleManager<NightPlugin> {

    private final Set<NightPluginCommand> commands;
    private final Set<ServerCommand>      serverCommands;

    private BaseCommand                           mainCommand;
    private RootCommand<NightPlugin, ChainedNode> rootCommand;

    public CommandManager(@NonNull NightPlugin plugin) {
        super(plugin);
        this.commands = new HashSet<>();
        this.serverCommands = new HashSet<>();
    }

    @Override
    public void onLoad() {
        String[] aliases = this.plugin.getCommandAliases();
        if (aliases == null || aliases.length == 0) {
            this.plugin.error("Could not register plugin commands!");
            return;
        }

        if (this.plugin instanceof ImprovedCommands) {
            this.rootCommand = RootCommand.chained(this.plugin, aliases, builder -> builder
                .localized(this.plugin.getNameLocalized())
            );
            this.registerCommand(this.rootCommand);
        }
        else {
            // Create main plugin command and attach help sub-command as a default executor.
            this.mainCommand = new BaseCommand(this.plugin);
            this.mainCommand.addDefaultCommand(new HelpSubCommand(this.plugin));

            // Register main command as a bukkit command.
            this.registerCommand(this.mainCommand);
        }
    }

    @Override
    public void onShutdown() {
        this.serverCommands.forEach(ServerCommand::unregister);
        this.serverCommands.clear();

        for (NightPluginCommand command : new HashSet<>(this.getCommands())) {
            this.unregisterCommand(command);
            command.getChildrens().clear();
        }
        this.getCommands().clear();
    }

    @NonNull
    @Deprecated
    public Set<NightPluginCommand> getCommands() {
        return this.commands;
    }

    @NonNull
    public Set<ServerCommand> getServerCommands() {
        return serverCommands;
    }

    @NonNull
    @Deprecated
    public BaseCommand getMainCommand() {
        return this.mainCommand;
    }

    @NonNull
    public RootCommand<NightPlugin, ChainedNode> getRootCommand() {
        return rootCommand;
    }

    @Nullable
    public NightPluginCommand getCommand(@NonNull String alias) {
        return this.getCommands().stream()
            .filter(command -> Lists.contains(command.getAliases(), alias))
            .findFirst().orElse(null);
    }

    @Nullable
    public ServerCommand getServerCommand(@NonNull String alias) {
        return this.serverCommands.stream()
            .filter(command -> command.getNode().getName().equalsIgnoreCase(alias) || Lists.contains(command.getNode()
                .getAliases(), alias))
            .findFirst().orElse(null);
    }

    @Deprecated
    public void registerCommand(@NonNull NightPluginCommand command) {
        if (this.commands.add(command)) {
            CommandUtil.register(this.plugin, command);
        }
    }

    public boolean unregisterCommand(@NonNull String alias) {
        NightPluginCommand command = this.getCommand(alias);
        if (command != null) {
            return this.unregisterCommand(command);
        }
        return false;
    }

    @Deprecated
    public boolean unregisterCommand(@NonNull NightPluginCommand command) {
        if (this.commands.remove(command)) {
            return CommandUtil.unregister(command.getAliases()[0]);
        }
        return false;
    }

    public void registerCommand(@NonNull ServerCommand command) {
        if (!this.serverCommands.contains(command)) {
            if (command.register()) {
                this.serverCommands.add(command);
            }
        }
    }

    public boolean unregisterServerCommand(@NonNull String alias) {
        ServerCommand command = this.getServerCommand(alias);
        if (command != null) {
            return this.unregisterCommand(command);
        }
        return false;
    }

    public boolean unregisterCommand(@NonNull ServerCommand command) {
        return this.serverCommands.remove(command) && command.unregister();
    }
}
