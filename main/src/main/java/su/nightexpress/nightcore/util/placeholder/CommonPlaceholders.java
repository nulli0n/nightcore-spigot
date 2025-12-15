package su.nightexpress.nightcore.util.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;

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
        .build();

    @NotNull
    public static UnaryOperator<String> forPlaceholderAPI(@Nullable Player player) {
        if (!Plugins.hasPlaceholderAPI()) return string -> string;

        return s -> PlaceholderAPI.setPlaceholders(player, s);
    }
}
