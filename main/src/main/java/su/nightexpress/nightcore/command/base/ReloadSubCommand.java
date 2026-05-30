package su.nightexpress.nightcore.command.base;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.core.CoreLang;

@Deprecated
public class ReloadSubCommand extends AbstractCommand<NightCorePlugin> {

    public ReloadSubCommand(@NonNull NightCorePlugin plugin, @NonNull Permission permission) {
        this(plugin, permission.getName());
    }

    public ReloadSubCommand(@NonNull NightCorePlugin plugin, @NonNull String permission) {
        super(plugin, new String[]{"reload"}, permission);
        this.setDescription(CoreLang.COMMAND_RELOAD_DESC);
    }

    @Override
    protected void onExecute(@NonNull CommandSender sender, @NonNull CommandResult result) {
        this.plugin.reload();
        CoreLang.COMMAND_RELOAD_DONE.getMessage(plugin).send(sender);
    }
}
