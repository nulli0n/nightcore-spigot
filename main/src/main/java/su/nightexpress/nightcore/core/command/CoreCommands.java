package su.nightexpress.nightcore.core.command;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.integration.permission.PermissionBridge;
import su.nightexpress.nightcore.util.ItemTag;
import su.nightexpress.nightcore.util.Players;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CoreCommands {

    private static final String CMD_CHECKPERM = "checkperm";

    private static final String ARG_PLAYER = "player";

    public static void load(@NotNull NightCore core, @NotNull HubNodeBuilder builder) {
        if (PermissionBridge.hasProvider()) {
            builder.branch(Commands.literal(CMD_CHECKPERM)
                .permission(CorePerms.COMMAND_CHECK_PERM)
                .description(CoreLang.COMMAND_CHECKPERM_DESC)
                .withArguments(Arguments.player(ARG_PLAYER))
                .executes(CoreCommands::checkPermissions)
            );
        }

        builder.branch(Commands.literal("dumpitem")
            .playerOnly()
            .permission(CorePerms.COMMAND_DUMP_ITEM)
            .description(CoreLang.COMMAND_DUMPITEM_DESC)
            .executes(CoreCommands::dumpItem)
        );

        builder.branch(Commands.literal("reload")
            .description(CoreLang.COMMAND_RELOAD_DESC)
            .permission(CorePerms.COMMAND_RELOAD)
            .executes((context, arguments) -> {
                core.doReload(context.getSender());
                return true;
            })
        );
    }

    private static boolean checkPermissions(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = arguments.getPlayer(ARG_PLAYER);
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
        ItemTag tag = ItemTag.of(itemStack); // TODO try catch

        player.sendMessage("=".repeat(10) + " DUMP ITEM " + "=".repeat(10));
        player.sendMessage(tag.getTag());

        return true;
    }


}
