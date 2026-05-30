package su.nightexpress.nightcore.config;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
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

@NullMarked
@Deprecated(forRemoval = true)
public class ConfigValue<T> {

    private final String    path;
    private final T         defaultValue;
    private final String[]  description;
    private final Loader<T> reader;
    private final Writer<T> writer;

    @Nullable
    private T                value;
    @Nullable
    private UnaryOperator<T> onRead;

    public ConfigValue(String path,
                       ConfigValue.Loader<T> reader,
                       Writer<T> writer,
                       T defaultValue,
                       @Nullable String @Nullable... description) {
        this.path = path;
        this.description = description == null ? new String[0] : description;
        this.reader = reader;
        this.writer = writer;
        this.defaultValue = defaultValue;
    }

    @Deprecated
    public ConfigValue(String path,
                       ConfigValue.Reader<T> reader,
                       Writer<T> writer,
                       T defaultValue,
                       @Nullable String... description) {
        this(path, reader.update(), writer, defaultValue, description);
    }


    private static <T> ConfigValue<T> create(String path,
                                             ConfigValue.Loader<T> reader,
                                             Writer<T> writer,
                                             T defaultValue,
                                             @Nullable String... description) {
        return new ConfigValue<>(path, reader, writer, defaultValue, description);
    }


    public static <T> ConfigValue<T> create(String path,
                                            ConfigValue.Loader<T> reader,
                                            Writer<T> writer,
                                            Supplier<T> defaultValue,
                                            @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }


    @Deprecated
    private static <T> ConfigValue<T> create(String path,
                                             ConfigValue.Reader<T> reader,
                                             Writer<T> writer,
                                             T defaultValue,
                                             @Nullable String... description) {
        return new ConfigValue<>(path, reader.update(), writer, defaultValue, description);
    }


    @Deprecated
    public static <T> ConfigValue<T> create(String path,
                                            ConfigValue.Reader<T> reader,
                                            Writer<T> writer,
                                            Supplier<T> defaultValue,
                                            @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }


    @Deprecated
    public static <T> ConfigValue<T> create(String path, ConfigValue.Reader<T> reader,
                                            T defaultValue, @Nullable String... description) {
        return create(path, reader.update(), FileConfig::set, defaultValue, description);
    }


