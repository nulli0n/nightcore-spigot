package su.nightexpress.nightcore.config;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.TriFunction;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.bukkit.NightSound;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;
import su.nightexpress.nightcore.util.wrapper.UniParticle;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class ConfigValue<T> {

    private final String    path;
    private final T         defaultValue;
    private final String[]  description;
    private final Loader<T> reader;
    private final Writer<T> writer;

    private T value;
    private UnaryOperator<T> onRead;

    public ConfigValue(@NotNull String path,
                       @NotNull ConfigValue.Loader<T> reader,
                       @NotNull Writer<T> writer,
                       @NotNull T defaultValue,
                       @Nullable String... description) {
        this.path = path;
        this.description = description == null ? new String[0] : description;
        this.reader = reader;
        this.writer = writer;
        this.defaultValue = defaultValue;
    }

    @Deprecated
    public ConfigValue(@NotNull String path,
                       @NotNull ConfigValue.Reader<T> reader,
                       @NotNull Writer<T> writer,
                       @NotNull T defaultValue,
                       @Nullable String... description) {
        this(path, reader.update(), writer, defaultValue, description);
    }

    @NotNull
    private static <T> ConfigValue<T> create(@NotNull String path,
                                            @NotNull ConfigValue.Loader<T> reader,
                                            @NotNull Writer<T> writer,
                                            @NotNull T defaultValue,
                                            @Nullable String... description) {
        return new ConfigValue<>(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path,
                                            @NotNull ConfigValue.Loader<T> reader,
                                            @NotNull Writer<T> writer,
                                            @NotNull Supplier<T> defaultValue,
                                            @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    @Deprecated
    private static <T> ConfigValue<T> create(@NotNull String path,
                                             @NotNull ConfigValue.Reader<T> reader,
                                             @NotNull Writer<T> writer,
                                             @NotNull T defaultValue,
                                             @Nullable String... description) {
        return new ConfigValue<>(path, reader.update(), writer, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <T> ConfigValue<T> create(@NotNull String path,
                                            @NotNull ConfigValue.Reader<T> reader,
                                            @NotNull Writer<T> writer,
                                            @NotNull Supplier<T> defaultValue,
                                            @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    @Deprecated
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull ConfigValue.Reader<T> reader, @NotNull T defaultValue, @Nullable String... description) {
        return create(path, reader.update(), FileConfig::set, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull ConfigValue.Reader<T> reader, @NotNull Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader.update(), FileConfig::set, defaultValue, description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull ConfigValue.Loader<T> reader, @NotNull T defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull ConfigValue.Loader<T> reader, @NotNull Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Boolean> create(@NotNull String path, boolean defaultValue, @Nullable String... description) {
        return create(path, (Loader<Boolean>) FileConfig::getBoolean, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Integer> create(@NotNull String path, int defaultValue, @Nullable String... description) {
        return create(path, (Loader<Integer>) FileConfig::getInt, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<int[]> create(@NotNull String path, int[] defaultValue, @Nullable String... description) {
        return create(path, (Loader<int[]>) FileConfig::getIntArray, FileConfig::setIntArray, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Double> create(@NotNull String path, double defaultValue, @Nullable String... description) {
        return create(path, (Loader<Double>) FileConfig::getDouble, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Long> create(@NotNull String path, long defaultValue, @Nullable String... description) {
        return create(path, (Loader<Long>) FileConfig::getLong, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<String> create(@NotNull String path, @NotNull String defaultValue, @Nullable String... description) {
        return create(path, (Loader<String>) FileConfig::getString, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<String[]> create(@NotNull String path, @NotNull String[] defaultValue, @Nullable String... description) {
        return create(path, (Loader<String[]>) FileConfig::getStringArray, FileConfig::setStringArray, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<List<String>> create(@NotNull String path, @NotNull List<String> defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getStringList, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Set<String>> create(@NotNull String path, @NotNull Set<String> defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getStringSet, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static ConfigValue<ItemStack> create(@NotNull String path, @NotNull ItemStack defaultValue, @Nullable String... description) {
        return create(path, (Loader<ItemStack>) FileConfig::getItem, FileConfig::setItem, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<NightItem> create(@NotNull String path, @NotNull NightItem defaultValue, @Nullable String... description) {
        return create(path, (Loader<NightItem>) FileConfig::getCosmeticItem, FileConfig::set, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static ConfigValue<UniSound> create(@NotNull String path, @NotNull UniSound defaultValue, @Nullable String... description) {
        Writer<UniSound> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, UniSound::read, writer, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<NightSound> create(@NotNull String path, @NotNull NightSound defaultValue, @Nullable String... description) {
//        Reader<NightSound> reader = NightSound::read;
//        Writer<NightSound> writer = (cfg, writePath, sound) -> sound.write(cfg, writePath);

        return create(path, FileConfig::getSound, FileConfig::setSound, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<UniParticle> create(@NotNull String path, @NotNull UniParticle defaultValue, @Nullable String... description) {
        Loader<UniParticle> reader = UniParticle::read;
        Writer<UniParticle> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static ConfigValue<UniFormatter> create(@NotNull String path, @NotNull UniFormatter defaultValue, @Nullable String... description) {
//        Reader<UniFormatter> reader = UniFormatter::read;
//        Writer<UniFormatter> writer = (cfg, path1, obj) -> obj.write(cfg, path1);
//
//        return create(path, reader, writer, defaultValue, description);

        return create(path, UniFormatter::read, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>> ConfigValue<E> create(@NotNull String path, @NotNull Class<E> clazz, @NotNull E defaultValue, @Nullable String... description) {
        Loader<E> reader = (cfg, path1) -> cfg.getEnum(path1, clazz);
        Writer<E> writer = (cfg, path1, obj) -> cfg.set(path1, obj.name());

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <V> ConfigValue<Set<V>> forSet(@NotNull String path,
                                                 @NotNull Function<String, V> reader,
                                                 @NotNull Writer<Set<V>> writer,
                                                 @NotNull Supplier<Set<V>> defaultValue,
                                                 @Nullable String... description) {
        return forSet(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <V> ConfigValue<Set<V>> forSet(@NotNull String path,
                                                 @NotNull Function<String, V> valFun,
                                                 @NotNull Writer<Set<V>> writer,
                                                 @NotNull Set<V> defaultValue,
                                                 @Nullable String... description) {

        Loader<Set<V>> reader = (cfg, path1) -> cfg.getStringSet(path1).stream().map(valFun).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <K, V> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                       @NotNull Function<String, K> keyFun,
                                                       @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                       @NotNull Writer<Map<K, V>> writer,
                                                       @NotNull Supplier<Map<K, V>> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    @Deprecated
    public static <K, V> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                       @NotNull Function<String, K> keyFun,
                                                       @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                       @NotNull Writer<Map<K, V>> writer,
                                                       @NotNull Map<K, V> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, HashMap::new, writer, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                               @NotNull Function<String, K> keyFun,
                                                               @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                               @NotNull Writer<TreeMap<K, V>> writer,
                                                               @NotNull Supplier<TreeMap<K, V>> defaultValue, @Nullable String... description) {
        return forTreeMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    @Deprecated
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                               @NotNull Function<String, K> keyFun,
                                                               @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                               @NotNull Writer<TreeMap<K, V>> writer,
                                                               @NotNull TreeMap<K, V> defaultValue, @Nullable String... description) {
        return forMap(path, keyFun, valFun, TreeMap::new, writer, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <K, V, M extends Map<K, V>> ConfigValue<M> forMap(@NotNull String path,
                                                                    @NotNull Function<String, K> keyFun,
                                                                    @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                                    @NotNull Supplier<M> mapSupplier,
                                                                    @NotNull Writer<M> writer,
                                                                    @NotNull M defaultValue, @Nullable String... description) {
        Loader<M> reader = (cfg, path1) -> {
            M map = mapSupplier.get();
            for (String id : cfg.getSection(path1)) {
                K key = keyFun.apply(id);
                V val = valFun.apply(cfg, path1, id);
                if (key == null || val == null) continue;

                map.put(key, val);
            }
            return map;
        };

        forMap("a", s -> s, s -> s, (cfg, path2) -> null, map -> {});
        forMap("a", s -> s, s -> s, (cfg, path2, id) -> null, map -> {});

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                                         @NotNull Function<String, K> keyReadFun,
                                                                         @NotNull Function<K, String> keyWriteFun,
                                                                         @NotNull ReadFunction<V> valReadFun,
                                                                         @NotNull Consumer<Map<K, V>> defaultValue,
                                                                         @Nullable String... description) {
        Loader<Map<K, V>> reader = (config, readPath) -> {
            var map = new HashMap<K, V>();
            for (String keyRaw : config.getSection(readPath)) {
                K key = keyReadFun.apply(keyRaw);
                V val = valReadFun.read(config, readPath + "." + keyRaw, keyRaw);
                if (key == null || val == null) continue;

                map.put(key, val);
            }
            return map;
        };

        Writer<Map<K, V>> writer = (config, writePath, map) -> {
            map.forEach((key, value) -> {
                String keyRaw = keyWriteFun.apply(key);
                config.set(writePath + "." + keyRaw, value);
                //value.write(config, writePath + "." + keyRaw);
            });
        };

        // Linked map used only to preserve the order of default values when writing it in the config.
        Map<K, V> defaultMap = new LinkedHashMap<>();
        defaultValue.accept(defaultMap);

        return create(path, reader, writer, defaultMap, description);
    }

    @NotNull
    public static <K, V extends Writeable> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                                         @NotNull Function<String, K> keyReadFun,
                                                                         @NotNull Function<K, String> keyWriteFun,
                                                                         @NotNull BiFunction<FileConfig, String, V> valReadFun,
                                                                         //@NotNull Supplier<Map<K, V>> mapSupplier,
                                                                         @NotNull Consumer<Map<K, V>> defaultValue,
                                                                         @Nullable String... description) {
        return forMap(path,keyReadFun, keyWriteFun, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue, description);

//        Reader<Map<K, V>> reader = (cfg, readPath, def) -> {
//            var map = new HashMap<K, V>();//mapSupplier.get();
//            for (String keyRaw : cfg.getSection(readPath)) {
//                K key = keyReadFun.apply(keyRaw);
//                V val = valReadFun.apply(cfg, readPath + "." + keyRaw);
//                if (key == null || val == null) continue;
//
//                map.put(key, val);
//            }
//            return map;
//        };
//
//        Writer<Map<K, V>> writer = (cfg, writePath, map) -> {
//            map.forEach((key, value) -> {
//                String keyRaw = keyWriteFun.apply(key);
//                value.write(cfg, writePath + "." + keyRaw);
//            });
//        };
//
//        // Linked map used only to preserve the order of default values when writing it in the config.
//        Map<K, V> defaultMap = new LinkedHashMap<>();
//        defaultValue.accept(defaultMap);
//
//        return create(path, reader, writer, defaultMap, description);
    }

//    @NotNull
//    public static <K, V extends Writeable> ConfigValue<HashMap<K, V>> forHashMap(@NotNull String path,
//                                                                                 @NotNull Function<String, K> keyReadFun,
//                                                                                 @NotNull Function<K, String> keyWriteFun,
//                                                                                 @NotNull BiFunction<FileConfig, String, V> valReadFun,
//                                                                                 @NotNull Consumer<HashMap<K, V>> defaultValue,
//                                                                                 @Nullable String... description) {
//        return forMap(path, keyReadFun, keyWriteFun, valReadFun, HashMap::new, defaultValue, description);
//    }

//    @NotNull
//    public static <V> ConfigValue<Map<String, V>> forMapById(@NotNull String path,
//                                                             @NotNull Loader<V> valReadFun,
//                                                             @NotNull Consumer<Map<String, V>> defaultValue,
//                                                             @Nullable String... description) {
//        //return forMap(path, String::toLowerCase, key -> key, valReadFun, defaultValue, description);
//
//        return forMapById(path, (cfg, path2, id) -> valReadFun.read(cfg, path2 + "." + id), defaultValue, description);
//    }

    @NotNull
    public static <V> ConfigValue<Map<String, V>> forMapById(@NotNull String path,
                                                             @NotNull BiFunction<FileConfig, String, V> valReadFun,
                                                             @NotNull Consumer<Map<String, V>> defaultValue,
                                                             @Nullable String... description) {
        //return forMap(path, String::toLowerCase, key -> key, valReadFun, defaultValue, description);
        return forMapById(path, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue, description);
    }

    @NotNull
    public static <V> ConfigValue<Map<String, V>> forMapById(@NotNull String path,
                                                             @NotNull ReadFunction<V> valReadFun,
                                                             @NotNull Consumer<Map<String, V>> defaultValue,
                                                             @Nullable String... description) {
        return forMap(path, String::toLowerCase, key -> key, valReadFun, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>, V> ConfigValue<Map<E, V>> forMapByEnum(@NotNull String path,
                                                                             @NotNull Class<E> clazz,
                                                                             @NotNull BiFunction<FileConfig, String, V> valReadFun,
                                                                             @NotNull Consumer<Map<E, V>> defaultValue,
                                                                             @Nullable String... description) {
        //return forMap(path, str -> StringUtil.getEnum(str, clazz).orElse(null), Enum::name, valReadFun, defaultValue, description);

        return forMapByEnum(path, clazz, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>, V> ConfigValue<Map<E, V>> forMapByEnum(@NotNull String path,
                                                                             @NotNull Class<E> clazz,
                                                                             @NotNull ReadFunction<V> valReadFun,
                                                                             @NotNull Consumer<Map<E, V>> defaultValue,
                                                                             @Nullable String... description) {
        return forMap(path, str -> StringUtil.getEnum(str, clazz).orElse(null), Enum::name, valReadFun, defaultValue, description);
    }

    @NotNull
    @Deprecated
    public static <V> ConfigValue<Map<String, V>> forMap(@NotNull String path,
                                                         @NotNull TriFunction<FileConfig, String, String, V> function,
                                                         @NotNull Writer<Map<String, V>> writer,
                                                         @NotNull Supplier<Map<String, V>> defaultValue,
                                                         @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue.get(), description);
    }

    @NotNull
    @Deprecated
    public static <V> ConfigValue<Map<String, V>> forMap(@NotNull String path,
                                                         @NotNull TriFunction<FileConfig, String, String, V> function,
                                                         @NotNull Writer<Map<String, V>> writer,
                                                         @NotNull Map<String, V> defaultValue,
                                                         @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue, description);
    }

    @NotNull
    public ConfigValue<T> onRead(@NotNull UnaryOperator<T> onRead) {
        this.onRead = onRead;
        return this;
    }

    @NotNull
    public ConfigValue<T> whenRead(@NotNull Consumer<T> onRead) {
        this.onRead(object -> {
            onRead.accept(object);
            return object;
        });
        return this;
    }

    @NotNull
    public T read(@NotNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.write(config);
        }
        if (this.description.length > 0 && !this.description[0].isEmpty()) {
            config.setComments(this.path, this.description);
        }

        UnaryOperator<T> postRead = this.onRead == null ? value -> value : this.onRead;

        T read = this.reader.read(config, this.path);
        if (read == null) {
            read = this.defaultValue;
        }

        return (this.value = postRead.apply(read));

        //return (this.value = postRead.apply(this.reader.read(config, this.path, this.defaultValue)));
    }

    public void write(@NotNull FileConfig config) {
        this.getWriter().write(config, this.getPath(), this.get());
    }

    public boolean remove(@NotNull FileConfig config) {
        return config.remove(this.getPath());
    }

    @NotNull
    public T get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public void set(@NotNull T value) {
        this.value = value;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public String[] getDescription() {
        return description;
    }

    @NotNull
    public T getDefaultValue() {
        return defaultValue;
    }

    @NotNull
    public ConfigValue.Loader<T> getReader() {
        return reader;
    }

    @NotNull
    public Writer<T> getWriter() {
        return writer;
    }

    @Deprecated
    public interface Reader<T> {

        @NotNull T read(@NotNull FileConfig config, @NotNull String path, T def);

        default Loader<T> update() {
            return (cfg, path) -> read(cfg, path, null);
        }
    }

    public interface Loader<T> {

        @Nullable T read(@NotNull FileConfig config, @NotNull String path);
    }

    public interface Writer<T> {

        void write(@NotNull FileConfig config, @NotNull String path, @NotNull T obj);
    }
}
