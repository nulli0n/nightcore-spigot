package su.nightexpress.nightcore.core;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.number.NumberShortcut;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;

import java.math.RoundingMode;
import java.util.Map;

public class CoreConfig {

    @Deprecated
    public static final ConfigValue<Boolean> DATA_FIXER_ENABLED = ConfigValue.create("DataFixer.Enabled",
        false,
        "When enabled, uses Mojang's DataFixer util to update ItemStacks from <= 1.20.4 NBT format to 1.20.5+ NBT format.",
        "IMPORTANT NOTE: You need to use this setting only once! Enable it, reboot the server and re-save all configurations that stores compressed item data: shops, crates, etc."
    );

    @Deprecated
    public static final ConfigValue<Boolean> MODERN_TEXT_PRECOMPILE_LANG = ConfigValue.create("ModernTextFormation.Precompile_Language",
        true,
        "When enabled, parses (deserializes) language messages to Spigot TextComponent(s) on plugin load.",
        "When disabled, parses (deserializes) language texts to Spigot TextComponent(s) in runtime when sending it to player.",
        "Enabling this setting is good for performance, however messages with placeholders will be recompiled in runtime anyway.",
        "(Side Note) Unlike NexEngine, it does not uses regex anymore, which makes deserialization significantly faster and accurate.",
        "[Default is true]"
    );

    public static final ConfigValue<Long> MENU_CLICK_COOLDOWN = ConfigValue.create("Menu.Click_Cooldown",
        150L,
        "Sets cooldown (in milliseconds) for player clicks in GUIs.",
        "[Default is 150ms]"
    );

//    @Deprecated
//    public static final ConfigValue<Boolean> USER_DEBUG_ENABLED = ConfigValue.create("UserData.Debug",
//        false,
//        "Enables debug messages for user data management.",
//        "[Default is false]");

    @Deprecated
    public static final ConfigValue<Integer> USER_CACHE_LIFETIME = ConfigValue.create("UserData.Cache.LifeTime",
        300,
        "[ OUTDATED, MOVED IN PER-PLUGIN config.yml ]"
    );

//    @Deprecated
//    public static final ConfigValue<Boolean> USER_CACHE_NAME_AND_UUID = ConfigValue.create("UserData.Cache.Names_And_UUIDs",
//        true,
//        "Sets whether or not plugin will cache player names and UUIDs.",
//        "This will improve database performance when checking if user exists, but will use more memory to store UUIDs and names.",
//        "[Default is true]");

    public static final ConfigValue<Boolean> LEGACY_COLOR_SUPPORT = ConfigValue.create("Engine.Legacy_Color_Support",
        true,
        "Allows to use legacy color codes (such as '&7', '&l', etc.) in plugin configurations.",
        "This setting exist for backwards compatibility only! There are NO GUARANTEES that it will persist for future updates!",
        "You should NOT use it unless you need to add support for your custom language configurations & translations with legacy codes.",
        "You should migrate to new text format as soon as possible: " + Placeholders.URL_WIKI_TEXT
    );

//    public static final ConfigValue<Boolean> RESPECT_PLAYER_DISPLAYNAME = ConfigValue.create("Engine.Respect_Player_DisplayName",
//        false,
//        "Sets whether or not 'Player#getDisplayName' can be used to find & get players in addition to regular 'Player#getName'.",
//        "This is useful if you want to use custom player nicknames in commands.",
//        "(Works only for nightcore based plugins.)",
//        "[Default is false]");

    public static final ConfigValue<UniFormatter> NUMBER_FORMAT = ConfigValue.create("Number.Format",
        UniFormatter.of("#,###.###", RoundingMode.HALF_EVEN),
        "Control over how numerical data is formatted and rounded.",
        "Allowed modes: " + StringUtil.inlineEnum(RoundingMode.class, ", "),
        "A tutorial can be found here: https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html"
    );

    public static final ConfigValue<Integer> NUMBER_SHORTCUT_STEP = ConfigValue.create("Number.Shortcut_Step",
        1000,
        "Sets step for the Number Shortcut's magnitude.",
        "When a number is about to be compacted, it will be divided by this value to determine if it falls under specific Number Shortcut.",
        "[Default is 1000]"
    );

    public static final ConfigValue<Map<String, NumberShortcut>> NUMBER_SHORTCUT_LIST = ConfigValue.forMapById("Number.Shortcut_List",
        NumberShortcut::read,
        map -> {
            map.put("thousand", new NumberShortcut(1, "k"));
            map.put("million", new NumberShortcut(2, "m"));
            map.put("billion", new NumberShortcut(3, "b"));
            map.put("trillion", new NumberShortcut(4, "t"));
            map.put("quadrillion", new NumberShortcut(5, "q"));
        },
        "Custom number shortcuts."
    );
}
