package su.nightexpress.nightcore.core.command;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.impl.ReloadCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.ItemTag;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.text.NightMessage;

import static su.nightexpress.nightcore.util.Placeholders.TAG_LINE_BREAK;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CoreCommands {

    private static final String CMD_CHECKPERM = "checkperm";

    private static final String ARG_PLAYER = "player";

    public static void load(@NotNull NightCore core) {
        ChainedNode root = core.getRootNode();

        if (Plugins.hasPermissionsProvider()) {
            root.addChildren(DirectNode.builder(core, CMD_CHECKPERM)
                .permission(CorePerms.COMMAND_CHECK_PERM)
                .description(CoreLang.COMMAND_CHECKPERM_DESC)
                .withArgument(ArgumentTypes.player(ARG_PLAYER).required())
                .executes(CoreCommands::checkPermissions)
            );
        }

        root.addChildren(DirectNode.builder(core, "dumpitem")
            .playerOnly()
            .permission(CorePerms.COMMAND_DUMP_ITEM)
            .description(CoreLang.COMMAND_DUMPITEM_DESC)
            .executes(CoreCommands::dumpItem)
        );

        root.addChildren(ReloadCommand.builder(core, CorePerms.COMMAND_RELOAD));
    }

    private static boolean checkPermissions(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayerArgument(ARG_PLAYER);
        String builder =
            BOLD.wrap(LIGHT_YELLOW.wrap("Permissions report for ") + LIGHT_ORANGE.wrap(player.getName() + ":")) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.wrap("▪ " + LIGHT_YELLOW.wrap("Primary Group: ") + Players.getPrimaryGroup(player)) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.wrap("▪ " + LIGHT_YELLOW.wrap("All Groups: ") + String.join(", ", Players.getInheritanceGroups(player))) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.wrap("▪ " + LIGHT_YELLOW.wrap("Prefix: ") + Players.getRawPrefix(player)) +
                TAG_LINE_BREAK +
                LIGHT_ORANGE.wrap("▪ " + LIGHT_YELLOW.wrap("Suffix: ") + Players.getRawSuffix(player));
        NightMessage.create(builder).send(context.getSender());
        return true;
    }

    private static boolean dumpItem(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemTag tag = ItemNbt.getTag(itemStack);

        player.sendMessage("=".repeat(10) + " DUMP ITEM " + "=".repeat(10));
        player.sendMessage(tag == null ? "null": tag.getTag());

        return true;
    }
}
