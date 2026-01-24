package su.nightexpress.nightcore.util.random;

import org.jetbrains.annotations.NotNull;

@Deprecated
public class WeightedItem<T> {

    private final T item;
    private final double weight;

    public WeightedItem(@NotNull T item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    @NotNull
    public static <T> WeightedItem<T> of(@NotNull T item, double weight) {
        return new WeightedItem<>(item, weight);
    }

    @NotNull
    public T getItem() {
        return this.item;
    }

    public double getWeight() {
        return this.weight;
    }
}
