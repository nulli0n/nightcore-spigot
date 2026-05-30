package su.nightexpress.nightcore.command.experimental.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.builder.DirectNodeBuilder;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

@Deprecated
public class ReloadCommand {

    @Deprecated
    public static void inject(@NonNull NightCorePlugin plugin, @NonNull ChainedNode node,
                              @NonNull UniPermission permission) {
        node.addChildren(builder(plugin, permission));
    }

    @NonNull
    public static DirectNodeBuilder builder(@NonNull NightCorePlugin plugin, @NonNull UniPermission permission) {
        return DirectNode.builder(plugin, "reload")
            .permission(permission)
            .description(CoreLang.COMMAND_RELOAD_DESC)
            .executes((context, arguments) -> execute(plugin, context, arguments));
    }

    public static boolean execute(@NonNull NightCorePlugin plugin, @NonNull CommandContext context,
                                  @NonNull ParsedArguments arguments) {
        plugin.reload();
        context.send(CoreLang.COMMAND_RELOAD_DONE.getMessage(plugin));
        return true;
    }
}
