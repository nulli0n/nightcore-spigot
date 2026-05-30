package su.nightexpress.nightcore.db.data;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.db.state.Stateful;

public interface DataCache<V extends Stateful> {

    boolean contains(@NonNull V data);

    void put(@NonNull V data);

    void remove(@NonNull V data);

    void removeIf(@NonNull Predicate<V> predicate);

    void clear();

    void clearRemoved();

    void clearExpired();

    @NonNull
    Set<V> getAll();

    @NonNull
    Stream<V> streamAll();
}
