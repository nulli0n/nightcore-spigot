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
    public static RankMap<Integer> readInt(@NotNull FileConfig cfg, @NotNull String path) {
        return read(cfg, path, Integer.class);
    }

    @NotNull
    public static RankMap<Double> readDouble(@NotNull FileConfig cfg, @NotNull String path) {
        return read(cfg, path, Double.class);
    }

    @NotNull
    public static RankMap<Long> readLong(@NotNull FileConfig cfg, @NotNull String path) {
        return read(cfg, path, Long.class);
    }

    @NotNull
    public static <T extends Number> RankMap<T> read(@NotNull FileConfig cfg, @NotNull String path, @NotNull Class<T> clazz) {
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
            "RANK = Get value by player's permission group. All 'Values' keys will represent permission group names.",
            "PERMISSION = Get value by player's permissions. All 'Values' keys will become as suffix for 'Permission_Prefix' setting."
        ).read(cfg);

        String permissionPrefix = ConfigValue.create(path + ".Permission_Prefix", "sample.prefix.",
            "Sets permission prefix for the 'PERMISSION' mode.",
            "All 'Values' keys will be used to check if player has permission: <permission_prefix> + <key>."
        ).read(cfg);

        T defaultValue;
        if (clazz == Double.class) {
            defaultValue = clazz.cast(ConfigValue.create(path + ".Default_Value", -1).read(cfg));
        }
        else defaultValue = clazz.cast(ConfigValue.create(path + ".Default_Value", -1).read(cfg));

        Map<String, T> values = new HashMap<>();
        for (String rank : cfg.getSection(path + ".Values")) {
            T number;
            if (clazz == Double.class) {
                number = clazz.cast(cfg.getDouble(path + ".Values." + rank));
            }
            else number = clazz.cast(cfg.getInt(path + ".Values." + rank));

            values.put(rank.toLowerCase(), number);
        }

        return new RankMap<>(mode, permissionPrefix, defaultValue, values);
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
