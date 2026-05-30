package su.nightexpress.nightcore.util.rankmap;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Players;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Deprecated
public abstract class RankMap<T extends Number> {

    private final Mode           mode;
    private final String         permissionPrefix;
    private final T              defaultValue;
    private final Map<String, T> values;

    public enum Mode {
        RANK,
        PERMISSION
    }

    public interface Creator<T extends Number, R extends RankMap<T>> {

        @NonNull
        R create(@NonNull Mode mode, @NonNull String permissionPrefix, @NonNull T defaultValue,
                 @NonNull Map<String, T> values);
    }

    public RankMap(@NonNull Mode mode, @NonNull String permissionPrefix, @NonNull T defaultValue,
                   @NonNull Map<String, T> values) {
        this.mode = mode;
        this.permissionPrefix = permissionPrefix;
        this.defaultValue = defaultValue;
        this.values = new HashMap<>(values);
    }

    @NonNull
    protected static <T extends Number, R extends RankMap<T>> R ranked(@NonNull Creator<T, R> creator,
                                                                       @NonNull T defaultValue) {
        return creator.create(Mode.RANK, "", defaultValue, new HashMap<>());
    }

    @NonNull
    protected static <T extends Number, R extends RankMap<T>> R permissioned(@NonNull Creator<T, R> creator,
                                                                             @NonNull String prefix,
                                                                             @NonNull T defaultValue) {
        return creator.create(Mode.PERMISSION, prefix, defaultValue, new HashMap<>());
    }

    @NonNull
    protected static <T extends Number, R extends RankMap<T>> ConfigValue<R> asConfigValue(@NonNull String path,
                                                                                           @NonNull R defaultValue,
                                                                                           @NonNull Creator<T, R> creator,
                                                                                           @NonNull Function<Double, T> converter,
                                                                                           @NonNull String... description) {
        ConfigValue.Loader<R> reader = (config, path2) -> read(config, path2, creator, converter);
        ConfigValue.Writer<R> writer = (config, path2, obj) -> obj.write(config, path2);

        return ConfigValue.create(path, reader, writer, () -> defaultValue, description);
    }

    @NonNull
    protected static <T extends Number, R extends RankMap<T>> R read(@NonNull FileConfig config,
                                                                     @NonNull String path,
                                                                     @NonNull Creator<T, R> creator,
                                                                     @NonNull Function<Double, T> converter) {
        Mode mode = ConfigValue.create(path + ".Mode", Mode.class, Mode.RANK,
            "Available values: " + Enums.inline(Mode.class),
            "=".repeat(20) + " " + Mode.RANK.name() + " MODE " + "=".repeat(20),
            "Get value by player's permission group. All keys in 'Values' list will represent permission group names.",
            "If player has none of specified groups, the 'Default_Value' setting will be used then",
            "  Values:",
            "    vip: 1 # -> Player must be in 'vip' permission group.",
            "    gold: 2 # -> Player must be in 'gold' permission group.",
            "    emerald: 3 # -> Player must be in 'emerald' permission group.",
            "",
            "=".repeat(20) + " " + Mode.PERMISSION.name() + " MODE " + "=".repeat(20),
            "Get value by player's permissions. All keys in 'Values' list will represent postfixes for the 'Permission_Prefix' setting (see below).",
            "If player has none of specified permissions, the 'Default_Value' setting will be used then",
            "  Permission_Prefix: 'example.prefix.'",
            "  Values:",
            "    vip: 1 # -> Player must have 'example.prefix.vip' permission.",
            "    gold: 2 # -> Player must have 'example.prefix.gold' permission.",
            "    emerald: 3 # -> Player must have 'example.prefix.emerald' permission."
        ).read(config);

        String permissionPrefix = ConfigValue.create(path + ".Permission_Prefix",
            "example.prefix.",
            "Sets permission prefix for the '" + Mode.PERMISSION.name() + "' mode."
        ).read(config);

        T defaultValue = converter.apply(config.getDouble(path + ".Default_Value"));

        Map<String, T> values = new HashMap<>();
        for (String rank : config.getSection(path + ".Values")) {
            double number = config.getDouble(path + ".Values." + rank);
            values.put(rank.toLowerCase(), converter.apply(number));
        }

        return creator.create(mode, permissionPrefix, defaultValue, values);
    }

    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Mode", this.mode.name());
        config.set(path + ".Permission_Prefix", this.permissionPrefix);
        config.set(path + ".Default_Value", this.defaultValue);
        this.values.forEach((rank, number) -> {
            config.set(path + ".Values." + rank, number);
        });
    }

    @NonNull
    public RankMap<T> addValue(@NonNull String key, @NonNull T value) {
        this.values.put(key.toLowerCase(), value);
        return this;
    }

    @NonNull
    public T getRankValue(@NonNull Player player) {
        String group = Players.getPermissionGroup(player);
        return this.values.getOrDefault(group, this.defaultValue);
    }

    @NonNull
    public T getGreatestOrNegative(@NonNull Player player) {
        T best = this.getGreatest(player);
        T lowest = this.getSmallest(player);

        return lowest.doubleValue() < 0D ? lowest : best;
    }

    @NonNull
    public T getGreatest(@NonNull Player player) {
        if (this.mode == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .max(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    @NonNull
    public T getSmallest(@NonNull Player player) {
        if (this.mode == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .min(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    @NonNull
    public Mode getMode() {
        return mode;
    }

    @Nullable
    public String getPermissionPrefix() {
        return permissionPrefix;
    }

    @NonNull
    public T getDefaultValue() {
        return defaultValue;
    }
}
