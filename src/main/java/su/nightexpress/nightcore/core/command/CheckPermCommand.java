package su.nightexpress.nightcore.core.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.text.NightMessage;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;
import static su.nightexpress.nightcore.util.Placeholders.*;

public class CheckPermCommand {

    private static final String ARG_PLAYER = "player";

    public static void inject(@NotNull NightCore plugin, @NotNull ChainedNode node) {
        node.addChildren(DirectNode.builder(plugin, "checkperm")
            .permission(CorePerms.COMMAND_CHECK_PERM)
            .description(CoreLang.COMMAND_CHECKPERM_DESC)
            .withArgument(ArgumentTypes.player(ARG_PLAYER).required())
            .executes(CheckPermCommand::execute)
        );
    }

    public static boolean execute(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayerArgument(ARG_PLAYER);
        String builder =
            BOLD.enclose(LIGHT_YELLOW.enclose("Permissions report for ") + LIGHT_ORANGE.enclose(player.getName() + ":")) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Primary Group: ") + Colorizer.plain(VaultHook.getPermissionGroup(player))) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("All Groups: ") + Colorizer.plain(String.join(", ", VaultHook.getPermissionGroups(player)))) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Prefix: ") + VaultHook.getPrefix(player)) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Suffix: ") + VaultHook.getSuffix(player));
        NightMessage.create(builder).send(context.getSender());
        return true;
    }
}
