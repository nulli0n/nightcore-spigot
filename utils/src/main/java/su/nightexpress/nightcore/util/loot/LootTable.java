package su.nightexpress.nightcore.util.loot;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Randomizer;

import java.util.*;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

public class LootTable<T, C> {

    private final List<LootEntry<T, C>> entries;
    private final RandomGenerator       rnd;

    public LootTable(@NotNull RandomGenerator rnd) {
        this.entries = new ArrayList<>();
        this.rnd = rnd;
    }

    @NotNull
    public static <T, C> LootTable<T, C> create() {
        return new LootTable<>(Randomizer.getSource());
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @NotNull
    public Optional<T> pick(@NotNull C context) {
        if (this.isEmpty()) return Optional.empty();

        double totalWeight = 0D;

        List<LootEntry<T, C>> allowed = new ArrayList<>(this.entries.size());

        for (LootEntry<T, C> entry : this.entries) {
            if (entry.canPick(context)) {
                totalWeight += entry.weight();
                allowed.add(entry);
            }
        }

        double randomValue = this.rnd.nextDouble(totalWeight);

        for (LootEntry<T, C> entry : allowed) {
            randomValue -= entry.weight();
            if (randomValue <= 0D) {
                return Optional.of(entry.item());
            }
        }

        return Optional.empty();
    }

    @NotNull
    public LootTable<T, C> add(@NotNull T item, double weight) {
        this.entries.add(LootEntry.of(item, weight));
        return this;
    }

    @NotNull
    public LootTable<T, C> add(@NotNull T item, double weight, @NotNull Predicate<C> predicate) {
        this.entries.add(LootEntry.of(item, weight, predicate));
        return this;
    }
}
