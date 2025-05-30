package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.CommandResult;

@Deprecated
public class BaseCommand extends PluginCommand<NightCorePlugin> {

    public BaseCommand(@NotNull NightCorePlugin plugin) {
        super(plugin, plugin.getCommandAliases());
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
