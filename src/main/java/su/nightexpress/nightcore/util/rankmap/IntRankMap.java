package su.nightexpress.nightcore.util.rankmap;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Map;
import java.util.function.Function;

public class IntRankMap extends RankMap<Integer> {

    public static final Creator<Integer, IntRankMap> CREATOR   = IntRankMap::new;
    public static final Function<Double, Integer>    CONVERTER = Double::intValue;

    public IntRankMap(@NotNull Mode mode, @NotNull String permissionPrefix, int defaultValue, @NotNull Map<String, Integer> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @Override
    @NotNull
    public IntRankMap addValue(@NotNull String key, @NotNull Integer value) {
        super.addValue(key, value);
        return this;
    }

    @NotNull
    public static IntRankMap ranked(int defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NotNull
    public static IntRankMap permissioned(@NotNull String prefix, int defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NotNull
    public static ConfigValue<IntRankMap> asConfigValue(@NotNull String path, @NotNull IntRankMap defaultValue, @NotNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NotNull
    public static IntRankMap read(@NotNull FileConfig config, @NotNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
