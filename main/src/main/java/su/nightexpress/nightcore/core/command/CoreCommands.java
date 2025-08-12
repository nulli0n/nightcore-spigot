package su.nightexpress.nightcore.core.command;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;
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

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CoreCommands {

    private static final String CMD_CHECKPERM = "checkperm";

    private static final String ARG_PLAYER = "player";

    public static void load(@NotNull NightCore core) {
        ChainedNode root = core.getRootNode();

        if (Engine.hasPermissions()) {
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

        /*root.addChildren(DirectNode.builder(core, "dialog")
            .playerOnly()
            .executes((context, arguments) -> {
                Dialogs.testDialog(context.getPlayerOrThrow());
                return true;
            })
        );

        root.addChildren(DirectNode.builder(core, "text")
            .playerOnly()
            .withArgument(ArgumentTypes.string("text").required().complex())
            .executes((context, arguments) -> {
                TextParser.parse(arguments.getStringArgument("text")).send(context.getSender());
                return true;
            })
        );*/

        root.addChildren(ReloadCommand.builder(core, CorePerms.COMMAND_RELOAD));
    }

    private static boolean checkPermissions(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayerArgument(ARG_PLAYER);
        String builder =
            BOLD.wrap(SOFT_YELLOW.wrap("Permissions report for ") + SOFT_ORANGE.wrap(player.getName() + ":")) +
                BR +
                SOFT_ORANGE.wrap("▪ " + SOFT_YELLOW.wrap("Primary Group: ") + Players.getPrimaryGroup(player)) +
                BR +
                SOFT_ORANGE.wrap("▪ " + SOFT_YELLOW.wrap("All Groups: ") + String.join(", ", Players.getInheritanceGroups(player))) +
                BR +
                SOFT_ORANGE.wrap("▪ " + SOFT_YELLOW.wrap("Prefix: ") + Players.getRawPrefix(player)) +
                BR +
                SOFT_ORANGE.wrap("▪ " + SOFT_YELLOW.wrap("Suffix: ") + Players.getRawSuffix(player));
        Players.sendMessage(context.getSender(), builder);
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
