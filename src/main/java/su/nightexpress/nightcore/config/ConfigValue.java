package su.nightexpress.nightcore.config;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.TriFunction;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;
import su.nightexpress.nightcore.util.wrapper.UniParticle;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ConfigValue<T> {

    private final String    path;
    private final T         defaultValue;
    private final String[]  description;
    private final Reader<T> reader;
    private final Writer<T> writer;

    private T value;
    private UnaryOperator<T> onRead;

    public ConfigValue(@NotNull String path,
                       @NotNull Reader<T> reader,
                       @NotNull Writer<T> writer,
                       @NotNull T defaultValue,
                       @Nullable String... description) {
        this.path = path;
        this.description = description == null ? new String[0] : description;
        this.reader = reader;
        this.writer = writer;
        this.defaultValue = defaultValue;
    }

    @NotNull
    private static <T> ConfigValue<T> create(@NotNull String path,
                                            @NotNull Reader<T> reader,
                                            @NotNull Writer<T> writer,
                                            @NotNull T defaultValue,
                                            @Nullable String... description) {
        return new ConfigValue<>(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path,
                                            @NotNull Reader<T> reader,
                                            @NotNull Writer<T> writer,

                                            @NotNull Supplier<T> defaultValue,
                                            @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull Reader<T> reader, @NotNull T defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }

    @NotNull
    public static <T> ConfigValue<T> create(@NotNull String path, @NotNull Reader<T> reader, @NotNull Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader, FileConfig::set, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Boolean> create(@NotNull String path, boolean defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getBoolean, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Integer> create(@NotNull String path, int defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getInt, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<int[]> create(@NotNull String path, int[] defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getIntArray, FileConfig::setIntArray, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Double> create(@NotNull String path, double defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getDouble, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Long> create(@NotNull String path, long defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getLong, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<String> create(@NotNull String path, @NotNull String defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getString, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<String[]> create(@NotNull String path, @NotNull String[] defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getStringArray, FileConfig::setStringArray, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<List<String>> create(@NotNull String path, @NotNull List<String> defaultValue, @Nullable String... description) {
        return create(path, (cfg, path1, def) -> cfg.getStringList(path1), defaultValue, description);
    }

    @NotNull
    public static ConfigValue<Set<String>> create(@NotNull String path, @NotNull Set<String> defaultValue, @Nullable String... description) {
        return create(path, (cfg, path1, def) -> cfg.getStringSet(path1), defaultValue, description);
    }

    @NotNull
    public static ConfigValue<ItemStack> create(@NotNull String path, @NotNull ItemStack defaultValue, @Nullable String... description) {
        return create(path, FileConfig::getItem, FileConfig::setItem, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<UniSound> create(@NotNull String path, @NotNull UniSound defaultValue, @Nullable String... description) {
        Reader<UniSound> reader = (cfg, path1, def) -> UniSound.read(cfg, path1);
        Writer<UniSound> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<UniParticle> create(@NotNull String path, @NotNull UniParticle defaultValue, @Nullable String... description) {
        Reader<UniParticle> reader = (cfg, path1, def) -> UniParticle.read(cfg, path1);
        Writer<UniParticle> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static ConfigValue<UniFormatter> create(@NotNull String path, @NotNull UniFormatter defaultValue, @Nullable String... description) {
        Reader<UniFormatter> reader = (cfg, path1, def) -> UniFormatter.read(cfg, path1);
        Writer<UniFormatter> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>> ConfigValue<E> create(@NotNull String path, @NotNull Class<E> clazz, @NotNull E defaultValue, @Nullable String... description) {
        Reader<E> reader = (cfg, path1, def) -> cfg.getEnum(path1, clazz, def);
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

        Reader<Set<V>> reader = (cfg, path1, def) -> cfg.getStringSet(path1).stream().map(valFun).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                       @NotNull Function<String, K> keyFun,
                                                       @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                       @NotNull Writer<Map<K, V>> writer,
                                                       @NotNull Supplier<Map<K, V>> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <K, V> ConfigValue<Map<K, V>> forMap(@NotNull String path,
                                                       @NotNull Function<String, K> keyFun,
                                                       @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                       @NotNull Writer<Map<K, V>> writer,
                                                       @NotNull Map<K, V> defaultValue,
                                                       @Nullable String... description) {
        return forMap(path, keyFun, valFun, HashMap::new, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                               @NotNull Function<String, K> keyFun,
                                                               @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                               @NotNull Writer<TreeMap<K, V>> writer,
                                                               @NotNull Supplier<TreeMap<K, V>> defaultValue, @Nullable String... description) {
        return forTreeMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <K, V> ConfigValue<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                               @NotNull Function<String, K> keyFun,
                                                               @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                               @NotNull Writer<TreeMap<K, V>> writer,
                                                               @NotNull TreeMap<K, V> defaultValue, @Nullable String... description) {
        return forMap(path, keyFun, valFun, TreeMap::new, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V, M extends Map<K, V>> ConfigValue<M> forMap(@NotNull String path,
                                                                    @NotNull Function<String, K> keyFun,
                                                                    @NotNull TriFunction<FileConfig, String, String, V> valFun,
                                                                    @NotNull Supplier<M> mapSupplier,
                                                                    @NotNull Writer<M> writer,
                                                                    @NotNull M defaultValue, @Nullable String... description) {
        Reader<M> reader = (cfg, path1, def) -> {
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

    @NotNull
    public static <V> ConfigValue<Map<String, V>> forMap(@NotNull String path,
                                                         @NotNull TriFunction<FileConfig, String, String, V> function,
                                                         @NotNull Writer<Map<String, V>> writer,
                                                         @NotNull Supplier<Map<String, V>> defaultValue,
                                                         @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue.get(), description);
    }

    @NotNull
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
    public T read(@NotNull FileConfig config) {
        if (!config.contains(this.getPath())) {
            this.write(config);
        }
        if (this.getDescription().length > 0 && !this.getDescription()[0].isEmpty()) {
            config.setComments(this.getPath(), this.getDescription());
        }

        UnaryOperator<T> operator = this.onRead == null ? value -> value : this.onRead;

        return (this.value = operator.apply(this.reader.read(config, this.getPath(), this.getDefaultValue())));
    }

    public void write(@NotNull FileConfig config) {
        this.getWriter().write(config, this.getPath(), this.get());
    }

    public boolean remove(@NotNull FileConfig config) {
        return config.remove(this.getPath());
    }

    @NotNull
    public T get() {
        return this.value == null ? this.getDefaultValue() : this.value;
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
    public Reader<T> getReader() {
        return reader;
    }

    @NotNull
    public Writer<T> getWriter() {
        return writer;
    }

    public interface Reader<T> {

        @NotNull T read(@NotNull FileConfig config, @NotNull String path, @NotNull T def);
    }

    public interface Writer<T> {

        void write(@NotNull FileConfig config, @NotNull String path, @NotNull T obj);
    }
}
