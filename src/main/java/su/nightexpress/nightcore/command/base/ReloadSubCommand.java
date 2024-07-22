package su.nightexpress.nightcore.command.base;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.core.CoreLang;

@Deprecated
public class ReloadSubCommand extends AbstractCommand<NightCorePlugin> {

    public ReloadSubCommand(@NotNull NightCorePlugin plugin, @NotNull Permission permission) {
        this(plugin, permission.getName());
    }

    public ReloadSubCommand(@NotNull NightCorePlugin plugin, @NotNull String permission) {
        super(plugin, new String[]{"reload"}, permission);
        this.setDescription(CoreLang.COMMAND_RELOAD_DESC);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        this.plugin.reload();
        CoreLang.COMMAND_RELOAD_DONE.getMessage(plugin).send(sender);
    }
}
