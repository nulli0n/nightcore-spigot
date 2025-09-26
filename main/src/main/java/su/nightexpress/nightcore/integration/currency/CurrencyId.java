package su.nightexpress.nightcore.integration.currency;

import org.jetbrains.annotations.NotNull;

public class CurrencyId {

    public static final String DUMMY = "dummy";

    public static final String XP_LEVELS = "xp_level";
    public static final String XP_POINTS = "xp_points";

    public static final String COINS_ENGINE_PREFIX  = "coinsengine_";
    public static final String ULTRA_ECONOMY_PREFIX = "ultraeconomy_";

    public static final String PLAYER_POINTS = "playerpoints";
    public static final String BEAST_TOKENS  = "beasttokens";
    public static final String VOTING_PLUGIN = "votingplugin";
    public static final String ELITE_MOBS    = "elitemobs";
    public static final String VAULT         = "vault";

    @NotNull
    public static String forCoinsEngine(@NotNull String id) {
        return COINS_ENGINE_PREFIX + id.toLowerCase();
    }

    @NotNull
    public static String forUltraEconomy(@NotNull String id) {
        return ULTRA_ECONOMY_PREFIX + id.toLowerCase();
    }

    @NotNull
    public static String reroute(@NotNull String oldName) {
        if (oldName.equalsIgnoreCase("exp")) return XP_LEVELS;
        if (oldName.equalsIgnoreCase("level")) return XP_LEVELS;
        if (oldName.equalsIgnoreCase("xp")) return XP_POINTS;
        if (oldName.equalsIgnoreCase("money")) return VAULT;
        if (oldName.equalsIgnoreCase("economy")) return VAULT;

        return oldName;
    }
}
