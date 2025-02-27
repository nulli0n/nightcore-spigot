package su.nightexpress.nightcore.util.rankmap;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class RankMap<T extends Number> {

    private final Mode           mode;
    private final String         permissionPrefix;
    private final T              defaultValue;
    private final Map<String, T> values;

    public enum Mode {
        RANK, PERMISSION
    }

    public interface Creator<T extends Number, R extends RankMap<T>> {
        @NotNull R create(@NotNull Mode mode, @NotNull String permissionPrefix, @NotNull T defaultValue, @NotNull Map<String, T> values);
    }

    public RankMap(@NotNull Mode mode, @NotNull String permissionPrefix, @NotNull T defaultValue, @NotNull Map<String, T> values) {
        this.mode = mode;
        this.permissionPrefix = permissionPrefix;
        this.defaultValue = defaultValue;
        this.values = new HashMap<>(values);
    }

    @NotNull
    protected static <T extends Number, R extends RankMap<T>> R ranked(@NotNull Creator<T, R> creator, @NotNull T defaultValue) {
        return creator.create(Mode.RANK, "", defaultValue, new HashMap<>());
    }

    @NotNull
    protected static <T extends Number, R extends RankMap<T>> R permissioned(@NotNull Creator<T, R> creator, @NotNull String prefix, @NotNull T defaultValue) {
        return creator.create(Mode.PERMISSION, prefix, defaultValue, new HashMap<>());
    }

    @NotNull
    protected static <T extends Number, R extends RankMap<T>> ConfigValue<R> asConfigValue(@NotNull String path,
                                                                                           @NotNull R defaultValue,
                                                                                           @NotNull Creator<T, R> creator,
                                                                                           @NotNull Function<Double, T> converter,
                                                                                           @NotNull String... description) {
        ConfigValue.Loader<R> reader = (config, path2) -> read(config, path2, creator, converter);
        ConfigValue.Writer<R> writer = (config, path2, obj) -> obj.write(config, path2);

        return ConfigValue.create(path, reader, writer, () -> defaultValue, description);
    }

    @NotNull
    protected static <T extends Number, R extends RankMap<T>> R read(@NotNull FileConfig config,
                                                                  @NotNull String path,
                                                                  @NotNull Creator<T, R> creator,
                                                                  @NotNull Function<Double, T> converter) {
        Mode mode = ConfigValue.create(path + ".Mode", Mode.class, Mode.RANK,
            "Available values: " + StringUtil.inlineEnum(Mode.class, ", "),
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

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Mode", this.mode.name());
        config.set(path + ".Permission_Prefix", this.permissionPrefix);
        config.set(path + ".Default_Value", this.defaultValue);
        this.values.forEach((rank, number) -> {
            config.set(path + ".Values." + rank, number);
        });
    }

    @NotNull
    public RankMap<T> addValue(@NotNull String key, @NotNull T value) {
        this.values.put(key.toLowerCase(), value);
        return this;
    }

    @NotNull
    public T getRankValue(@NotNull Player player) {
        String group = Players.getPermissionGroup(player);
        return this.values.getOrDefault(group, this.defaultValue);
    }

    @NotNull
    public T getGreatestOrNegative(@NotNull Player player) {
        T best = this.getGreatest(player);
        T lowest = this.getSmallest(player);

        return lowest.doubleValue() < 0D ? lowest : best;
    }

    @NotNull
    public T getGreatest(@NotNull Player player) {
        if (this.mode == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .max(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    @NotNull
    public T getSmallest(@NotNull Player player) {
        if (this.mode == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .min(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    @NotNull
    public Mode getMode() {
        return mode;
    }

    @Nullable
    public String getPermissionPrefix() {
        return permissionPrefix;
    }

    @NotNull
    public T getDefaultValue() {
        return defaultValue;
    }
}
