package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlaceholderList<T> {

    private final Map<String, PlaceholderEntry<T>> entries;

    public PlaceholderList() {
        this(new ArrayList<>());
    }

    public PlaceholderList(@NotNull PlaceholderList<T> other) {
        this(other.getEntries());
    }

    public PlaceholderList(@NotNull List<PlaceholderEntry<T>> entries) {
        //this.entries = new ArrayList<>(entries);
        this(fromList(entries));
    }

    public PlaceholderList(@NotNull Map<String, PlaceholderEntry<T>> entries) {
        this.entries = new LinkedHashMap<>(entries);
    }

    @NotNull
    public static <T> PlaceholderList<T> create(@NotNull Consumer<PlaceholderList<T>> consumer) {
        PlaceholderList<T> placeholderList = new PlaceholderList<>();
        consumer.accept(placeholderList);
        return placeholderList;
    }

    @NotNull
    private static <T> Map<String, PlaceholderEntry<T>> fromList(@NotNull List<PlaceholderEntry<T>> entries) {
        Map<String, PlaceholderEntry<T>> map = new HashMap<>();

        entries.forEach(entry -> map.put(entry.getKey().toLowerCase(), entry));

        return map;
    }

    @NotNull
    public List<PlaceholderEntry<T>> getEntries() {
        return new ArrayList<>(this.entries.values());
    }

    @NotNull
    public PlaceholderList<T> add(@NotNull PlaceholderList<? super T> other) {
        other.getEntries().forEach(entry -> {
            this.add(entry.getKey(), entry::get);
        });
        //this.entries.addAll(other.getEntries());
        return this;
    }

    @NotNull
    public PlaceholderList<T> add(@NotNull String key, @NotNull String replacer) {
        this.add(key, source -> replacer);
        return this;
    }

    @NotNull
    public PlaceholderList<T> add(@NotNull String key, @NotNull Supplier<String> replacer) {
        this.add(key, source -> replacer.get());
        return this;
    }

    @NotNull
    public PlaceholderList<T> add(@NotNull String key, @NotNull Function<T, String> replacer) {
        this.entries.put(key.toLowerCase(), new PlaceholderEntry<>(key, replacer));
        return this;
    }

    public boolean remove(@NotNull String key) {
        return this.entries.remove(key.toLowerCase()) != null;
    }

    public void clear() {
        this.entries.clear();
    }

    @NotNull
    public UnaryOperator<String> replacer(@NotNull T source) {
        return str -> StringUtil.replaceEach(str, this.getEntries(), source);
    }

    @NotNull
    public String apply(@NotNull String str, @NotNull T source) {
        return this.replacer(source).apply(str);
    }
}
