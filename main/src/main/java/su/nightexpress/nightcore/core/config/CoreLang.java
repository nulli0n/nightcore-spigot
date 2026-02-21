package su.nightexpress.nightcore.core.config;

import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.*;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public final class CoreLang implements LangContainer {

    public static final RegistryLocale<DamageType> DAMAGE_TYPE = LangEntry.builder("Bukkit.DamageType").registry(RegistryType.DAMAGE_TYPE);

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

    public static final TextLocale COMMAND_USAGE_REQUIRED_ARGUMENT = LangEntry.builder("Command.Usage.RequiredArgument")
        .text(COLOR.with("#FF6B6B").and(SHADOW_1_0.with("#9C172B")).wrap("<" + GENERIC_NAME + ">"));

    public static final TextLocale COMMAND_USAGE_OPTIONAL_ARGUMENT = LangEntry.builder("Command.Usage.OptionalArgument")
        .text(COLOR.with("#4ADE80").and(SHADOW_1_0.with("#15803D")).wrap("[" + GENERIC_NAME + "]"));

    public static final TextLocale COMMAND_USAGE_OPTIONAL_FLAG     = LangEntry.builder("Command.Usage.OptionalFlag")
        .text(COLOR.with("#60A5FA").and(SHADOW_1_0.with("#1D4ED8")).wrap("[" + GENERIC_NAME + "]"));

    public static final TextLocale COMMAND_HELP_DESC      = LangEntry.builder("Command.Help.Desc").text("List all sub-commands.");
    public static final TextLocale COMMAND_RELOAD_DESC    = LangEntry.builder("Command.Reload.Desc").text("Reload the plugin.");
    public static final TextLocale COMMAND_CHECKPERM_DESC = LangEntry.builder("Command.CheckPerm.Desc").text("Print player permissions info.");
    public static final TextLocale COMMAND_DUMPITEM_DESC  = LangEntry.builder("Command.DumpItem.Desc").text("Print item NBT info.");

    public static final TextLocale COMMAND_ECONOMY_BRIDGE_NAME           = LangEntry.builder("Command.EconomyBridge.Name").text("Economy Bridge");
    public static final TextLocale COMMAND_ECONOMY_BRIDGE_DESC           = LangEntry.builder("Command.EconomyBridge.Desc").text("Economy Bridge commands.");
    public static final TextLocale COMMAND_ECONOMY_BRIDGE_FROM_ITEM_DESC = LangEntry.builder("Command.EconomyBridge.FromItem.Desc").text("Create item currency.");

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
            RED.wrap("Error: ") + COLOR.with("#D9D9D9").and(SHADOW_1_0.with("#5D5D5D")).wrap("Missing required arguments!"),
            RED.wrap("Usage: ") + COLOR.with("#FCC549").and(SHADOW_1_0.with("#B45115")).wrap("/" + GENERIC_COMMAND),
            " "
        );



    public static final MessageLocale HELP_PAGE_GENERAL = LangEntry.builder("HelpPage.General")
        .message(MessageData.CHAT_NO_PREFIX,
            COLOR.with("#FCE491").and(SHADOW_1_0.with("#C5741A")).and(STRIKETHROUGH).wrap(" ".repeat(60)),
            COLOR.with("#FCC549").and(SHADOW_1_0.with("#B45115")).wrap("  " + BOLD.wrap(GENERIC_NAME + " Commands")),
            " ",
            "  " + SPRITE_GUI.apply("icon/info") + COLOR.with("#D9D9D9").and(SHADOW_1_0.with("#5D5D5D")).wrap("Hover over a command to see description."),
            " ",
            GENERIC_ENTRY,
            COLOR.with("#FCE491").and(SHADOW_1_0.with("#C5741A")).and(STRIKETHROUGH).wrap(" ".repeat(60))
        );

    public static final TextLocale HELP_PAGE_ENTRY = LangEntry.builder("HelpPage.Entry")
        .text(
            SHOW_TEXT.with(COLOR.with("#D9D9D9").and(SHADOW_1_0.with("#5D5D5D")).wrap(GENERIC_DESCRIPTION)).wrap(
            COLOR.with("#FCC549").and(SHADOW_1_0.with("#B45115")).wrap("  • /" + GENERIC_COMMAND))
        );

    public static final MessageLocale PLUGIN_RELOADED = LangEntry.builder("Plugin.Reloaded")
        .chatMessage(GRAY.wrap("Plugin " + GREEN.wrap("reloaded") + "!"));


    public static final MessageLocale ECONOMY_BRIDGE_FROM_ITEM_NOTHING = LangEntry.builder("EconomyBridge.FromItem.Nothing").chatMessage(
        RED.wrap("You must hold an item in hand!")
    );

    public static final MessageLocale ECONOMY_BRIDGE_FROM_ITEM_EXISTS = LangEntry.builder("EconomyBridge.FromItem.Exists").chatMessage(
        RED.wrap("Currency with this name already exists!")
    );

    public static final MessageLocale ECONOMY_BRIDGE_FROM_ITEM_CREATED = LangEntry.builder("EconomyBridge.FromItem.Created").chatMessage(
        GRAY.wrap("Created " + YELLOW.wrap(CURRENCY_NAME) + " currency as " + YELLOW.wrap(CURRENCY_ID) + ".")
    );

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

    public static final TextLocale ENTRY_GOOD = LangEntry.builder("Entry.Valid").text(GREEN.wrap("✔") + " " + GRAY.wrap("%s"));
    public static final TextLocale ENTRY_BAD  = LangEntry.builder("Entry.Invalid").text(RED.wrap("✘") + " " + GRAY.wrap("%s"));

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

    public static final ButtonLocale DIALOG_BUTTON_OK     = LangEntry.builder("Dialog.Button.OK").button(GREEN.wrap("✔") + " OK");
    public static final ButtonLocale DIALOG_BUTTON_CANCEL = LangEntry.builder("Dialog.Button.Cancel").button(RED.wrap("✘") + " Cancel");
    public static final ButtonLocale DIALOG_BUTTON_BACK   = LangEntry.builder("Dialog.Button.Back").button(SOFT_YELLOW.wrap("←") + " Back");

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
