package su.nightexpress.nightcore.util.rankmap;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Map;
import java.util.function.Function;

@Deprecated
public class IntRankMap extends RankMap<Integer> {

    public static final Creator<Integer, IntRankMap> CREATOR   = IntRankMap::new;
    public static final Function<Double, Integer>    CONVERTER = Double::intValue;

    public IntRankMap(@NonNull Mode mode, @NonNull String permissionPrefix, int defaultValue,
                      @NonNull Map<String, Integer> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @Override
    @NonNull
    public IntRankMap addValue(@NonNull String key, @NonNull Integer value) {
        super.addValue(key, value);
        return this;
    }

    @NonNull
    public static IntRankMap ranked(int defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NonNull
    public static IntRankMap permissioned(@NonNull String prefix, int defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NonNull
    public static ConfigValue<IntRankMap> asConfigValue(@NonNull String path, @NonNull IntRankMap defaultValue,
                                                        @NonNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NonNull
    public static IntRankMap read(@NonNull FileConfig config, @NonNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
