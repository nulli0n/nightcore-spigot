package su.nightexpress.nightcore.core;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.text.tag.impl.ColorTag;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;

import java.awt.*;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CoreConfig {

    public static final ConfigValue<Boolean> DATA_FIXER_ENABLED = ConfigValue.create("DataFixer.Enabled",
        true,
        "When enabled, uses Mojang's DataFixer util to update ItemStacks from <= 1.20.4 NBT format to 1.20.5+ NBT format.",
        "IMPORTANT NOTE: You need to use this setting only once! Enable it, reboot the server and re-save all configurations that stores compressed item data: shops, crates, etc."
    );

    public static final ConfigValue<Boolean> MODERN_TEXT_PRECOMPILE_LANG = ConfigValue.create("ModernTextFormation.Precompile_Language",
        true,
        "When enabled, parses (deserializes) language messages to Spigot TextComponent(s) on plugin load.",
        "When disabled, parses (deserializes) language texts to Spigot TextComponent(s) in runtime when sending it to player.",
        "Enabling this setting is good for performance, however messages with placeholders will be recompiled in runtime anyway.",
        "(Side Note) Unlike NexEngine, it does not uses regex anymore, which makes deserialization significantly faster and accurate.",
        "[Default is true]");

    public static final ConfigValue<Map<String, Color>> MODERN_TEXT_DEFAULT_COLORS = ConfigValue.forMap("ModernTextFormation.Default_Colors",
        (cfg, path, id) -> {
            try {
                Color color = Color.decode(cfg.getString(path + "." + id, "#ffffff"));
                Tags.registerTag(new ColorTag(id, color));
                return color;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return Color.WHITE;
            }
        },
        (cfg, path, map) -> map.forEach((id, value) -> cfg.set(path + "." + id, "#" + Integer.toHexString(value.getRGB()).substring(2))),
        () -> {
            Map<String, Color> map = new HashMap<>();
            map.put("aqua", Color.CYAN);
            return map;
        },
        "Here you can define custom colors to use it in all nightcore plugins like <aqua>Text</aqua>.",
        "You can also override default ones."
    );

    public static final ConfigValue<Long> MENU_CLICK_COOLDOWN = ConfigValue.create("Menu.Click_Cooldown",
        150L,
        "Sets cooldown (in milliseconds) for player clicks in GUIs.",
        "[Default is 150ms]");

    public static final ConfigValue<Boolean> USER_DEBUG_ENABLED = ConfigValue.create("UserData.Debug",
        false,
        "Enables debug messages for user data management.",
        "[Default is false]");

    public static final ConfigValue<Integer> USER_CACHE_LIFETIME = ConfigValue.create("UserData.Cache.LifeTime",
        300,
        "Sets how long (in seconds) user data will be cached for offline users",
        "until removed and needs to be loaded from the database again.",
        "[Default is 300 (5 minutes)]");

    public static final ConfigValue<Boolean> USER_CACHE_NAME_AND_UUID = ConfigValue.create("UserData.Cache.Names_And_UUIDs",
        true,
        "Sets whether or not plugin will cache player names and UUIDs.",
        "This will improve database performance when checking if user exists, but will use more memory to store UUIDs and names.",
        "[Default is true]");

    public static final ConfigValue<Boolean> LEGACY_COLOR_SUPPORT = ConfigValue.create("Engine.Legacy_Color_Support",
        true,
        "Allows to use legacy color codes (such as '&7', '&l', etc.) in plugin configurations.",
        "This setting exist for backwards compatibility only! There are NO GUARANTEES that it will persist for future updates!",
        "You should NOT use it unless you need to add support for your custom language configurations & translations with legacy codes.",
        "You should migrate to new text format as soon as possible: " + Placeholders.WIKI_TEXT_URL);

    public static final ConfigValue<Boolean> RESPECT_PLAYER_DISPLAYNAME = ConfigValue.create("Engine.Respect_Player_DisplayName",
        false,
        "Sets whether or not 'Player#getDisplayName' can be used to find & get players in addition to regular 'Player#getName'.",
        "This is useful if you want to use custom player nicknames in commands.",
        "(Works only for nightcore based plugins.)",
        "[Default is false]");

    public static final ConfigValue<UniFormatter> NUMBER_FORMAT = ConfigValue.create("Engine.Number_Format",
        UniFormatter.of("#,###.##", RoundingMode.HALF_EVEN),
        "Control over how numerical data is formatted and rounded.",
        "Allowed modes: " + StringUtil.inlineEnum(RoundingMode.class, ", "),
        "A tutorial can be found here: https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html"
    );
}
