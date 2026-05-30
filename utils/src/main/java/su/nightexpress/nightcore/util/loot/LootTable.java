package su.nightexpress.nightcore.util.loot;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.Randomizer;

import java.util.*;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

public class LootTable<T, C> {

    private final List<LootEntry<T, C>> entries;
    private final RandomGenerator       rnd;

    public LootTable(@NonNull RandomGenerator rnd) {
        this.entries = new ArrayList<>();
        this.rnd = rnd;
    }

    @NonNull
    public static <T, C> LootTable<T, C> create() {
        return new LootTable<>(Randomizer.getSource());
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @NonNull
    public Optional<T> pick(@NonNull C context) {
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

    @NonNull
    public LootTable<T, C> add(@NonNull T item, double weight) {
        this.entries.add(LootEntry.of(item, weight));
        return this;
    }

    @NonNull
    public LootTable<T, C> add(@NonNull T item, double weight, @NonNull Predicate<C> predicate) {
        this.entries.add(LootEntry.of(item, weight, predicate));
        return this;
    }
}
