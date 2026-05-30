package su.nightexpress.nightcore.integration.currency.command;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.integration.currency.impl.ItemStackCurrency;

public class CurrencyCommands {

    private static final String ARG_NAME = "name";

    private static NightCommand command;

    public static void load(@NonNull NightCore core) {
        command = NightCommand.hub(core, "ecobridge", hub -> hub
            .permission(CorePerms.COMMAND_ECONOMY_BRIDGE)
            .branch(Commands.literal("fromitem")
                .description(CoreLang.COMMAND_ECONOMY_BRIDGE_FROM_ITEM_DESC)
                .playerOnly()
                .withArguments(Arguments.string(ARG_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME))
                .executes((context, arguments) -> createFromItem(core, context, arguments))
            )
        );
        command.register();
    }

    public static void shutdown() {
        if (command != null) {
            command.unregister();
            command = null;
        }
    }

    private static boolean createFromItem(@NonNull NightCore plugin, @NonNull CommandContext context,
                                          @NonNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            CoreLang.ECONOMY_BRIDGE_FROM_ITEM_NOTHING.message().send(player);
            return false;
        }

        String name = arguments.getString(ARG_NAME);
        ItemStackCurrency currency = plugin.getCurrencyManager().createItemCurrency(name, itemStack);
        if (currency == null) {
            CoreLang.ECONOMY_BRIDGE_FROM_ITEM_EXISTS.message().send(player);
            return false;
        }

        CoreLang.ECONOMY_BRIDGE_FROM_ITEM_CREATED.message().send(player, replacer -> replacer.replace(currency
            .replacePlaceholders()));
        return true;
    }
}
