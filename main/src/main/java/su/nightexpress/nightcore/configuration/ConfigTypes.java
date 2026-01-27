package su.nightexpress.nightcore.configuration;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.MenuType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigTypes {

    public static final ConfigType<Boolean> BOOLEAN = ConfigType.of(FileConfig::getBoolean, FileConfig::set);

    public static final ConfigType<Integer> INT       = ConfigType.of(FileConfig::getInt, FileConfig::set);
    public static final ConfigType<int[]>   INT_ARRAY = ConfigType.of(FileConfig::getIntArray, FileConfig::setArray);

    public static final ConfigType<Double>   DOUBLE       = ConfigType.of(FileConfig::getDouble, FileConfig::set);
    public static final ConfigType<double[]> DOUBLE_ARRAY = ConfigType.of(FileConfig::getDoubleArray, FileConfig::setArray);

    public static final ConfigType<Long>   LONG       = ConfigType.of(FileConfig::getLong, FileConfig::set);
    public static final ConfigType<long[]> LONG_ARRAY = ConfigType.of(FileConfig::getLongArray, FileConfig::setArray);

    public static final ConfigType<String>       STRING          = ConfigType.of(FileConfig::getString, FileConfig::set);
    public static final ConfigType<String>       STRING_OR_EMPTY = ConfigType.of(FileConfig::getStringOrEmpty, FileConfig::set);
    public static final ConfigType<String[]>     STRING_ARRAY    = ConfigType.of(FileConfig::getStringArray, FileConfig::setStringArray);
    public static final ConfigType<List<String>> STRING_LIST     = ConfigType.of(FileConfig::getStringList, FileConfig::set);
    public static final ConfigType<Set<String>>  STRING_SET      = ConfigType.of(FileConfig::getStringSet, FileConfig::set);

    public static final ConfigType<List<String>> STRING_LIST_LOWER_CASE = forList(LowerCase.INTERNAL::apply, key -> key);
    public static final ConfigType<Set<String>>  STRING_SET_LOWER_CASE  = forSet(LowerCase.INTERNAL::apply, key -> key);

    public static final ConfigType<NightItem>  NIGHT_ITEM  = ConfigType.of(FileConfig::getCosmeticItem, FileConfig::set);
    public static final ConfigType<NightSound> NIGHT_SOUND = ConfigType.of(FileConfig::readSound, FileConfig::set);

    public static final ConfigType<MenuType>         MENU_TYPE       = forNamespaced(BukkitThing::getMenuType);
    public static final ConfigType<EntityType>       ENTITY_TYPE     = forNamespaced(BukkitThing::getEntityType);
    public static final ConfigType<Set<EntityType>>  ENTITY_TYPE_SET = forNamespacedSet(BukkitThing::getEntityType);
    public static final ConfigType<PotionEffectType> EFFECT_TYPE     = forNamespaced(BukkitThing::getEffectType);
    public static final ConfigType<Material>         MATERIAL        = forNamespaced(BukkitThing::getMaterial);
    public static final ConfigType<Set<Material>>    MATERIAL_SET    = forNamespacedSet(BukkitThing::getMaterial);
    public static final ConfigType<Enchantment>      ENCHANTMENT     = forNamespaced(BukkitThing::getEnchantment);
    public static final ConfigType<Particle>         PARTICLE        = forNamespaced(BukkitThing::getParticle);
    public static final ConfigType<Sound>            SOUND           = forNamespaced(BukkitThing::getSound);
    public static final ConfigType<Attribute>        ATTRIBUTE       = forNamespaced(BukkitThing::getAttribute);

    /**
     * Creates a {@link ConfigType} for any {@link Enum}.
     */
    @NotNull
    public static <E extends Enum<E>> ConfigType<E> forEnum(@NotNull Class<E> type) {
        ConfigType.Loader<E> reader = (config, path) -> config.getEnum(path, type);
        ConfigType.Writer<E> writer = (config, path, value) -> config.set(path, value.name());

        return ConfigType.of(reader, writer);
    }

    /**
     * Creates a {@link ConfigType} for any {@link org.bukkit.Keyed}.
     */
    @NotNull
    public static <T extends Keyed> ConfigType<T> forNamespaced(@NotNull Function<String, T> fromString) {
        return ConfigType.of(
            (config, path) -> Optional.ofNullable(config.getString(path)).map(fromString).orElse(null),
            (config, path, value) -> config.set(path, BukkitThing.getAsString(value))
        );
    }

    /**
     * Creates a {@link ConfigType} for a Set<V> where each item is stored as a String.
     *
     * @param fromString A function to convert a String from the config into V.
     * @param toString   A function to convert V into a String for saving.
     */
    @NotNull
    public static <V> ConfigType<Set<V>> forSet(@NotNull Function<String, V> fromString, @NotNull Function<V, String> toString) {
        return ConfigType.of(
            (config, path) -> config.getStringSet(path).stream()
                .map(fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new)),

            (config, path, set) -> config.set(path, set.stream().map(toString).toList())
        );
    }

    @NotNull
    public static <V> ConfigType<List<V>> forList(@NotNull Function<String, V> fromString, @NotNull Function<V, String> toString) {
        return ConfigType.of(
            (config, path) -> config.getStringList(path).stream()
                .map(fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new)),

            (config, path, set) -> config.set(path, set.stream().map(toString).toList())
        );
    }

    @NotNull
    public static <V extends Keyed> ConfigType<Set<V>> forNamespacedSet(@NotNull Function<String, V> fromString) {
        return forSet(fromString, BukkitThing::getAsString);
    }

    @NotNull
    public static <V extends Enum<V>> ConfigType<EnumSet<V>> forEnumSet(@NotNull Class<V> type) {
        return ConfigType.of(
            (config, path) -> config.getStringSet(path).stream()
                .map(string -> Enums.get(string, type))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(type))),

            (config, path, set) -> config.set(path, set.stream().map(Enum::name).toList())
        );
    }

    /**
     * Creates a {@link ConfigType} for a Map<String, V> where V is a custom object.
     *
     * @param type The ConfigType for the *value* (V). This tells
     * the map how to read/write each sub-object.
     */
    @NotNull
    public static <V> ConfigType<Map<String, V>> forMap(@NotNull ConfigType<V> type) {
        return forMap(s -> s, s -> s, type);
    }

    @NotNull
    public static <V> ConfigType<Map<String, V>> forMapWithLowerKeys(@NotNull ConfigType<V> type) {
        return forMap(LowerCase.INTERNAL::apply, key -> key, type);
    }

    @NotNull
    public static <K, V> ConfigType<Map<K, V>> forMap(@NotNull Function<String, K> strToKey, @NotNull Function<K, String> keyToStr, @NotNull ConfigType<V> valType) {
        return ConfigType.of(
            (config, path) -> {
                Map<K, V> map = new LinkedHashMap<>();
                config.getSection(path).forEach(key -> {
                    K k = strToKey.apply(key);
                    if (k == null) return;

                    valType.read(config, path + "." + key).ifPresent(value -> map.put(k, value));
                });
                return map;
            },

            (config, path, map) -> {
                config.set(path, null); // Clear old values
                map.forEach((key, value) -> valType.write(config, path + "." + keyToStr.apply(key), value));
            }
        );
    }
}
