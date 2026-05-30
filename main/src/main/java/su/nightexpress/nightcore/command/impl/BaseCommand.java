package su.nightexpress.nightcore.command.impl;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.CommandResult;

@Deprecated
public class BaseCommand extends PluginCommand<NightCorePlugin> {

    public BaseCommand(@NonNull NightCorePlugin plugin) {
        super(plugin, plugin.getCommandAliases());
    }

    @Override
    protected void onExecute(@NonNull CommandSender sender, @NonNull CommandResult result) {

    }
}
