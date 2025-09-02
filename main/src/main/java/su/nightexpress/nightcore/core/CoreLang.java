package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.language.entry.*;
import su.nightexpress.nightcore.ui.dialog.DialogManager;
import su.nightexpress.nightcore.ui.menu.click.ClickKey;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@Deprecated
public class CoreLang {

    @Deprecated
    public static final LangEnum<ClickKey> CLICK_KEY = LangEnum.of("ClickKey", ClickKey.class, map -> {
        map.put(ClickKey.LEFT, "L-Click");
        map.put(ClickKey.RIGHT, "R-Click");
        map.put(ClickKey.SHIFT_LEFT, "Shift + L-Click");
        map.put(ClickKey.SHIFT_RIGHT, "Shift + R-Click");
        map.put(ClickKey.DROP_KEY, "Drop [Q]");
        map.put(ClickKey.SWAP_KEY, "Swap [F]");
        map.put(ClickKey.DRAG_N_DROP, "Drag & Drop");
    });

    public static final LangString COMMAND_ARGUMENT_FORMAT_REQUIRED = LangString.of("Command.Argument.Type.Required",
        LIGHT_RED.wrap("<" + GENERIC_NAME + ">"));

    public static final LangString COMMAND_ARGUMENT_FORMAT_OPTIONAL = LangString.of("Command.Argument.Type.Optional",
        LIGHT_YELLOW.wrap("[" + GENERIC_NAME + "]"));

