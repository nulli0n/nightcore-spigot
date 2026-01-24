package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class Randomizer {

    private static final RandomGeneratorFactory<RandomGenerator> FACTORY = getFactory();

    private static final ThreadLocal<RandomGenerator> GENERATOR = ThreadLocal.withInitial(FACTORY::create);

    private static RandomGeneratorFactory<RandomGenerator> getFactory() {
        try {
            return RandomGeneratorFactory.of("Xoshiro256PlusPlus");
        }
        catch (IllegalArgumentException exception) {
            return RandomGeneratorFactory.of("Random");
        }
    }

    private Randomizer() {}

    @NotNull
    public static RandomGenerator getSource() {
        return GENERATOR.get();
    }


    public static boolean nextBoolean() {
        return GENERATOR.get().nextBoolean();
    }


    public static int nextInt() {
        return GENERATOR.get().nextInt();
    }

    public static int nextInt(int bound) {
        return GENERATOR.get().nextInt(bound);
    }

    public static int nextInt(int origin, int bound) {
        return GENERATOR.get().nextInt(origin, bound);
    }



    public static long nextLong() {
        return GENERATOR.get().nextLong();
    }

    public static long nextLong(long bound) {
        return GENERATOR.get().nextLong(bound);
    }

    public static long nextLong(long origin, long bound) {
        return GENERATOR.get().nextLong(origin, bound);
    }



    public static double nextDouble() {
        return GENERATOR.get().nextDouble();
    }

    public static double nextDouble(double bound) {
        return GENERATOR.get().nextDouble(bound);
    }

    public static double nextDouble(double origin, double bound) {
        return GENERATOR.get().nextDouble(origin, bound);
    }



    public static float nextFloat() {
        return GENERATOR.get().nextFloat();
    }

    public static float nextFloat(float bound) {
        return GENERATOR.get().nextFloat(bound);
    }

    public static float nextFloat(float origin, float bound) {
        return GENERATOR.get().nextFloat(origin, bound);
    }



    public static boolean checkChance(double chance) {
        return checkProbability(chance / 100D);
    }

    public static boolean checkProbability(double probability) {
        return nextDouble() < probability;
    }

    public static double generateChance() {
        return generateProbability() * 100D;
    }

    public static double generateProbability() {
        return nextDouble();
    }



    @NotNull
    public static <E> E pick(E[] source) {
        if (source.length == 0) throw new IllegalStateException("Array must not be empty");

        return source[nextInt(source.length)];
    }

    public static int pick(int[] source) {
        if (source.length == 0) throw new IllegalStateException("Array must not be empty");

        return source[nextInt(source.length)];
    }

    @NotNull
    public static <E> E pick(@NotNull List<E> list) {
        if (list.isEmpty()) throw new IllegalStateException("List must not be empty");

        return list.get(nextInt(list.size()));
    }

    @NotNull
    public static <E> E pick(@NotNull Set<E> set) {
        if (set.isEmpty()) throw new IllegalStateException("Set must not be empty");

        return set.stream().skip(nextInt(set.size())).findFirst().orElseThrow();
    }
}