    @Deprecated
    public static <T> ConfigValue<T> create(String path, ConfigValue.Reader<T> reader,
                                            Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader.update(), FileConfig::set, defaultValue, description);
    }


    public static <T> ConfigValue<T> create(String path, ConfigValue.Loader<T> reader,
                                            T defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }


    public static <T> ConfigValue<T> create(String path, ConfigValue.Loader<T> reader,
                                            Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }


    public static ConfigValue<Boolean> create(String path, boolean defaultValue,
                                              @Nullable String... description) {
        return create(path, (Loader<Boolean>) FileConfig::getBoolean, defaultValue, description);
    }


    public static ConfigValue<Integer> create(String path, int defaultValue, @Nullable String... description) {
        return create(path, (Loader<Integer>) FileConfig::getInt, defaultValue, description);
    }


    public static ConfigValue<int[]> create(String path, int[] defaultValue, @Nullable String... description) {
        return create(path, (Loader<int[]>) FileConfig::getIntArray, FileConfig::setIntArray, defaultValue,
            description);
    }


    public static ConfigValue<Double> create(String path, double defaultValue,
                                             @Nullable String... description) {
        return create(path, (Loader<Double>) FileConfig::getDouble, defaultValue, description);
    }


    public static ConfigValue<Long> create(String path, long defaultValue, @Nullable String... description) {
        return create(path, (Loader<Long>) FileConfig::getLong, defaultValue, description);
    }


    public static ConfigValue<String> create(String path, String defaultValue,
                                             @Nullable String... description) {
        return create(path, (Loader<String>) FileConfig::getString, defaultValue, description);
    }


    public static ConfigValue<String[]> create(String path, String[] defaultValue,
                                               @Nullable String... description) {
        return create(path, (Loader<String[]>) FileConfig::getStringArray, FileConfig::setStringArray, defaultValue,
            description);
    }


    public static ConfigValue<List<String>> create(String path, List<String> defaultValue,
                                                   @Nullable String... description) {
        return create(path, FileConfig::getStringList, defaultValue, description);
    }


    public static ConfigValue<Set<String>> create(String path, Set<String> defaultValue,
                                                  @Nullable String... description) {
        return create(path, FileConfig::getStringSet, defaultValue, description);
    }


    @Deprecated
    public static ConfigValue<ItemStack> create(String path, ItemStack defaultValue,
                                                @Nullable String... description) {
        return create(path, (Loader<ItemStack>) FileConfig::getItem, FileConfig::setItem, defaultValue, description);
    }


    public static ConfigValue<NightItem> create(String path, NightItem defaultValue,
                                                @Nullable String... description) {
        return create(path, (Loader<NightItem>) FileConfig::getCosmeticItem, FileConfig::set, defaultValue,
            description);
    }


    @Deprecated
    public static ConfigValue<UniSound> create(String path, UniSound defaultValue,
                                               @Nullable String... description) {
        Writer<UniSound> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, UniSound::read, writer, defaultValue, description);
    }


    public static ConfigValue<NightSound> create(String path, NightSound defaultValue,
                                                 @Nullable String... description) {
        //        Reader<NightSound> reader = NightSound::read;
        //        Writer<NightSound> writer = (cfg, writePath, sound) -> sound.write(cfg, writePath);

        return create(path, FileConfig::getSound, FileConfig::set, defaultValue, description);
    }


    public static ConfigValue<su.nightexpress.nightcore.bridge.wrap.NightSound> create(String path,
                                                                                       su.nightexpress.nightcore.bridge.wrap.NightSound defaultValue,
                                                                                       @Nullable String... description) {
        return create(path, (config, path2) -> config.readSound(path2), FileConfig::set, defaultValue, description);
    }


    public static ConfigValue<UniParticle> create(String path, UniParticle defaultValue,
                                                  @Nullable String... description) {
        Loader<UniParticle> reader = UniParticle::read;
        Writer<UniParticle> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }


    @Deprecated
    public static ConfigValue<UniFormatter> create(String path, UniFormatter defaultValue,
                                                   @Nullable String... description) {
        //        Reader<UniFormatter> reader = UniFormatter::read;
        //        Writer<UniFormatter> writer = (cfg, path1, obj) -> obj.write(cfg, path1);
        //
        //        return create(path, reader, writer, defaultValue, description);

        return create(path, UniFormatter::read, defaultValue, description);
    }


    public static <E extends Enum<E>> ConfigValue<E> create(String path, Class<E> clazz,
                                                            E defaultValue, @Nullable String... description) {
        Loader<E> reader = (cfg, path1) -> cfg.getEnum(path1, clazz);
        Writer<E> writer = (cfg, path1, obj) -> cfg.set(path1, obj.name());

        return create(path, reader, writer, defaultValue, description);
    }


    public static <V> ConfigValue<Set<V>> forSet(String path,
                                                 Function<String, V> reader,
                                                 Writer<Set<V>> writer,
                                                 Supplier<Set<V>> defaultValue,
                                                 @Nullable String... description) {
        return forSet(path, reader, writer, defaultValue.get(), description);
    }


    public static <V> ConfigValue<Set<V>> forSet(String path,
                                                 Function<String, V> valFun,
                                                 Writer<Set<V>> writer,
                                                 Set<V> defaultValue,
                                                 @Nullable String... description) {

        Loader<Set<V>> reader = (cfg, path1) -> cfg.getStringSet(path1).stream().map(valFun).filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));

        return create(path, reader, writer, defaultValue, description);
    }


    @Deprecated
    public static <K, V> ConfigValue<Map<K, V>> forMap(String path,
                                                       Function<String, K> keyFun,
                                                       TriFunction<FileConfig, String, String, V> valFun,
                                                       Writer<Map<K, V>> writer,
                                                       Supplier<Map<K, V>> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }


    @Deprecated
    public static <K, V> ConfigValue<Map<K, V>> forMap(String path,
                                                       Function<String, K> keyFun,
                                                       TriFunction<FileConfig, String, String, V> valFun,
                                                       Writer<Map<K, V>> writer,
                                                       Map<K, V> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, LinkedHashMap::new, writer, defaultValue, description);
    }


    @Deprecated
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(String path,
                                                               Function<String, K> keyFun,
                                                               TriFunction<FileConfig, String, String, V> valFun,
                                                               Writer<TreeMap<K, V>> writer,
                                                               Supplier<TreeMap<K, V>> defaultValue,
                                                               @Nullable String... description) {
        return forTreeMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }


    @Deprecated
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(String path,
                                                               Function<String, K> keyFun,
                                                               TriFunction<FileConfig, String, String, V> valFun,
                                                               Writer<TreeMap<K, V>> writer,
                                                               TreeMap<K, V> defaultValue,
                                                               @Nullable String... description) {
        return forMap(path, keyFun, valFun, TreeMap::new, writer, defaultValue, description);
    }


    @Deprecated
    public static <K, V, M extends Map<K, V>> ConfigValue<M> forMap(String path,
                                                                    Function<String, K> keyFun,
                                                                    TriFunction<FileConfig, String, String, V> valFun,
                                                                    Supplier<M> mapSupplier,
                                                                    Writer<M> writer,
                                                                    M defaultValue,
                                                                    @Nullable String... description) {
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

        return create(path, reader, writer, defaultValue, description);
    }


    public static <K, V> ConfigValue<Map<K, V>> forMap(String path,
                                                       Function<String, K> keyReadFun,
                                                       Function<K, String> keyWriteFun,
                                                       ReadFunction<V> valReadFun,
                                                       Consumer<Map<K, V>> defaultValue,
                                                       @Nullable String... description) {
        Loader<Map<K, V>> reader = (config, readPath) -> {
            var map = new LinkedHashMap<K, V>(); // Preserve config order
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


    public static <K, V extends Writeable> ConfigValue<Map<K, V>> forMap(String path,
                                                                         Function<String, K> keyReadFun,
                                                                         Function<K, String> keyWriteFun,
                                                                         BiFunction<FileConfig, String, V> valReadFun,
                                                                         Consumer<Map<K, V>> defaultValue,
                                                                         @Nullable String... description) {
        return forMap(path, keyReadFun, keyWriteFun, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue,
            description);
    }


    public static <V> ConfigValue<Map<String, V>> forMapById(String path,
                                                             BiFunction<FileConfig, String, V> valReadFun,
                                                             Consumer<Map<String, V>> defaultValue,
                                                             @Nullable String... description) {
        return forMapById(path, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue, description);
    }


    public static <V> ConfigValue<Map<String, V>> forMapById(String path,
                                                             ReadFunction<V> valReadFun,
                                                             Consumer<Map<String, V>> defaultValue,
                                                             @Nullable String... description) {
        return forMap(path, String::toLowerCase, key -> key, valReadFun, defaultValue, description);
    }


    public static <E extends Enum<E>, V> ConfigValue<Map<E, V>> forMapByEnum(String path,
                                                                             Class<E> clazz,
                                                                             BiFunction<FileConfig, String, V> valReadFun,
                                                                             Consumer<Map<E, V>> defaultValue,
                                                                             @Nullable String... description) {
        return forMapByEnum(path, clazz, (cfg, path2, id) -> valReadFun.apply(cfg, path2), defaultValue, description);
    }


    public static <E extends Enum<E>, V> ConfigValue<Map<E, V>> forMapByEnum(String path,
                                                                             Class<E> clazz,
                                                                             ReadFunction<V> valReadFun,
                                                                             Consumer<Map<E, V>> defaultValue,
                                                                             @Nullable String... description) {
        return forMap(path, str -> StringUtil.getEnum(str, clazz).orElse(null), Enum::name, valReadFun, defaultValue,
            description);
    }


    @Deprecated
    public static <V> ConfigValue<Map<String, V>> forMap(String path,
                                                         TriFunction<FileConfig, String, String, V> function,
                                                         Writer<Map<String, V>> writer,
                                                         Supplier<Map<String, V>> defaultValue,
                                                         @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue.get(), description);
    }


    @Deprecated
    public static <V> ConfigValue<Map<String, V>> forMap(String path,
                                                         TriFunction<FileConfig, String, String, V> function,
                                                         Writer<Map<String, V>> writer,
                                                         Map<String, V> defaultValue,
                                                         @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue, description);
    }


    public ConfigValue<T> onRead(UnaryOperator<T> onRead) {
        this.onRead = onRead;
        return this;
    }


    public ConfigValue<T> whenRead(Consumer<T> onRead) {
        this.onRead(object -> {
            onRead.accept(object);
            return object;
        });
        return this;
    }


    public T read(FileConfig config) {
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

    public void write(FileConfig config) {
        this.getWriter().write(config, this.getPath(), this.get());
    }

    public boolean remove(FileConfig config) {
        return config.remove(this.getPath());
    }


    public T get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public void set(T value) {
        this.value = value;
    }


    public String getPath() {
        return path;
    }


    public String[] getDescription() {
        return description;
    }


    public T getDefaultValue() {
        return defaultValue;
    }


    public ConfigValue.Loader<T> getReader() {
        return reader;
    }


    public Writer<T> getWriter() {
        return writer;
    }

    @Deprecated
    public interface Reader<T> {


        T read(FileConfig config, String path, T def);

        default Loader<T> update() {
            return (cfg, path) -> read(cfg, path, null);
        }
    }

    public interface Loader<T> {

        @Nullable
        T read(FileConfig config, String path);
    }

    public interface Writer<T> {

        void write(FileConfig config, String path, T obj);
    }
}
