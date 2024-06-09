package su.nightexpress.nightcore.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RankMap<T extends Number> {

    private final Mode mode;
    private final String permissionPrefix;
    private final T defaultValue;
    private final Map<String, T> values;

    public enum Mode {
        RANK, PERMISSION
    }

    public RankMap(@NotNull Mode mode, @NotNull String permissionPrefix, @NotNull T defaultValue, @NotNull Map<String, T> values) {
        this.mode = mode;
        this.permissionPrefix = permissionPrefix;
        this.defaultValue = defaultValue;
        this.values = new HashMap<>(values);
    }

    @NotNull
    public static RankMap<Integer> readInt(@NotNull FileConfig cfg, @NotNull String path, int defaultValue) {
        return read(cfg, path, Integer.class, defaultValue);
    }

    @NotNull
    public static RankMap<Double> readDouble(@NotNull FileConfig cfg, @NotNull String path, double defaultValue) {
        return read(cfg, path, Double.class, defaultValue);
    }

    @NotNull
    public static RankMap<Long> readLong(@NotNull FileConfig cfg, @NotNull String path, long defaultValue) {
        return read(cfg, path, Long.class, defaultValue);
    }

    @NotNull
    public static <T extends Number> RankMap<T> read(@NotNull FileConfig cfg, @NotNull String path, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        Map<String, T> oldMap = new HashMap<>();

        if (!cfg.contains(path + ".Mode")) {
            for (String rank : cfg.getSection(path)) {
                T number;
                if (clazz == Double.class) {
                    number = clazz.cast(cfg.getDouble(path + "." + rank));
                }
                else number = clazz.cast(cfg.getInt(path + "." + rank));

                oldMap.put(rank.toLowerCase(), number);
            }
            cfg.remove(path);
        }

        oldMap.forEach((rank, number) -> {
            if (rank.equalsIgnoreCase(Placeholders.DEFAULT)) {
                cfg.set(path + ".Default_Value", number);
            }
            else {
                cfg.set(path + ".Values." + rank, number);
            }
        });

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
        ).read(cfg);

        String permissionPrefix = ConfigValue.create(path + ".Permission_Prefix",
            "example.prefix.",
            "Sets permission prefix for the '" + Mode.PERMISSION.name() + "' mode."
        ).read(cfg);

        T fallback;
        if (clazz == Double.class) {
            fallback = clazz.cast(ConfigValue.create(path + ".Default_Value", defaultValue.doubleValue()).read(cfg));
        }
        else fallback = clazz.cast(ConfigValue.create(path + ".Default_Value", defaultValue.intValue()).read(cfg));

        Map<String, T> values = new HashMap<>();
        for (String rank : cfg.getSection(path + ".Values")) {
            T number;
            if (clazz == Double.class) {
                number = clazz.cast(cfg.getDouble(path + ".Values." + rank));
            }
            else number = clazz.cast(cfg.getInt(path + ".Values." + rank));

            values.put(rank.toLowerCase(), number);
        }

        return new RankMap<>(mode, permissionPrefix, fallback, values);
    }

    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        cfg.set(path + ".Mode", this.getMode().name());
        cfg.set(path + ".Permission_Prefix", this.getPermissionPrefix());
        cfg.set(path + ".Default_Value", this.getDefaultValue());
        this.values.forEach((rank, number) -> {
            cfg.set(path + ".Values." + rank, number);
        });
    }

    @NotNull
    public T getRankValue(@NotNull Player player) {
        String group = Players.getPermissionGroup(player);
        return this.values.getOrDefault(group, this.values.getOrDefault(Placeholders.DEFAULT, this.getDefaultValue()));
    }

    @NotNull
    public T getGreatestOrNegative(@NotNull Player player) {
        T best = this.getGreatest(player);
        T lowest = this.getSmallest(player);

        return lowest.doubleValue() < 0D ? lowest : best;
    }

    @NotNull
    public T getGreatest(@NotNull Player player) {
        if (this.getMode() == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.getPermissionPrefix() + entry.getKey()))
            .map(Map.Entry::getValue)
            .max(Comparator.comparingDouble(Number::doubleValue)).orElse(this.getDefaultValue());
    }

    @NotNull
    public T getSmallest(@NotNull Player player) {
        if (this.getMode() == Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.getPermissionPrefix() + entry.getKey()))
            .map(Map.Entry::getValue)
            .min(Comparator.comparingDouble(Number::doubleValue)).orElse(this.getDefaultValue());
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
