package su.nightexpress.nightcore.util.loot;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

public record LootEntry<T, C>(@NonNull T item, double weight, @Nullable Predicate<C> predicate) {

    @NonNull
    public static <T, C> LootEntry<T, C> of(@NonNull T item, double weight) {
        return of(item, weight, null);
    }

    @NonNull
    public static <T, C> LootEntry<T, C> of(@NonNull T item, double weight, @Nullable Predicate<C> predicate) {
        return new LootEntry<>(item, weight, predicate);
    }

    public boolean canPick(@NonNull C context) {
        return this.predicate == null || this.predicate.test(context);
    }
}
