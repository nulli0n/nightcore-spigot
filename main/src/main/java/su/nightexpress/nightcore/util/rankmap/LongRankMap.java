package su.nightexpress.nightcore.util.rankmap;

import java.util.Map;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

@Deprecated
public class LongRankMap extends RankMap<Long> {

    public static final Creator<Long, LongRankMap> CREATOR   = LongRankMap::new;
    public static final Function<Double, Long>     CONVERTER = Double::longValue;

    public LongRankMap(RankMap.@NonNull Mode mode, @NonNull String permissionPrefix, long defaultValue,
                       @NonNull Map<String, Long> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @NonNull
    public static LongRankMap ranked(long defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NonNull
    public static LongRankMap permissioned(@NonNull String prefix, long defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NonNull
    public static ConfigValue<LongRankMap> asConfigValue(@NonNull String path, @NonNull LongRankMap defaultValue,
                                                         @NonNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NonNull
    public static LongRankMap read(@NonNull FileConfig config, @NonNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
