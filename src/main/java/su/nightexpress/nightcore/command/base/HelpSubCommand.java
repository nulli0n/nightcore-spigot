package su.nightexpress.nightcore.command.base;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.api.NightCommand;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;

public class HelpSubCommand extends AbstractCommand<NightCorePlugin> {

    public HelpSubCommand(@NotNull NightCorePlugin plugin) {
        super(plugin, new String[]{"help"});
        this.setDescription(CoreLang.COMMAND_HELP_DESC);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        NightCommand parent = this.getParent();
        if (parent == null) return;

        if (!parent.hasPermission(sender)) {
            this.errorPermission(sender);
            return;
        }

        CoreLang.COMMAND_HELP_LIST.getMessage()
            .replace(Placeholders.GENERIC_NAME, this.plugin.getNameLocalized())
            .replace(Placeholders.GENERIC_ENTRY, list -> {
                parent.getChildrens().forEach(children -> {
                    if (!children.hasPermission(sender)) return;

                    list.add(children.replacePlaceholders().apply(CoreLang.COMMAND_HELP_ENTRY.getString()));
                });
            }).send(sender);
    }
}
