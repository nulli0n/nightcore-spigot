package su.nightexpress.nightcore.util.random;

import org.jspecify.annotations.NonNull;

@Deprecated
public class WeightedItem<T> {

    private final T      item;
    private final double weight;

    public WeightedItem(@NonNull T item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    @NonNull
    public static <T> WeightedItem<T> of(@NonNull T item, double weight) {
        return new WeightedItem<>(item, weight);
    }

    @NonNull
    public T getItem() {
        return this.item;
    }

    public double getWeight() {
        return this.weight;
    }
}
