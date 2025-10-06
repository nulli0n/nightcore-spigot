package su.nightexpress.nightcore.core;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.number.NumberShortcut;
import su.nightexpress.nightcore.util.time.TimeFormats;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;

import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class CoreConfig {

    public static final String DIR_UI = "/ui/";

    @Deprecated
    public static final ConfigValue<Integer> DATA_FIXER_MISSING_VERSION = ConfigValue.create("DataFixer.MissingVersion",
        Version.MC_1_21_4.getDataVersion()
    );

    @Deprecated
    public static final ConfigValue<Integer> DATA_FIXER_UNKNOWN_VERSION = ConfigValue.create("DataFixer.UnknownVersion",
        10000
    );

    public static final ConfigValue<String> GENERAL_DATE_TIME_FORMAT = ConfigValue.create("General.DateTimeFormat",
        "dd/MM/yyyy HH:mm"
    ).whenRead(TimeFormats::setDateTimeFormatter);

    public static final ConfigValue<Long> MENU_CLICK_COOLDOWN = ConfigValue.create("Menu.Click_Cooldown",
        150L,
        "Sets cooldown (in milliseconds) for player clicks in GUIs.",
        "[Default is 150ms]"
    );

    public static final ConfigValue<Set<String>> ECONOMY_DISABLED_PROVIDERS = ConfigValue.create("Integrations.Economy.DisabledProviders",
        Lists.newSet("example_currency", "custom_economy"),
        "List of economy/currency IDs that will not be handled by the nightcore and it's child plugins."
    ).onRead(set -> Lists.modify(set, LowerCase.INTERNAL::apply));

    public static final ConfigValue<Boolean> ECONOMY_PLACEHOLDERS_API_FORMAT = ConfigValue.create("Integrations.Economy.PlaceholderAPI_In_Format",
        true,
        "Whether to apply " + Plugins.PLACEHOLDER_API + " placeholders in currency's Format setting."
    );

    public static final ConfigValue<Set<String>> ITEMS_DISABLED_PROVIDERS = ConfigValue.create("Integrations.Items.DisabledProviders",
        Lists.newSet("CustomSuperItems", "UltimateItemsPlugin"),
        "List of custom item plugins that will not be handled by the nightcore and it's child plugins."
    ).onRead(set -> Lists.modify(set, LowerCase.INTERNAL::apply));

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
        "Allowed modes: " + Enums.inline(RoundingMode.class),
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

    public static final ConfigValue<Integer> PROFILE_CACHE_UPDATE_TIME = ConfigValue.create("Profiles.Cache.UpdateTime",
        360,
        "Sets expire time (in minutes) for cached player profiles before they will make a call to the Mojang API to update their name and textures.",
        "[*] Profiles with custom skins for heads are excluded from updates.",
        "[*] Profile's expire time is reset on each profile query.",
        "[*] Set -1 to never update until the server reboot.",
        "[Default is 360 (6 hours)]"
    );

    public static final ConfigValue<Integer> PROFILE_CACHE_PURGE_TIME = ConfigValue.create("Profiles.Cache.PurgeTime",
        1440,
        "Sets expire time (in minutes) for cached player profiles before they will be purged from the cache and memory.",
        "[*] Profiles with custom skins for heads are excluded from being purged.",
        "[*] Profile's expire time is reset on each profile query.",
        "[*] Set -1 to never purge until the server reboot.",
        "[Default is 1440 (24 hours)]"
    );

    public static final ConfigValue<Integer> PROFILE_PURGE_INTERVAL = ConfigValue.create("Profiles.PurgeInterval",
        600,
        "How often (in seconds) to purge cached player profiles.",
        "[Default is 600 (10 minutes)]"
    );

    public static final ConfigValue<Integer> PROFILE_UPDATE_INTERVAL = ConfigValue.create("Profiles.UpdateInterval",
        1,
        "How often (in seconds) to perform updates for profiles that were put in update queue.",
        "",
        "[Default is 1]"
    );

    public static final ConfigValue<Integer> PROFILE_UPDATE_AMOUNT = ConfigValue.create("Profiles.UpdateAmount",
        1,
        "Amount of profiles to be updated from a queue (see UpdateInterval).",
        "[Default is 1]"
    );

    public static final ConfigValue<Boolean> PROFILE_UPDATE_ON_JOIN = ConfigValue.create("Profiles.UpdateOnJoin",
        true,
        "Controls whether cached player profiles should force update when their owner joins the server.",
        "[Default is true]"
    );

    public static final ConfigValue<Boolean> PROFILE_FETCH_CUSTOM = ConfigValue.create("Profiles.FetchCustom",
        false,
        "Controls whether profiles with custom textures can query Mojang API to fetch missing properties.",
        "[*] Enable this only if you're experiencing issues with texture loading for custom heads.",
        "[*] Enabling this setting may get you rate limited from Mojang.",
        "[Default is false]"
    );
}
