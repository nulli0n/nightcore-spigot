package su.nightexpress.nightcore.util.rankmap;

import java.util.Map;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

@Deprecated
public class DoubleRankMap extends RankMap<Double> {

    public static final Creator<Double, DoubleRankMap> CREATOR   = DoubleRankMap::new;
    public static final Function<Double, Double>       CONVERTER = Double::doubleValue;

    public DoubleRankMap(RankMap.@NonNull Mode mode, @NonNull String permissionPrefix, double defaultValue,
                         @NonNull Map<String, Double> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @NonNull
    public static DoubleRankMap ranked(double defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NonNull
    public static DoubleRankMap permissioned(@NonNull String prefix, double defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NonNull
    public static ConfigValue<DoubleRankMap> asConfigValue(@NonNull String path, @NonNull DoubleRankMap defaultValue,
                                                           @NonNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NonNull
    public static DoubleRankMap read(@NonNull FileConfig config, @NonNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
