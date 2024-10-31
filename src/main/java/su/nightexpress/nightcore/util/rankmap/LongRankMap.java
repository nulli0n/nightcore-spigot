package su.nightexpress.nightcore.util.rankmap;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Map;
import java.util.function.Function;

public class LongRankMap extends RankMap<Long> {

    public static final Creator<Long, LongRankMap> CREATOR   = LongRankMap::new;
    public static final Function<Double, Long>     CONVERTER = Double::longValue;

    public LongRankMap(@NotNull RankMap.Mode mode, @NotNull String permissionPrefix, long defaultValue, @NotNull Map<String, Long> values) {
        super(mode, permissionPrefix, defaultValue, values);
    }

    @NotNull
    public static LongRankMap ranked(long defaultValue) {
        return ranked(CREATOR, defaultValue);
    }

    @NotNull
    public static LongRankMap permissioned(@NotNull String prefix, long defaultValue) {
        return permissioned(CREATOR, prefix, defaultValue);
    }

    @NotNull
    public static ConfigValue<LongRankMap> asConfigValue(@NotNull String path, @NotNull LongRankMap defaultValue, @NotNull String... description) {
        return asConfigValue(path, defaultValue, CREATOR, CONVERTER, description);
    }

    @NotNull
    public static LongRankMap read(@NotNull FileConfig config, @NotNull String path) {
        return read(config, path, CREATOR, CONVERTER);
    }
}
