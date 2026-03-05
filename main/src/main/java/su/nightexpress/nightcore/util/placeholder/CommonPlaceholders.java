package su.nightexpress.nightcore.util.placeholder;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.placeholder.PAPI;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;

import java.util.function.UnaryOperator;

public class CommonPlaceholders {

    public static final String DEFAULT     = "default";
    public static final String NONE        = "none";

    public static final String GENERIC_NAME        = "%name%";
    public static final String GENERIC_ITEM        = "%item%";
    public static final String GENERIC_VALUE       = "%value%";
    public static final String GENERIC_AMOUNT      = "%amount%";
    public static final String GENERIC_ENTRY       = "%entry%";
    public static final String GENERIC_TIME        = "%time%";
    public static final String GENERIC_INPUT       = "%input%";
    public static final String GENERIC_COMMAND     = "%command%";
    public static final String GENERIC_DESCRIPTION = "%description%";

    public static final String PLAYER_NAME         = "%player_name%";
    public static final String PLAYER_DISPLAY_NAME = "%player_display_name%";
    public static final String PLAYER_PREFIX       = "%player_prefix%";
    public static final String PLAYER_SUFFIX       = "%player_suffix%";
    public static final String PLAYER_WORLD        = "%player_world%";

    public static final String LOCATION_X     = "%location_x%";
    public static final String LOCATION_Y     = "%location_y%";
    public static final String LOCATION_Z     = "%location_z%";
    public static final String LOCATION_WORLD = "%location_world%";

    public static final String EMPTY_IF_ABOVE = "%empty-if-above%";
    public static final String EMPTY_IF_BELOW = "%empty-if-below%";
    public static final String EMPTY_IF_BOTH  = "%empty-if-both%";

    public static final String CURRENCY_NAME = "%currency_name%";
    public static final String CURRENCY_ID   = "%currency_id%";

    public static final TypedPlaceholder<Player> PLAYER = TypedPlaceholder.builder(Player.class)
        .with(PLAYER_NAME, Player::getName)
        .with(PLAYER_DISPLAY_NAME, Players::getDisplayNameSerialized)
        .with(PLAYER_PREFIX, Players::getPrefixOrEmpty)
        .with(PLAYER_SUFFIX, Players::getSuffixOrEmpty)
        .with(PLAYER_WORLD, player -> player.getWorld().getName())
        .build();

    public static final TypedPlaceholder<Location> LOCATION = TypedPlaceholder.builder(Location.class)
        .with(LOCATION_X, location -> NumberUtil.format(location.getX()))
        .with(LOCATION_Y, location -> NumberUtil.format(location.getY()))
        .with(LOCATION_Z, location -> NumberUtil.format(location.getZ()))
        .with(LOCATION_WORLD, location -> location.getWorld().getName())
        .build();

    @NonNull
    public static UnaryOperator<String> forPlaceholderAPI(@Nullable Player player) {
        return string -> setPAPIPlaceholders(player, string);
    }

    @NonNull
    public static String withoutBrackets(@NonNull String placeholder) {
        int length = placeholder.length();

        if (length > 2 && placeholder.startsWith("%") && placeholder.endsWith("%")) {
            return placeholder.substring(1, length - 1);
        }

        return placeholder;
    }
    
    public static String setPAPIPlaceholders(@Nullable Player player, @NonNull String string) {
        return PAPI.setPlaceholders(player, string);
    }

    public static String setPAPIBracketPlaceholders(@Nullable Player player, @NonNull String string) {
        return PAPI.setBracketPlaceholders(player, string);
    }
}
