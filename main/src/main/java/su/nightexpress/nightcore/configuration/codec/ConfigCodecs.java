package su.nightexpress.nightcore.configuration.codec;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.MenuType;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.configuration.codec.core.IntArrayCodec;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotPattern;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.Numbers;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class ConfigCodecs {

    private static CodecRegistry registry;

    protected ConfigCodecs() {
    }

    public static void init(CodecRegistry registry) {
        if (ConfigCodecs.registry != null) throw new IllegalStateException("Registry is already initialized!");

        ConfigCodecs.registry = registry;
    }

    public static CodecRegistry registry() {
        if (registry == null) {
            throw new IllegalStateException("ConfigCodecs is not initialized yet! Is NightCore loaded?");
        }
        return registry;
    }

    public static <T> void register(Class<T> type, ConfigCodec<T> codec) {
        registry().register(type, codec);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable ConfigCodec<T> getCodec(T object) {
        return (ConfigCodec<T>) getCodec(object.getClass());
    }

    public static <T> @Nullable ConfigCodec<T> getCodec(Class<T> type) {
        return registry().get(type);
    }

    public static boolean isRegistered(Class<?> type) {
        return registry().isRegistered(type);
    }

    public static final ConfigType<Boolean> BOOLEAN = ConfigType.of(FileConfig::getBoolean);

    public static final ConfigType<Integer> INT       = ConfigType.of(FileConfig::getInt);
    public static final ConfigCodec<int[]>  INT_ARRAY = new IntArrayCodec();

    public static final ConfigType<Double>   DOUBLE       = ConfigType.of(FileConfig::getDouble);
    public static final ConfigType<double[]> DOUBLE_ARRAY = ConfigType.of(FileConfig::getDoubleArray,
        FileConfig::setArray);

    public static final ConfigType<Long>   LONG       = ConfigType.of(FileConfig::getLong);
    public static final ConfigType<long[]> LONG_ARRAY = ConfigType.of(FileConfig::getLongArray, FileConfig::setArray);

    public static final ConfigType<UUID> UUID = ConfigType.of(FileConfig::getUUID, FileConfig::set);

    public static final ConfigType<String>       STRING          = ConfigType.of(FileConfig::getString);
    public static final ConfigType<String>       STRING_OR_EMPTY = ConfigType.of(FileConfig::getStringOrEmpty);
    public static final ConfigType<String[]>     STRING_ARRAY    = ConfigType.of(FileConfig::getStringArray,
        FileConfig::setStringArray);
    public static final ConfigType<List<String>> STRING_LIST     = ConfigType.of(FileConfig::getStringList);
    public static final ConfigType<Set<String>>  STRING_SET      = ConfigType.of(FileConfig::getStringSet);

    public static final ConfigType<List<String>> STRING_LIST_LOWER_CASE = forList(LowerCase.INTERNAL::apply,
        key -> key);
    public static final ConfigType<Set<String>>  STRING_SET_LOWER_CASE  = forSet(LowerCase.INTERNAL::apply, key -> key);

    public static final ConfigType<NightItem>  NIGHT_ITEM  = ConfigType.of(FileConfig::getCosmeticItem);
    public static final ConfigType<NightSound> NIGHT_SOUND = ConfigType.of(FileConfig::readSound);

    public static final ConfigType<RankTable> RANK_TABLE = ConfigType.of(RankTable::read);

    public static final ConfigType<SlotPattern> SLOT_PATTERN = ConfigType.of(SlotPattern::read);

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
    @NonNull
    public static <E extends Enum<E>> ConfigType<E> forEnum(@NonNull Class<E> type) {
        ConfigType.Loader<E> reader = (config, path) -> config.getEnum(path, type);
        ConfigType.Writer<E> writer = (config, path, value) -> config.set(path, value.name());

        return ConfigType.of(reader, writer);
    }

    /**
     * Creates a {@link ConfigType} for any {@link org.bukkit.Keyed}.
     */
    @NonNull
    public static <T extends Keyed> ConfigType<T> forNamespaced(@NonNull Function<String, T> fromString) {
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
    @NonNull
    public static <V> ConfigType<Set<V>> forSet(@NonNull Function<String, V> fromString,
                                                @NonNull Function<V, String> toString) {
        return ConfigType.of(
            (config, path) -> config.getStringSet(path).stream()
                .map(fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new)),

            (config, path, set) -> config.set(path, set.stream().map(toString).toList())
        );
    }

    @NonNull
    public static <V> ConfigType<List<V>> forList(@NonNull Function<String, V> fromString,
                                                  @NonNull Function<V, String> toString) {
        return ConfigType.of(
            (config, path) -> config.getStringList(path).stream()
                .map(fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new)),

            (config, path, set) -> config.set(path, set.stream().map(toString).toList())
        );
    }

    @NonNull
    public static <V extends Keyed> ConfigType<Set<V>> forNamespacedSet(@NonNull Function<String, V> fromString) {
        return forSet(fromString, BukkitThing::getAsString);
    }

    @NonNull
    public static <V extends Enum<V>> ConfigType<EnumSet<V>> forEnumSet(@NonNull Class<V> type) {
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
     *             the map how to read/write each sub-object.
     */
    @NonNull
    public static <V> ConfigType<Map<String, V>> forMap(@NonNull ConfigType<V> type) {
        return forMap(s -> s, s -> s, type);
    }

    @NonNull
    public static <V> ConfigType<Map<String, V>> forMapWithLowerKeys(@NonNull ConfigType<V> type) {
        return forMap(LowerCase.INTERNAL::apply, key -> key, type);
    }

    @NonNull
    public static <K, V> ConfigType<Map<K, V>> forMap(@NonNull Function<String, K> strToKey,
                                                      @NonNull Function<K, String> keyToStr,
                                                      @NonNull ConfigType<V> valType) {
        return forMap(strToKey, keyToStr, valType, LinkedHashMap::new);
    }

    public static <V> @NonNull ConfigType<TreeMap<Integer, V>> forIntTreeMap(@NonNull ConfigCodec<V> valType) {
        return forMap(key -> Numbers.getAnyInteger(key, 0), String::valueOf, valType, TreeMap::new);
    }

    public static <K, V, M extends Map<K, V>> @NonNull ConfigType<M> forMap(@NonNull Function<String, K> strToKey,
                                                                            @NonNull Function<K, String> keyToStr,
                                                                            @NonNull ConfigCodec<V> valType,
                                                                            @NonNull Supplier<M> mapSupplier) {
        return ConfigType.of(
            (config, path) -> {
                M map = mapSupplier.get();
                config.getSection(path).forEach(key -> {
                    K k = strToKey.apply(key);
                    if (k == null) return;

                    valType.readOptional(config, path + "." + key).ifPresent(value -> map.put(k, value));
                });
                return map;
            },

            (config, path, map) -> {
                config.set(path, null); // Clear old values
                map.forEach((key, value) -> valType.write(config, path + "." + keyToStr.apply(key), value));
            }
        );
    }

    public static <V> @NonNull ConfigType<Map<String, V>> forMap(@NonNull ConfigType<V> type,
                                                                 @NonNull Function<V, String> idExtract) {
        return forMap(type, idExtract, key -> key);
    }

    public static <K, V> @NonNull ConfigType<Map<K, V>> forMap(@NonNull ConfigType<V> valType,
                                                               @NonNull Function<V, K> idExtract,
                                                               @NonNull Function<K, String> keyToStr) {
        return ConfigType.of(
            (config, path) -> {
                Map<K, V> map = new LinkedHashMap<>();
                config.getSection(path).forEach(key -> {
                    valType.readOptional(config, path + "." + key).ifPresent(value -> map.put(idExtract.apply(value),
                        value));
                });
                return map;
            },

            (config, path, map) -> {
                config.set(path, null); // Clear old values
                map.forEach((key, value) -> valType.write(config, path + "." + idExtract.apply(value), value));
            }
        );
    }
}
