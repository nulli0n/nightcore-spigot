package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlaceholderList<T> {

    private final List<PlaceholderEntry<T>> entries;

    public PlaceholderList() {
        this(new ArrayList<>());
    }

    public PlaceholderList(@NotNull PlaceholderList<T> other) {
        this(other.getEntries());
    }

    public PlaceholderList(@NotNull List<PlaceholderEntry<T>> entries) {
        this.entries = new ArrayList<>(entries);
    }

    @NotNull
    public List<PlaceholderEntry<T>> getEntries() {
        return this.entries;
    }

    @NotNull
    public PlaceholderList<T> add(@NotNull PlaceholderList<T> other) {
        this.entries.addAll(other.getEntries());
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
        this.entries.add(new PlaceholderEntry<>(key, replacer));
        return this;
    }

    public void clear() {
        this.entries.clear();
    }

    @NotNull
    public UnaryOperator<String> replacer(@NotNull T source) {
        return str -> StringUtil.replaceEach(str, this.entries, source);
    }

    @NotNull
    public String apply(@NotNull String str, @NotNull T source) {
        return this.replacer(source).apply(str);
    }
}
