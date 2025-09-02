package su.nightexpress.nightcore.core.config;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.BooleanLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.locale.message.MessageData;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public final class CoreLang implements LangContainer {

    public static final TextLocale COMMAND_ARGUMENT_NAME_GENERIC     = LangEntry.builder("Command.ArgumentName.Generic").text("value");
    public static final TextLocale COMMAND_ARGUMENT_NAME_TYPE        = LangEntry.builder("Command.ArgumentName.Type").text("type");
    public static final TextLocale COMMAND_ARGUMENT_NAME_NUMBER      = LangEntry.builder("Command.ArgumentName.Number").text("number");
    public static final TextLocale COMMAND_ARGUMENT_NAME_NAME        = LangEntry.builder("Command.ArgumentName.Name").text("name");
    public static final TextLocale COMMAND_ARGUMENT_NAME_AMOUNT      = LangEntry.builder("Command.ArgumentName.Amount").text("amount");
    public static final TextLocale COMMAND_ARGUMENT_NAME_PLAYER      = LangEntry.builder("Command.ArgumentName.Player").text("player");
    public static final TextLocale COMMAND_ARGUMENT_NAME_WORLD       = LangEntry.builder("Command.ArgumentName.World").text("world");
    public static final TextLocale COMMAND_ARGUMENT_NAME_ITEM_TYPE   = LangEntry.builder("Command.ArgumentName.ItemType").text("itemType");
    public static final TextLocale COMMAND_ARGUMENT_NAME_BLOCK_TYPE  = LangEntry.builder("Command.ArgumentName.BlockType").text("blockType");
    public static final TextLocale COMMAND_ARGUMENT_NAME_ENCHANTMENT = LangEntry.builder("Command.ArgumentName.Enchantment").text("enchantment");

    public static final TextLocale COMMAND_USAGE_REQUIRED_ARGUMENT = LangEntry.builder("Command.Usage.RequiredArgument").text(SOFT_RED.wrap("<" + GENERIC_NAME + ">"));
    public static final TextLocale COMMAND_USAGE_OPTIONAL_ARGUMENT = LangEntry.builder("Command.Usage.OptionalArgument").text(SOFT_YELLOW.wrap("[" + GENERIC_NAME + "]"));
    public static final TextLocale COMMAND_USAGE_OPTIONAL_FLAG     = LangEntry.builder("Command.Usage.OptionalFlag").text(GRAY.wrap("[" + GENERIC_NAME + "]"));

    public static final TextLocale COMMAND_HELP_DESC      = LangEntry.builder("Command.Help.Desc").text("List all sub-commands.");
    public static final TextLocale COMMAND_RELOAD_DESC    = LangEntry.builder("Command.Reload.Desc").text("Reload the plugin.");
    public static final TextLocale COMMAND_CHECKPERM_DESC = LangEntry.builder("Command.CheckPerm.Desc").text("Print player permissions info.");
    public static final TextLocale COMMAND_DUMPITEM_DESC  = LangEntry.builder("Command.DumpItem.Desc").text("Print item NBT info.");

    public static final MessageLocale COMMAND_SYNTAX_GENERIC_ERROR = LangEntry.builder("Command.Syntax.GenericError")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_INPUT) + " is not a valid " + SOFT_RED.wrap(GENERIC_NAME) + " argument!"));

    public static final MessageLocale COMMAND_SYNTAX_NUMBER_NOT_DECIMAL = LangEntry.builder("Command.Syntax.NumberNotDecimal")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_NAME) + " argument value must be decimal, but " + SOFT_RED.wrap(GENERIC_INPUT) + " was found."));

    public static final MessageLocale COMMAND_SYNTAX_NUMBER_NOT_INTEGER = LangEntry.builder("Command.Syntax.NumberNotInteger")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_NAME) + " argument value must be integer, but " + SOFT_RED.wrap(GENERIC_INPUT) + " was found."));

    public static final MessageLocale COMMAND_SYNTAX_NUMBER_ABOVE_MAX = LangEntry.builder("Command.Syntax.NumberAboveMax")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_NAME) + " argument value can not be greater than " + SOFT_RED.wrap(GENERIC_VALUE) + ", but " + SOFT_RED.wrap(GENERIC_INPUT) + " was found."));

    public static final MessageLocale COMMAND_SYNTAX_NUMBER_BELOW_MIN = LangEntry.builder("Command.Syntax.NumberBelowMin")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_NAME) + " argument value can not be smaller than " + SOFT_RED.wrap(GENERIC_VALUE) + ", but " + SOFT_RED.wrap(GENERIC_INPUT) + " was found."));

    public static final MessageLocale COMMAND_SYNTAX_INVALID_WORLD = LangEntry.builder("Command.Syntax.InvalidWorld")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_INPUT) + " is not a valid world!"));

    public static final MessageLocale COMMAND_SYNTAX_INVALID_ITEM = LangEntry.builder("Command.Syntax.InvalidItem")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_INPUT) + " is not a valid item!"));

    public static final MessageLocale COMMAND_SYNTAX_INVALID_BLOCK = LangEntry.builder("Command.Syntax.InvalidBlock")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_INPUT) + " is not a valid block!"));

    public static final MessageLocale COMMAND_SYNTAX_INVALID_ENCHANTMENT = LangEntry.builder("Command.Syntax.InvalidEnchantment")
        .chatMessage(GRAY.wrap(SOFT_RED.wrap(GENERIC_INPUT) + " is not a valid enchantment!"));

    public static final MessageLocale COMMAND_EXECUTION_PLAYER_ONLY = LangEntry.builder("Command.Execution.PlayerOnly")
        .chatMessage(SOFT_RED.wrap("This command is for players only."));

    public static final MessageLocale COMMAND_EXECUTION_NOT_YOURSELF = LangEntry.builder("Command.Execution.NotYourself")
        .chatMessage(SOFT_RED.wrap("You can't use this command on yourself."));

    public static final MessageLocale COMMAND_EXECUTION_MISSING_ARGUMENTS = LangEntry.builder("Command.Execution.MissingArguments")
        .message(MessageData.CHAT_NO_PREFIX,
            " ",
            RED.wrap("Error: ") + GRAY.wrap("Missing required arguments!"),
            RED.wrap("Usage: ") + SOFT_YELLOW.wrap("/" + GENERIC_COMMAND),
            " "
        );



    public static final MessageLocale HELP_PAGE_GENERAL = LangEntry.builder("HelpPage.General")
        .message(MessageData.CHAT_NO_PREFIX,
            " ",
            GRAY.wrap("  " + YELLOW.and(BOLD).wrap(GENERIC_NAME) + " - " + YELLOW.and(BOLD).wrap("Commands:")),
            " ",
            GRAY.wrap("  " + RED.and(BOLD).wrap("<>") + " - Required, " + GREEN.and(BOLD).wrap("[]") + " - Optional."),
            " ",
            GENERIC_ENTRY,
            " "
        );

    public static final TextLocale HELP_PAGE_ENTRY = LangEntry.builder("HelpPage.Entry")
        .text("  " + SOFT_YELLOW.wrap("/" + GENERIC_COMMAND) + GRAY.wrap(" - " + GENERIC_DESCRIPTION));

    public static final MessageLocale PLUGIN_RELOADED = LangEntry.builder("Plugin.Reloaded")
        .chatMessage(GRAY.wrap("Plugin " + GREEN.wrap("reloaded") + "!"));


    public static final MessageLocale ERROR_NO_PERMISSION = LangEntry.builder("Error.NoPermission")
        .chatMessage(SOFT_RED.wrap("You don't have permission to do that!"));

    public static final MessageLocale ERROR_INVALID_PLAYER = LangEntry.builder("Error.InvalidPlayer")
        .chatMessage(SOFT_RED.wrap("Player not found!"));

    public static final TextLocale TIME_LABEL_DAY    = LangEntry.builder("Time.Label.Day").text("%sd.");
    public static final TextLocale TIME_LABEL_HOUR   = LangEntry.builder("Time.Label.Hour").text("%sh.");
    public static final TextLocale TIME_LABEL_MINUTE = LangEntry.builder("Time.Label.Minute").text("%smin.");
    public static final TextLocale TIME_LABEL_SECOND = LangEntry.builder("Time.Label.Second").text("%ssec.");
    public static final TextLocale TIME_DELIMITER    = LangEntry.builder("Time.Delimiter").text(" ");


    public static final BooleanLocale STATE_ENABLED_DISALBED = LangEntry.builder("States.EnabledDisabled").bool(GREEN.wrap("Enabled"), RED.wrap("Disabled"));
    public static final BooleanLocale STATE_YES_NO           = LangEntry.builder("States.YesNo").bool(GREEN.wrap("Yes"), RED.wrap("No"));
    public static final BooleanLocale STATE_ON_OFF           = LangEntry.builder("States.OnOff").bool(GREEN.wrap("ON"), RED.wrap("OFF"));

    public static final TextLocale ENTRY_GOOD = LangEntry.builder("Entry.Good").text(GREEN.wrap("✔") + " " + GRAY.wrap("%s"));
    public static final TextLocale ENTRY_BAD  = LangEntry.builder("Entry.Bad").text(RED.wrap("✘") + " " + GRAY.wrap("%s"));

    public static final TextLocale OTHER_ANY       = LangEntry.builder("Other.Any").text("Any");
    public static final TextLocale OTHER_NONE      = LangEntry.builder("Other.None").text("None");
    public static final TextLocale OTHER_NEVER     = LangEntry.builder("Other.Never").text("Never");
    public static final TextLocale OTHER_ONE_TIMED = LangEntry.builder("Other.OneTimed").text("One-timed");
    public static final TextLocale OTHER_UNLIMITED = LangEntry.builder("Other.Unlimited").text("Unlimited");
    public static final TextLocale OTHER_INFINITY  = LangEntry.builder("Other.Infinity").text("∞");

    public static final IconLocale MENU_ICON_EXIT          = LangEntry.iconBuilder("MenuItem.Exit").name("Close", RED).build();
    public static final IconLocale MENU_ICON_BACK          = LangEntry.iconBuilder("MenuItem.Back").name("Back", SOFT_YELLOW).build();
    public static final IconLocale MENU_ICON_NEXT_PAGE     = LangEntry.iconBuilder("MenuItem.NextPage").name(UNDERLINED.wrap("Next Page") + " →", WHITE).build();
    public static final IconLocale MENU_ICON_PREVIOUS_PAGE = LangEntry.iconBuilder("MenuItem.PreviousPage").name("← " + UNDERLINED.wrap("Previous Page"), WHITE).build();

    @NotNull
    public static String formatEntry(@NotNull String entry, boolean flag) {
        return (flag ? ENTRY_GOOD : ENTRY_BAD).text().formatted(entry);
    }

    @NotNull
    public static String goodEntry(@NotNull String entry) {
        return formatEntry(entry, true);
    }

    @NotNull
    public static String badEntry(@NotNull String entry) {
        return formatEntry(entry, false);
    }
}
