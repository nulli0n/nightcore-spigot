package su.nightexpress.nightcore.util.rankmap;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Map;
import java.util.function.Function;

public class DoubleRankMap extends RankMap<Double> {

    public static final Creator<Double, DoubleRankMap> CREATOR   = DoubleRankMap::new;
    public static final Function<Double, Double>       CONVERTER = Double::doubleValue;

    public DoubleRankMap(@NotNull RankMap.Mode mode, @NotNull String permissionPrefix, double defaultValue, @NotNull Map<String, Double> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @NotNull
    public static DoubleRankMap ranked(double defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NotNull
    public static DoubleRankMap permissioned(@NotNull String prefix, double defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NotNull
    public static ConfigValue<DoubleRankMap> asConfigValue(@NotNull String path, @NotNull DoubleRankMap defaultValue, @NotNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NotNull
    public static DoubleRankMap read(@NotNull FileConfig config, @NotNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
