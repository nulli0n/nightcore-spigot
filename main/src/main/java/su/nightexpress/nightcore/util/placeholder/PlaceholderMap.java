package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Deprecated
public class PlaceholderMap {

    private final List<Pair<String, Supplier<String>>> keys;

    public PlaceholderMap() {
        this(new ArrayList<>());
    }

    public PlaceholderMap(@NonNull PlaceholderMap other) {
        this(other.getKeys());
    }

    public PlaceholderMap(@NonNull List<Pair<String, Supplier<String>>> keys) {
        this.keys = new ArrayList<>(keys);
    }

    @NonNull
    public static PlaceholderMap fusion(@NonNull PlaceholderMap... others) {
        PlaceholderMap map = new PlaceholderMap();
        for (PlaceholderMap other : others) {
            map.add(other);
        }
        return map;
    }

    @NonNull
    public static PlaceholderMap fusion(@NonNull Placeholder... others) {
        PlaceholderMap map = new PlaceholderMap();
        for (Placeholder other : others) {
            map.add(other.getPlaceholders());
        }
        return map;
    }

    @NonNull
    public List<Pair<String, Supplier<String>>> getKeys() {
        return keys;
    }

    @NonNull
    public PlaceholderMap add(@NonNull PlaceholderMap other) {
        this.keys.addAll(other.getKeys());
        return this;
    }

    @NonNull
    public PlaceholderMap add(@NonNull String key, @NonNull String replacer) {
        this.add(key, () -> replacer);
        return this;
    }

    @NonNull
    public PlaceholderMap add(@NonNull String key, @NonNull Supplier<String> replacer) {
        this.keys.add(Pair.of(key, replacer));
        return this;
    }

    public void clear() {
        this.keys.clear();
    }

    @NonNull
    public UnaryOperator<String> replacer() {
        return str -> StringUtil.replaceEach(str, this.keys);
    }
}
