package su.nightexpress.nightcore.util;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RankTable implements Writeable {

    private final Mode                mode;
    private final String              permissionPrefix;
    private final double              defaultValue;
    private final Map<String, Double> values;

    public enum Mode {
        RANK,
        PERMISSION
    }

    RankTable(@NonNull Mode mode, @NonNull String permissionPrefix, double defaultValue,
              @NonNull Map<String, Double> values) {
        this.mode = mode;
        this.permissionPrefix = permissionPrefix;
        this.defaultValue = defaultValue;
        this.values = new HashMap<>(values);
    }

    @NonNull
    public static Builder ranked(double defaultValue) {
        return builder(Mode.RANK, defaultValue);
    }

    @NonNull
    public static Builder permissioned(double defaultValue) {
        return builder(Mode.PERMISSION, defaultValue);
    }

    @NonNull
    public static Builder builder(@NonNull Mode mode, double defaultValue) {
        return new Builder(mode, defaultValue);
    }

    @NonNull
    public static RankTable read(@NonNull FileConfig config, @NonNull String path) {
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

        double defaultValue = ConfigValue.create(path + ".Default_Value", 0D).read(config);

        Map<String, Double> values = new HashMap<>();
        for (String rank : config.getSection(path + ".Values")) {
            double number = config.getDouble(path + ".Values." + rank);
            values.put(rank.toLowerCase(), number);
        }

        return new RankTable(mode, permissionPrefix, defaultValue, values);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Mode", this.mode.name());
        config.set(path + ".Permission_Prefix", this.permissionPrefix);
        config.set(path + ".Default_Value", this.defaultValue);
        config.remove(path + ".Values");
        this.values.forEach((rank, number) -> {
            config.set(path + ".Values." + rank, number);
        });
    }

    @NonNull
    public Double getRankValue(@NonNull Player player) {
        String group = Players.getPrimaryGroupOrDefault(player);
        return this.values.getOrDefault(group, this.defaultValue);
    }

    @NonNull
    public Double getGreatestOrNegative(@NonNull Player player) {
        Double best = this.getGreatest(player);
        Double lowest = this.getSmallest(player);

        return lowest < 0D ? lowest : best;
    }

    @NonNull
    public Double getGreatest(@NonNull Player player) {
        if (this.mode == RankTable.Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .max(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    @NonNull
    public Double getSmallest(@NonNull Player player) {
        if (this.mode == RankTable.Mode.RANK) {
            return this.getRankValue(player);
        }
        return this.values.entrySet().stream()
            .filter(entry -> player.hasPermission(this.permissionPrefix + entry.getKey()))
            .map(Map.Entry::getValue)
            .min(Comparator.comparingDouble(Number::doubleValue)).orElse(this.defaultValue);
    }

    public RankTable.@NonNull Mode getMode() {
        return this.mode;
    }

    @Nullable
    public String getPermissionPrefix() {
        return this.permissionPrefix;
    }

    @NonNull
    public Double getDefaultValue() {
        return this.defaultValue;
    }

    public static class Builder {

        private final Mode                mode;
        private final double              defaultValue;
        private final Map<String, Double> values;

        private String permissionPrefix;

        public Builder(@NonNull Mode mode, double defaultValue) {
            this.mode = mode;
            this.defaultValue = defaultValue;
            this.values = new HashMap<>();
        }

        @NonNull
        public RankTable build() {
            return new RankTable(this.mode, this.permissionPrefix, this.defaultValue, this.values);
        }

        @NonNull
        public Builder permissionPrefix(@NonNull String prefix) {
            this.permissionPrefix = prefix;
            return this;
        }

        @NonNull
        public Builder addRankValue(@NonNull String rank, double value) {
            this.values.put(rank.toLowerCase(), value);
            return this;
        }
    }
}