    public static final LangString COMMAND_FLAG_FORMAT = LangString.of("Command.Flag.Format",
        LIGHT_GRAY.wrap("[" + GENERIC_NAME + "]"));

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
        "  " + YELLOW.wrap(BOLD.wrap(GENERIC_NAME)) + GRAY.wrap(" - ") + YELLOW.wrap(BOLD.wrap("Commands:")),
        " ",
        GRAY.wrap("  " + RED.wrap(BOLD.wrap("<>")) + " - Required, " + GREEN.wrap(BOLD.wrap("[]")) + " - Optional."),
        " ",
        GENERIC_ENTRY,
        " "
    );

    public static final LangString COMMAND_HELP_ENTRY = LangString.of("Command.Help.Entry",
        "  " + YELLOW.wrap("/" + COMMAND_LABEL) + " " + ORANGE.wrap(COMMAND_USAGE) + GRAY.wrap(" - " + COMMAND_DESCRIPTION)
    );

    public static final LangString COMMAND_HELP_DESC      = LangString.of("Command.Help.Desc", "Show help page.");
    public static final LangString COMMAND_CHECKPERM_DESC = LangString.of("Command.CheckPerm.Desc", "Print player permission info.");
    public static final LangString COMMAND_DUMPITEM_DESC  = LangString.of("Command.DumpItem.Desc", "Print item components info.");
    public static final LangString COMMAND_RELOAD_DESC    = LangString.of("Command.Reload.Desc", "Reload the plugin.");

    public static final LangText COMMAND_RELOAD_DONE = LangText.of("Command.Reload.Done",
        LIGHT_GRAY.wrap("Plugin " + LIGHT_GREEN.wrap("reloaded") + "!"));

    public static final LangString TIME_DAY       = LangString.of("Time.Day", GENERIC_AMOUNT + "d.");
    public static final LangString TIME_HOUR      = LangString.of("Time.Hour", GENERIC_AMOUNT + "h.");
    public static final LangString TIME_MINUTE    = LangString.of("Time.Min", GENERIC_AMOUNT + "min.");
    public static final LangString TIME_SECOND    = LangString.of("Time.Sec", GENERIC_AMOUNT + "sec.");
    public static final LangString TIME_DELIMITER = LangString.of("Time.Delimiter", " ");

    public static final LangString OTHER_YES       = LangString.of("Other.Yes", GREEN.wrap("Yes"));
    public static final LangString OTHER_NO        = LangString.of("Other.No", RED.wrap("No"));
    public static final LangString OTHER_ENABLED   = LangString.of("Other.Enabled", GREEN.wrap("Enabled"));
    public static final LangString OTHER_DISABLED  = LangString.of("Other.Disabled", RED.wrap("Disabled"));
    public static final LangString OTHER_ANY       = LangString.of("Other.Any", "Any");
    public static final LangString OTHER_NONE      = LangString.of("Other.None", "None");
    public static final LangString OTHER_NEVER     = LangString.of("Other.Never", "Never");
    public static final LangString OTHER_ONE_TIMED = LangString.of("Other.OneTimed", "One-Timed");
    public static final LangString OTHER_UNLIMITED = LangString.of("Other.Unlimited", "Unlimited");
    public static final LangString OTHER_INFINITY  = LangString.of("Other.Infinity", "∞");

    public static final LangString ENTRY_GOOD = LangString.of("Entry.Good", GREEN.wrap("✔") + " " + GRAY.wrap(GENERIC_ENTRY));
    public static final LangString ENTRY_BAD  = LangString.of("Entry.Bad", RED.wrap("✘") + " " + GRAY.wrap(GENERIC_ENTRY));
    public static final LangString ENTRY_WARN = LangString.of("Entry.Warn", ORANGE.wrap("[❗]") + " " + GRAY.wrap(GENERIC_ENTRY));

    public static final LangText ERROR_INVALID_PLAYER = LangText.of("Error.Invalid_Player",
        RED.wrap("Invalid player!"));

    public static final LangText ERROR_INVALID_WORLD = LangText.of("Error.Invalid_World",
        RED.wrap("Invalid world!"));

    public static final LangText ERROR_INVALID_NUMBER = LangText.of("Error.Invalid_Number",
        RED.wrap("Invalid number!"));

    @Deprecated
    public static final LangText ERROR_INVALID_MATERIAL = LangText.of("Error.InvalidMaterial",
        RED.wrap("Invalid material!"));

    @Deprecated
    public static final LangText ERROR_INVALID_ENCHANTMENT = LangText.of("Error.InvalidEnchantment",
        RED.wrap("Invalid enchantment!"));

    /*public static final LangText ERROR_INVALID_POTION_EFFECT = LangText.of("Error.InvalidPotionEffectType",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap(GENERIC_VALUE) + " is not a valid potion effect type!"));

    public static final LangText ERROR_INVALID_ATTRIBUTE = LangText.of("Error.InvalidAttribute",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap(GENERIC_VALUE) + " is not a valid attribute!"));*/

    public static final LangText ERROR_NO_PERMISSION = LangText.of("Error.NoPermission",
        RED.wrap("You don't have permissions to do that!"));

    public static final LangText ERROR_COMMAND_PARSE_FLAG = LangText.of("Error.Command.ParseFlag",
        LIGHT_GRAY.wrap("Invalid value " + LIGHT_RED.wrap(GENERIC_VALUE) + " for " + LIGHT_RED.wrap(GENERIC_NAME) + " flag."));

    public static final LangText ERROR_COMMAND_PARSE_ARGUMENT = LangText.of("Error.Command.ParseArgument",
        LIGHT_GRAY.wrap("Invalid value " + LIGHT_RED.wrap(GENERIC_VALUE) + " for " + LIGHT_RED.wrap(GENERIC_NAME) + " argument."));

    public static final LangText ERROR_COMMAND_INVALID_PLAYER_ARGUMENT = LangText.of("Error.Command.Argument.InvalidPlayer",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap("Can not find player " + GENERIC_VALUE) + "!"));

    public static final LangText ERROR_COMMAND_INVALID_WORLD_ARGUMENT = LangText.of("Error.Command.Argument.InvalidWorld",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap("Can not find world " + GENERIC_VALUE) + "!"));

    public static final LangText ERROR_COMMAND_INVALID_NUMBER_ARGUMENT = LangText.of("Error.Command.Argument.InvalidNumber",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap(GENERIC_VALUE) + " is not a valid number!"));

    public static final LangText ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT = LangText.of("Error.Command.Argument.InvalidMaterial",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap(GENERIC_VALUE) + " is not a valid material!"));

    public static final LangText ERROR_COMMAND_INVALID_ENCHANTMENT_ARGUMENT = LangText.of("Error.Command.Argument.InvalidEnchantment",
        LIGHT_GRAY.wrap(LIGHT_RED.wrap(GENERIC_VALUE) + " is not a valid enchantment!"));

    public static final LangText ERROR_COMMAND_NOT_YOURSELF = LangText.of("Error.Command.NotYourself",
        RED.wrap("This command can not be used on yourself."));

    public static final LangText ERROR_COMMAND_PLAYER_ONLY = LangText.of("Error.Command.PlayerOnly",
        RED.wrap("This command is for players only."));

    public static final LangText ERROR_COMMAND_USAGE = LangText.of("Error.Command.Usage",
        TAG_NO_PREFIX,
        " ",
        RED.wrap("Error: ") + GRAY.wrap("Wrong arguments!"),
        RED.wrap("Usage: ") + YELLOW.wrap("/" + COMMAND_LABEL) + " " + ORANGE.wrap(COMMAND_USAGE),
        " "
    );

    @Deprecated
    public static final LangText EDITOR_ACTION_EXIT = LangText.of("Editor.Action.Exit",
        TAG_NO_PREFIX,
        "",
        GRAY.wrap("Click " +
            CLICK.wrap(
                HOVER.wrap(GREEN.wrap("[Here]"), HoverEventType.SHOW_TEXT, GRAY.wrap("Click to cancel")),
                ClickEventType.RUN_COMMAND, "/" + Dialog.EXIT
            )
            + " to leave input mode."),
        "");

    @Deprecated
    public static final LangString DIALOG_HEADER = LangString.of("Dialog.Header",
        LIGHT_YELLOW.wrap(GENERIC_TIME));

    @Deprecated
    public static final LangString DIALOG_DEFAULT_PROMPT = LangString.of("Dialog.DefaultPrompt",
        LIGHT_GRAY.wrap("Enter " + LIGHT_GREEN.wrap("[Value]")));

    @Deprecated
    public static final LangText DIALOG_INFO_EXIT = LangText.of("Dialog.Info.Exit",
        TAG_NO_PREFIX,
        "",
        GRAY.wrap("Click " +
            CLICK.wrapRunCommand(
                HOVER.wrapShowText(GREEN.wrap("[Here]"), GRAY.wrap("Click to cancel.")),
                "/" + DialogManager.EXIT
            )
            + " to leave input mode."),
        "");

    @Deprecated
    public static final LangString EDITOR_INPUT_HEADER_MAIN       = LangString.of("Editor.Input.Header.Main", GREEN.wrap(BOLD.wrap("Input Mode")));
    @Deprecated
    public static final LangString EDITOR_INPUT_HEADER_ERROR      = LangString.of("Editor.Input.Header.Error", RED.wrap(BOLD.wrap("ERROR")));
    @Deprecated
    public static final LangString EDITOR_INPUT_ERROR_NOT_INTEGER = LangString.of("Editor.Input.Error.NotInteger", GRAY.wrap("Expecting " + RED.wrap("whole") + " number!"));
    @Deprecated
    public static final LangString EDITOR_INPUT_ERROR_GENERIC     = LangString.of("Editor.Input.Error.Generic", GRAY.wrap("Invalid value!"));

    @Deprecated
    public static final LangString EDITOR_BUTTON_NAME = LangString.of("Editor.Button.Name",
        LIGHT_YELLOW.wrap(BOLD.wrap(GENERIC_NAME))
    );

    @Deprecated
    public static final LangString EDITOR_BUTTON_DESCRIPTION = LangString.of("Editor.Button.Description",
        GRAY.wrap(GENERIC_ENTRY)
    );

    @Deprecated
    public static final LangString EDITOR_BUTTON_CURRENT_DEFAULT_NAME = LangString.of("Editor.Button.Current.DefaultName",
        "Current"
    );

    @Deprecated
    public static final LangString EDITOR_BUTTON_CURRENT_INFO = LangString.of("Editor.Button.Current.Info",
        LIGHT_YELLOW.wrap("➥ " + GRAY.wrap(GENERIC_NAME + ": ") + GENERIC_VALUE)
    );

    @Deprecated
    public static final LangString EDITOR_BUTTON_CLICK_KEY = LangString.of("Editor.Button.ClickKey",
        LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap(GENERIC_NAME + " to " + GENERIC_VALUE))
    );


    @Deprecated
    public static final LangItem EDITOR_ITEM_CLOSE         = LangItem.of("Editor.Generic.Close", LIGHT_RED.wrap(BOLD.wrap("Exit")));
    @Deprecated
    public static final LangItem EDITOR_ITEM_RETURN        = LangItem.of("Editor.Generic.Return", LIGHT_GRAY.wrap(BOLD.wrap("Return")));
    @Deprecated
    public static final LangItem EDITOR_ITEM_NEXT_PAGE     = LangItem.of("Editor.Generic.NextPage", LIGHT_GRAY.wrap("Next Page →"));
    @Deprecated
    public static final LangItem EDITOR_ITEM_PREVIOUS_PAGE = LangItem.of("Editor.Generic.PreviousPage", LIGHT_GRAY.wrap("← Previous Page"));

    public static final LangUIButton EDITOR_ITEM_EXIT     = LangUIButton.builder("Editor.Item.Exit", LIGHT_RED.wrap(UNDERLINED.wrap("Exit"))).formatted(false).build();
    public static final LangUIButton EDITOR_ITEM_BACK     = LangUIButton.builder("Editor.Item.Return", WHITE.wrap(UNDERLINED.wrap("Return"))).formatted(false).build();
    public static final LangUIButton EDITOR_ITEM_NEXT     = LangUIButton.builder("Editor.Item.NextPage", LIGHT_YELLOW.wrap(UNDERLINED.wrap("Next Page") + " →")).formatted(false).build();
    public static final LangUIButton EDITOR_ITEM_PREVIOUS = LangUIButton.builder("Editor.Item.PreviousPage", LIGHT_YELLOW.wrap("← " + UNDERLINED.wrap("Previous Page"))).formatted(false).build();

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
