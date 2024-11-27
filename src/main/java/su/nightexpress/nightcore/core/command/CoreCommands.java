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
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.text.NightMessage;

import static su.nightexpress.nightcore.util.Placeholders.TAG_LINE_BREAK;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CoreCommands {

    private static final String CMD_CHECKPERM = "checkperm";

    private static final String ARG_PLAYER = "player";

    public static void load(@NotNull NightCore core) {
        ChainedNode root = core.getRootNode();

        if (Plugins.hasVault()) {
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

    private static boolean dumpItem(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        player.sendMessage("=".repeat(10) + " DUMP ITEM " + "=".repeat(10));
        player.sendMessage(String.valueOf(ItemNbt.getTagString(itemStack)));

        return true;
    }
}
