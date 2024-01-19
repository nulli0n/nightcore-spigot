package su.nightexpress.nightcore.util.random;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Pair;

import java.util.*;

public class Rnd {

    public static final MTRandom RANDOM = new MTRandom();

    public static float getChance() {
        return nextFloat() * 100f;
    }

    public static int get(int n) {
        return nextInt(n);
    }

    public static int get(int min, int max) {
        return min + (int) Math.floor(RANDOM.nextDouble() * (max - min + 1));
    }

    public static double getDouble(double max) {
        return getDouble(0, max);
    }

    public static double getDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }

    @NotNull
    public static <E> E get(@NotNull E[] list) {
        return list[get(list.length)];
    }

    public static int get(int[] list) {
        return list[get(list.length)];
    }

    @NotNull
    public static <E> E get(@NotNull List<E> list) {
        if (list.isEmpty()) throw new NoSuchElementException("Empty list provided!");

        return list.get(get(list.size()));
    }

    @NotNull
    public static <E> E get(@NotNull Set<E> list) {
        return get(new ArrayList<>(list));
    }

    @NotNull
    public static <T> T getByWeight(@NotNull Map<T, Double> itemsMap) {
        List<Pair<T, Double>> items = itemsMap.entrySet().stream()
            .filter(entry -> entry.getValue() > 0D)
            .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(Pair::getSecond))
            .toList();
        double totalWeight = items.stream().mapToDouble(Pair::getSecond).sum();

        int index = 0;
        for (double roll = nextDouble() * totalWeight; index < items.size() - 1; ++index) {
            roll -= items.get(index).getSecond();
            if (roll <= 0D) break;
        }
        return items.get(index).getFirst();
    }

    public static boolean chance(int chance) {
        return chance >= 1 && (chance > 99 || nextInt(99) + 1 <= chance);
    }

    public static boolean chance(double chance) {
        return nextDouble() <= chance / 100.0;
    }

    public static int nextInt(int n) {
        return (int) Math.floor(RANDOM.nextDouble() * n);
    }

    public static int nextInt() {
        return RANDOM.nextInt();
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    public static float nextFloat() {
        return RANDOM.nextFloat();
    }

    public static double nextGaussian() {
        return RANDOM.nextGaussian();
    }

    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }
}
