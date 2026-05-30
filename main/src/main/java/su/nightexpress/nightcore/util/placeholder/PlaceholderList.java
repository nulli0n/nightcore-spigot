package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;
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

    public PlaceholderList(@NonNull PlaceholderList<T> other) {
        this(other.getEntries());
    }

    public PlaceholderList(@NonNull List<PlaceholderEntry<T>> entries) {
        //this.entries = new ArrayList<>(entries);
        this(fromList(entries));
    }

    public PlaceholderList(@NonNull Map<String, PlaceholderEntry<T>> entries) {
        this.entries = new LinkedHashMap<>(entries);
    }

    @NonNull
    public static <T> PlaceholderList<T> create(@NonNull Consumer<PlaceholderList<T>> consumer) {
        PlaceholderList<T> placeholderList = new PlaceholderList<>();
        consumer.accept(placeholderList);
        return placeholderList;
    }

    @NonNull
    private static <T> Map<String, PlaceholderEntry<T>> fromList(@NonNull List<PlaceholderEntry<T>> entries) {
        Map<String, PlaceholderEntry<T>> map = new HashMap<>();

        entries.forEach(entry -> map.put(entry.getKey().toLowerCase(), entry));

        return map;
    }

    @NonNull
    public List<PlaceholderEntry<T>> getEntries() {
        return new ArrayList<>(this.entries.values());
    }

    @NonNull
    public PlaceholderList<T> add(@NonNull PlaceholderList<? super T> other) {
        other.getEntries().forEach(entry -> {
            this.add(entry.getKey(), entry::get);
        });
        //this.entries.addAll(other.getEntries());
        return this;
    }

    @NonNull
    public PlaceholderList<T> add(@NonNull String key, @NonNull String replacer) {
        this.add(key, source -> replacer);
        return this;
    }

    @NonNull
    public PlaceholderList<T> add(@NonNull String key, @NonNull Supplier<String> replacer) {
        this.add(key, source -> replacer.get());
        return this;
    }

    @NonNull
    public PlaceholderList<T> add(@NonNull String key, @NonNull Function<T, String> replacer) {
        this.entries.put(key.toLowerCase(), new PlaceholderEntry<>(key, replacer));
        return this;
    }

    public boolean remove(@NonNull String key) {
        return this.entries.remove(key.toLowerCase()) != null;
    }

    public void clear() {
        this.entries.clear();
    }

    @NonNull
    public UnaryOperator<String> replacer(@NonNull T source) {
        return str -> StringUtil.replaceEach(str, this.getEntries(), source);
    }

    @NonNull
    public String apply(@NonNull String str, @NonNull T source) {
        return this.replacer(source).apply(str);
    }
}
