package su.nightexpress.nightcore.core;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.number.NumberShortcut;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;

import java.math.RoundingMode;
import java.util.Map;
import java.util.TimeZone;

public class CoreConfig {

    public static final String DIR_UI = "/ui/";

    public static final ConfigValue<Integer> DATA_FIXER_MISSING_VERSION = ConfigValue.create("DataFixer.MissingVersion",
        Version.MC_1_21_4.getDataVersion()
    );

    public static final ConfigValue<Integer> DATA_FIXER_UNKNOWN_VERSION = ConfigValue.create("DataFixer.UnknownVersion",
        10000
    );

    public static final ConfigValue<Long> MENU_CLICK_COOLDOWN = ConfigValue.create("Menu.Click_Cooldown",
        150L,
        "Sets cooldown (in milliseconds) for player clicks in GUIs.",
        "[Default is 150ms]"
    );

    @Deprecated
    public static final ConfigValue<Integer> USER_CACHE_LIFETIME = ConfigValue.create("UserData.Cache.LifeTime",
        300,
        "[ OUTDATED, MOVED IN PER-PLUGIN engine.yml ]"
    );

    public static final ConfigValue<String> TIME_ZONE = ConfigValue.create("Time.TimeZone",
        TimeZone.getDefault().getID(),
        "Sets timezone for plugins to handle local dates and times properly."
    ).whenRead(TimeUtil::setTimeZone);

    public static final ConfigValue<Boolean> LEGACY_COLOR_SUPPORT = ConfigValue.create("Engine.Legacy_Color_Support",
        true,
        "Allows to use legacy color codes (such as '&7', '&l', etc.) in plugin configurations.",
        "This setting exist for backwards compatibility only! There are NO GUARANTEES that it will persist for future updates!",
        "You should NOT use it unless you need to add support for your custom language configurations & translations with legacy codes.",
        "You should migrate to new text format as soon as possible: " + Placeholders.URL_WIKI_TEXT
    );

    public static final ConfigValue<UniFormatter> NUMBER_FORMAT = ConfigValue.create("Number.Format",
        UniFormatter::read,
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
