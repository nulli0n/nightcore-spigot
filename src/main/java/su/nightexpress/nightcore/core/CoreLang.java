package su.nightexpress.nightcore.core;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CoreLang {

    public static final LangString COMMAND_ARGUMENT_FORMAT_REQUIRED = LangString.of("Command.Argument.Type.Required",
        LIGHT_RED.enclose("<" + GENERIC_NAME + ">"));

    public static final LangString COMMAND_ARGUMENT_FORMAT_OPTIONAL = LangString.of("Command.Argument.Type.Optional",
        LIGHT_YELLOW.enclose("[" + GENERIC_NAME + "]"));

    public static final LangString COMMAND_FLAG_FORMAT = LangString.of("Command.Flag.Format",
        LIGHT_GRAY.enclose("[" + GENERIC_NAME + "]"));

    public static final LangString COMMAND_ARGUMENT_NAME_GENERIC        = LangString.of("Command.Argument.Name.Generic", "value");
    public static final LangString COMMAND_ARGUMENT_NAME_TYPE           = LangString.of("Command.Argument.Name.Type", "type");
    public static final LangString COMMAND_ARGUMENT_NAME_NAME           = LangString.of("Command.Argument.Name.Name", "name");
    public static final LangString COMMAND_ARGUMENT_NAME_PLAYER         = LangString.of("Command.Argument.Name.Player", "player");
    public static final LangString COMMAND_ARGUMENT_NAME_WORLD          = LangString.of("Command.Argument.Name.World", "world");
    public static final LangString COMMAND_ARGUMENT_NAME_AMOUNT         = LangString.of("Command.Argument.Name.Amount", "amount");
    public static final LangString COMMAND_ARGUMENT_NAME_MATERIAL       = LangString.of("Command.Argument.Name.Material", "material");
    public static final LangString COMMAND_ARGUMENT_NAME_ITEM_MATERIAL  = LangString.of("Command.Argument.Name.ItemMaterial", "item type");
    public static final LangString COMMAND_ARGUMENT_NAME_BLOCK_MATERIAL = LangString.of("Command.Argument.Name.BlockMaterial", "block type");
    public static final LangString COMMAND_ARGUMENT_NAME_ENCHANTMENT    = LangString.of("Command.Argument.Name.Enchantment", "enchantment");
    //public static final LangString COMMAND_ARGUMENT_NAME_POTION_EFFECT  = LangString.of("Command.Argument.Name.Effect", "effect");
    //public static final LangString COMMAND_ARGUMENT_NAME_ATTRIBUTE      = LangString.of("Command.Argument.Name.Attribute", "attribute");

    public static final LangText COMMAND_HELP_LIST = LangText.of("Command.Help.List",
        TAG_NO_PREFIX,
        " ",
        "  " + YELLOW.enclose(BOLD.enclose(GENERIC_NAME)) + GRAY.enclose(" - ") + YELLOW.enclose(BOLD.enclose("Commands:")),
        " ",
        GRAY.enclose("  " + RED.enclose(BOLD.enclose("<>")) + " - Required, " + GREEN.enclose(BOLD.enclose("[]")) + " - Optional."),
        " ",
        GENERIC_ENTRY,
        " "
    );

    public static final LangString COMMAND_HELP_ENTRY = LangString.of("Command.Help.Entry",
        "  " + YELLOW.enclose("/" + COMMAND_LABEL) + " " + ORANGE.enclose(COMMAND_USAGE) + GRAY.enclose(" - " + COMMAND_DESCRIPTION)
    );

    public static final LangString COMMAND_HELP_DESC = LangString.of("Command.Help.Desc", "Show help page.");

    public static final LangString COMMAND_CHECKPERM_DESC = LangString.of("Command.CheckPerm.Desc", "Print player permission info.");

    public static final LangString COMMAND_RELOAD_DESC = LangString.of("Command.Reload.Desc", "Reload the plugin.");

    public static final LangText COMMAND_RELOAD_DONE = LangText.of("Command.Reload.Done",
        LIGHT_GRAY.enclose("Plugin " + LIGHT_GREEN.enclose("reloaded") + "!"));

    public static final LangString TIME_DAY       = LangString.of("Time.Day", GENERIC_AMOUNT + "d.");
    public static final LangString TIME_HOUR      = LangString.of("Time.Hour", GENERIC_AMOUNT + "h.");
    public static final LangString TIME_MINUTE    = LangString.of("Time.Min", GENERIC_AMOUNT + "min.");
    public static final LangString TIME_SECOND    = LangString.of("Time.Sec", GENERIC_AMOUNT + "sec.");
    public static final LangString TIME_DELIMITER = LangString.of("Time.Delimiter", " ");

    public static final LangString OTHER_YES       = LangString.of("Other.Yes", GREEN.enclose("Yes"));
    public static final LangString OTHER_NO        = LangString.of("Other.No", RED.enclose("No"));
    public static final LangString OTHER_ENABLED   = LangString.of("Other.Enabled", GREEN.enclose("Enabled"));
    public static final LangString OTHER_DISABLED  = LangString.of("Other.Disabled", RED.enclose("Disabled"));
    public static final LangString OTHER_ANY       = LangString.of("Other.Any", "Any");
    public static final LangString OTHER_NONE      = LangString.of("Other.None", "None");
    public static final LangString OTHER_NEVER     = LangString.of("Other.Never", "Never");
    public static final LangString OTHER_ONE_TIMED = LangString.of("Other.OneTimed", "One-Timed");
    public static final LangString OTHER_UNLIMITED = LangString.of("Other.Unlimited", "Unlimited");
    public static final LangString OTHER_INFINITY  = LangString.of("Other.Infinity", "∞");

    public static final LangString ENTRY_GOOD = LangString.of("Entry.Good", GREEN.enclose("✔") + " " + GRAY.enclose(GENERIC_ENTRY));
    public static final LangString ENTRY_BAD  = LangString.of("Entry.Bad", RED.enclose("✘") + " " + GRAY.enclose(GENERIC_ENTRY));
    public static final LangString ENTRY_WARN = LangString.of("Entry.Warn", ORANGE.enclose("[❗]") + " " + GRAY.enclose(GENERIC_ENTRY));

    public static final LangText ERROR_INVALID_PLAYER = LangText.of("Error.Invalid_Player",
        RED.enclose("Invalid player!"));

    public static final LangText ERROR_INVALID_WORLD = LangText.of("Error.Invalid_World",
        RED.enclose("Invalid world!"));

    public static final LangText ERROR_INVALID_NUMBER = LangText.of("Error.Invalid_Number",
        RED.enclose("Invalid number!"));

    public static final LangText ERROR_INVALID_MATERIAL = LangText.of("Error.InvalidMaterial",
        RED.enclose("Invalid material!"));

    public static final LangText ERROR_INVALID_ENCHANTMENT = LangText.of("Error.InvalidEnchantment",
        RED.enclose("Invalid enchantment!"));

    /*public static final LangText ERROR_INVALID_POTION_EFFECT = LangText.of("Error.InvalidPotionEffectType",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid potion effect type!"));

    public static final LangText ERROR_INVALID_ATTRIBUTE = LangText.of("Error.InvalidAttribute",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid attribute!"));*/

    public static final LangText ERROR_NO_PERMISSION = LangText.of("Error.NoPermission",
        RED.enclose("You don't have permissions to do that!"));

    public static final LangText ERROR_COMMAND_PARSE_FLAG = LangText.of("Error.Command.ParseFlag",
        LIGHT_GRAY.enclose("Invalid value " + LIGHT_RED.enclose(GENERIC_VALUE) + " for " + LIGHT_RED.enclose(GENERIC_NAME) + " flag."));

    public static final LangText ERROR_COMMAND_PARSE_ARGUMENT = LangText.of("Error.Command.ParseArgument",
        LIGHT_GRAY.enclose("Invalid value " + LIGHT_RED.enclose(GENERIC_VALUE) + " for " + LIGHT_RED.enclose(GENERIC_NAME) + " argument."));

    public static final LangText ERROR_COMMAND_INVALID_PLAYER_ARGUMENT = LangText.of("Error.Command.Argument.InvalidPlayer",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose("Can not find player " + GENERIC_VALUE) + "!"));

    public static final LangText ERROR_COMMAND_INVALID_WORLD_ARGUMENT = LangText.of("Error.Command.Argument.InvalidWorld",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose("Can not find world " + GENERIC_VALUE) + "!"));

    public static final LangText ERROR_COMMAND_INVALID_NUMBER_ARGUMENT = LangText.of("Error.Command.Argument.InvalidNumber",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid number!"));

    public static final LangText ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT = LangText.of("Error.Command.Argument.InvalidMaterial",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid material!"));

    public static final LangText ERROR_COMMAND_INVALID_ENCHANTMENT_ARGUMENT = LangText.of("Error.Command.Argument.InvalidEnchantment",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid enchantment!"));

    public static final LangText ERROR_COMMAND_NOT_YOURSELF = LangText.of("Error.Command.NotYourself",
        RED.enclose("This command can not be used on yourself."));

    public static final LangText ERROR_COMMAND_PLAYER_ONLY = LangText.of("Error.Command.PlayerOnly",
        RED.enclose("This command is for players only."));

    public static final LangText ERROR_COMMAND_USAGE = LangText.of("Error.Command.Usage",
        TAG_NO_PREFIX,
        " ",
        RED.enclose("Error: ") + GRAY.enclose("Wrong arguments!"),
        RED.enclose("Usage: ") + YELLOW.enclose("/" + COMMAND_LABEL) + " " + ORANGE.enclose(COMMAND_USAGE),
        " "
    );

    public static final LangText EDITOR_ACTION_EXIT = LangText.of("Editor.Action.Exit",
        TAG_NO_PREFIX,
        "",
        GRAY.enclose("Click " +
            CLICK.enclose(
                HOVER.enclose(GREEN.enclose("[Here]"), HoverEvent.Action.SHOW_TEXT, GRAY.enclose("Click to cancel")),
                ClickEvent.Action.RUN_COMMAND, "/" + Dialog.EXIT
            )
            + " to leave input mode."),
        "");

    public static final LangString EDITOR_INPUT_HEADER_MAIN       = LangString.of("Editor.Input.Header.Main", GREEN.enclose(BOLD.enclose("Input Mode")));
    public static final LangString EDITOR_INPUT_HEADER_ERROR      = LangString.of("Editor.Input.Header.Error", RED.enclose(BOLD.enclose("ERROR")));
    public static final LangString EDITOR_INPUT_ERROR_NOT_INTEGER = LangString.of("Editor.Input.Error.NotInteger", GRAY.enclose("Expecting " + RED.enclose("whole") + " number!"));
    public static final LangString EDITOR_INPUT_ERROR_GENERIC     = LangString.of("Editor.Input.Error.Generic", GRAY.enclose("Invalid value!"));

    public static final LangItem EDITOR_ITEM_CLOSE         = LangItem.of("Editor.Generic.Close", LIGHT_RED.enclose(BOLD.enclose("Exit")));
    public static final LangItem EDITOR_ITEM_RETURN        = LangItem.of("Editor.Generic.Return", LIGHT_GRAY.enclose(BOLD.enclose("Return")));
    public static final LangItem EDITOR_ITEM_NEXT_PAGE     = LangItem.of("Editor.Generic.NextPage", LIGHT_GRAY.enclose("Next Page →"));
    public static final LangItem EDITOR_ITEM_PREVIOUS_PAGE = LangItem.of("Editor.Generic.PreviousPage", LIGHT_GRAY.enclose("← Previous Page"));

    public static final LangString NUMBER_SHORT_THOUSAND    = LangString.of("Number.Thousand", "k");
    public static final LangString NUMBER_SHORT_MILLION     = LangString.of("Number.Million", "m");
    public static final LangString NUMBER_SHORT_BILLION     = LangString.of("Number.Billion", "b");
    public static final LangString NUMBER_SHORT_TRILLION    = LangString.of("Number.Trillion", "t");
    public static final LangString NUMBER_SHORT_QUADRILLION = LangString.of("Number.Quadrillion", "q");

    @NotNull
    public static String getYesOrNo(boolean value) {
        return (value ? OTHER_YES : OTHER_NO).getString();
    }

    @NotNull
    public static String getEnabledOrDisabled(boolean value) {
        return (value ? OTHER_ENABLED : OTHER_DISABLED).getString();
    }

    @NotNull
    public static String goodEntry(@NotNull String str) {
        return ENTRY_GOOD.getString().replace(GENERIC_ENTRY, str);
    }

    @NotNull
    public static String badEntry(@NotNull String str) {
        return ENTRY_BAD.getString().replace(GENERIC_ENTRY, str);
    }

    @NotNull
    public static String warnEntry(@NotNull String str) {
        return ENTRY_WARN.getString().replace(GENERIC_ENTRY, str);
    }
}
