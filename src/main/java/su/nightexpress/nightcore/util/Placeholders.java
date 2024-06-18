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

    public static final String GITHUB_URL     = "https://github.com/nulli0n/nightcore-spigot";
    public static final String WIKI_MAIN_URL     = GITHUB_URL + "/wiki/";
    public static final String WIKI_ITEMS_URL    = WIKI_MAIN_URL + "ItemStack-Options";
    public static final String WIKI_TEXT_URL     = WIKI_MAIN_URL + "Text-Formation";
    public static final String WIKI_LANG_URL     = WIKI_MAIN_URL + "Language-Formation";

    public static final String DEFAULT     = "default";
    public static final String NONE        = "none";
    public static final String WILDCARD    = "*";

    public static final String GENERIC_NAME   = "%name%";
    public static final String GENERIC_ITEM   = "%item%";
    public static final String GENERIC_VALUE  = "%value%";
    public static final String GENERIC_AMOUNT = "%amount%";
    public static final String GENERIC_ENTRY =  "%entry%";

    public static final String TAG_NO_PREFIX  = MessageTags.NO_PREFIX.getBracketsName();
    public static final String TAG_LINE_BREAK = Tags.LINE_BREAK.getBracketsName();

    public static final String PLAYER_NAME         = "%player_name%";
    public static final String PLAYER_DISPLAY_NAME = "%player_display_name%";

    public static final String LOCATION_X     = "%location_x%";
    public static final String LOCATION_Y     = "%location_y%";
    public static final String LOCATION_Z     = "%location_z%";
    public static final String LOCATION_WORLD = "%location_world%";

    public static final String SKIN_WRONG_MARK       = "27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065";
    public static final String SKIN_QUESTION_MARK    = "2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec";
    public static final String SKIN_CHECK_MARK       = "a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756";
    public static final String SKIN_EXCLAMATION_MARK = "7b41996fd20ca21d79adfc0e12057b2f2ceadf7b3cf5bb5f8a92fe3460161acd";
    public static final String SKIN_ARROW_LEFT       = "86971dd881dbaf4fd6bcaa93614493c612f869641ed59d1c9363a3666a5fa6";
    public static final String SKIN_ARROW_RIGHT      = "f32ca66056b72863e98f7f32bd7d94c7a0d796af691c9ac3a9136331352288f9";
    public static final String SKIN_ARROW_DOWN       = "be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46";

    public static final String COMMAND_USAGE       = "%command_usage%";
    public static final String COMMAND_DESCRIPTION = "%command_description%";
    public static final String COMMAND_LABEL       = "%command_label%";

    @NotNull
    public static UnaryOperator<String> forLocation(@NotNull Location location) {
        return new PlaceholderMap()
            .add(LOCATION_X, () -> String.valueOf(location.getBlockX()))
            .add(LOCATION_Y, () -> String.valueOf(location.getBlockY()))
            .add(LOCATION_Z, () -> String.valueOf(location.getBlockZ()))
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
