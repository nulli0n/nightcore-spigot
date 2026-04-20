package su.nightexpress.nightcore.integration.currency;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.LowerCase;

public class CurrencyId {

    public static final String DUMMY = "dummy";

    public static final String XP_LEVELS = "xp_level";
    public static final String XP_POINTS = "xp_points";

    public static final String COINS_ENGINE_PREFIX      = "coinsengine_";
    public static final String EXCELLENT_ECONOMY_PREFIX = "excellenteconomy_";

    public static final String PLAYER_POINTS = "playerpoints";
    public static final String BEAST_TOKENS  = "beasttokens";
    public static final String VOTING_PLUGIN = "votingplugin";
    public static final String ELITE_MOBS    = "elitemobs";
    public static final String VAULT         = "vault";

    @NonNull
    public static String forCoinsEngine(@NonNull String id) {
        return COINS_ENGINE_PREFIX + LowerCase.INTERNAL.apply(id);
    }

    @NonNull
    public static String forExcellentEconomy(@NonNull String id) {
        return EXCELLENT_ECONOMY_PREFIX + LowerCase.INTERNAL.apply(id);
    }


    @NonNull
    public static String reroute(@NonNull String oldName) {
        if (oldName.equalsIgnoreCase("exp")) return XP_LEVELS;
        if (oldName.equalsIgnoreCase("level")) return XP_LEVELS;
        if (oldName.equalsIgnoreCase("xp")) return XP_POINTS;
        if (oldName.equalsIgnoreCase("money")) return VAULT;
        if (oldName.equalsIgnoreCase("economy")) return VAULT;

        return oldName;
    }
}
