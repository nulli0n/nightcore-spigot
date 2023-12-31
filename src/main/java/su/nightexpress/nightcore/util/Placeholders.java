package su.nightexpress.nightcore.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.tag.MessageTags;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.function.UnaryOperator;

public class Placeholders {

    public static final String GITHUB_URL     = "https://github.com/nulli0n/nightcore-spigot/";
    public static final String WIKI_MAIN_URL     = GITHUB_URL + "wiki/";
    public static final String WIKI_ITEMS_URL    = WIKI_MAIN_URL + "Configuration-Tips#item-sections";
    public static final String WIKI_LANG_URL     = WIKI_MAIN_URL + "Language-Config";

    public static final String DEFAULT     = "default";
    public static final String NONE        = "none";
    public static final String WILDCARD    = "*";

    public static final String GENERIC_NAME   = "%name%";
    public static final String GENERIC_ITEM   = "%item%";
    public static final String GENERIC_VALUE  = "%value%";
    public static final String GENERIC_AMOUNT = "%amount%";
    public static final String GENERIC_ENTRY =  "%entry%";

    public static final String TAG_NO_PREFIX = MessageTags.NO_PREFIX.getFullName();
    public static final String TAG_LINE_BREAK = Tags.LINE_BREAK.getFullName();

    public static final String PLAYER_NAME         = "%player_name%";
    public static final String PLAYER_DISPLAY_NAME = "%player_display_name%";

    public static final String LOCATION_X     = "%location_x%";
    public static final String LOCATION_Y     = "%location_y%";
    public static final String LOCATION_Z     = "%location_z%";
    public static final String LOCATION_WORLD = "%location_world%";

    public static final String COMMAND_USAGE       = "%command_usage%";
    public static final String COMMAND_DESCRIPTION = "%command_description%";
    public static final String COMMAND_LABEL       = "%command_label%";

    @NotNull
    public static UnaryOperator<String> forLocation(@NotNull Location location) {
        return new PlaceholderMap()
            .add(LOCATION_X, () -> NumberUtil.format(location.getX()))
            .add(LOCATION_Y, () -> NumberUtil.format(location.getY()))
            .add(LOCATION_Z, () -> NumberUtil.format(location.getZ()))
            .add(LOCATION_WORLD, () -> LocationUtil.getWorldName(location))
            .replacer();
    }

    @NotNull
    public static UnaryOperator<String> forPlayer(@NotNull Player player) {
        return new PlaceholderMap()
            .add(PLAYER_NAME, player::getName)
            .add(PLAYER_DISPLAY_NAME, player::getDisplayName)
            .replacer();
    }

    @NotNull
    public static UnaryOperator<String> forSender(@NotNull CommandSender sender) {
        if (sender instanceof Player player) return forPlayer(player);

        return new PlaceholderMap()
            .add(PLAYER_NAME, sender::getName)
            .add(PLAYER_DISPLAY_NAME, sender::getName)
            .replacer();
    }
}
