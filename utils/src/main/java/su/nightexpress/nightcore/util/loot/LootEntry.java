package su.nightexpress.nightcore.util.loot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public record LootEntry<T, C>(@NotNull T item, double weight, @Nullable Predicate<C> predicate) {

    @NotNull
    public static <T, C> LootEntry<T, C> of(@NotNull T item, double weight) {
        return of(item, weight, null);
    }

    @NotNull
    public static <T, C> LootEntry<T, C> of(@NotNull T item, double weight, @Nullable Predicate<C> predicate) {
        return new LootEntry<>(item, weight, predicate);
    }

    public boolean canPick(@NotNull C context) {
        return this.predicate == null || this.predicate.test(context);
    }
}
