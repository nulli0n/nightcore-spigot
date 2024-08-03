package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lists {

    @NotNull
    public static List<String> worldNames() {
        return Bukkit.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
    }

    public static int indexOf(Object[] array, @NotNull Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, @NotNull Object objectToFind, int startIndex) {
        if (array == null || array.length == 0) return -1;
        if (!array.getClass().getComponentType().isInstance(objectToFind)) return -1;

        if (startIndex < 0) startIndex = 0;

        int index;
        for (index = startIndex; index < array.length; ++index) {
            if (objectToFind.equals(array[index])) {
                return index;
            }
        }

        return -1;
    }

    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    public static int indexOf(int[] array, int valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(int[] array, int valueToFind, int startIndex) {
        if (array == null || array.length == 0) return -1;

        if (startIndex < 0) startIndex = 0;

        for (int index = startIndex; index < array.length; ++index) {
            if (valueToFind == array[index]) {
                return index;
            }
        }

        return -1;
    }

    public static boolean contains(int[] array, int valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    @NotNull
    public static <T, R> Set<R> modify(@NotNull Set<T> set, @NotNull Function<T, R> function) {
        return set.stream().map(function).collect(Collectors.toCollection(HashSet::new));
    }

    @NotNull
    public static <T, R> List<R> modify(@NotNull List<T> set, @NotNull Function<T, R> function) {
        return set.stream().map(function).collect(Collectors.toCollection(ArrayList::new));
    }

    @NotNull
    public static <T> List<List<T>> split(@NotNull List<T> list, int targetSize) {
        List<List<T>> lists = new ArrayList<>();
        if (targetSize <= 0) return lists;

        for (int index = 0; index < list.size(); index += targetSize) {
            lists.add(list.subList(index, Math.min(index + targetSize, list.size())));
        }
        return lists;
    }

    @NotNull
    public static List<String> replace(@NotNull List<String> origin, @NotNull String var, String... with) {
        return replace(origin, var, Arrays.asList(with));
    }

    @NotNull
    public static List<String> replace(@NotNull List<String> origin, @NotNull String var, @NotNull List<String> with) {
        List<String> replaced = new ArrayList<>();
        for (String line : origin) {
            if (line.equalsIgnoreCase(var)) {
                replaced.addAll(with);
            }
            else replaced.add(line);
        }
        return replaced;
    }

    /**
     * @param original List to remove empty lines from.
     * @return A list with no multiple empty lines in a row.
     */
    @NotNull
    public static List<String> stripEmpty(@NotNull List<String> original) {
        List<String> stripped = new ArrayList<>();

        for (int index = 0; index < original.size(); index++) {
            String line = original.get(index);
            if (line.isEmpty()) {
                String last = stripped.isEmpty() ? null : stripped.get(stripped.size() - 1);
                if (last == null || last.isEmpty() || index == (original.size() - 1)) continue;
            }
            stripped.add(line);
        }

        return stripped;
    }

    @NotNull
    public static List<String> getSequentialMatches(@NotNull List<String> results, @NotNull String input) {
        if (input.isBlank()) return results;

        char[] chars = input.toCharArray();
        List<String> goods = new ArrayList<>();

        Result:
        for (String item : results) {
            int itemLength = item.length();
            if (input.length() > itemLength) continue;

            int lastIndex = -1;
            for (char letter : chars) {
                int nextIndex = lastIndex;

                if (nextIndex < 0) {
                    nextIndex = 0;
                }
                else if (nextIndex < itemLength - 1) {
                    nextIndex++; // This fixes an issue, where method fails for similar characters in a row, like 'oo'.
                }

                int index = item.indexOf(letter, nextIndex);
                if (index <= lastIndex) {
                    continue Result;
                }

                lastIndex = index;
            }
            goods.add(item);
        }
        return goods;
    }

    @NotNull
    public static <K, V extends Comparable<? super V>> Map<K, V> sortAscent(@NotNull Map<K, V> map) {
        return sort(map, Map.Entry.comparingByValue());
    }

    @NotNull
    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescent(@NotNull Map<K, V> map) {
        return sort(map, Collections.reverseOrder(Map.Entry.comparingByValue()));
    }

    @SafeVarargs
    @NotNull
    public static <T> List<T> newList(T... values) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    @SafeVarargs
    @NotNull
    public static <T> Set<T> newSet(T... values) {
        Set<T> list = new HashSet<>();
        Collections.addAll(list, values);
        return list;
    }

    @NotNull
    public static <K, V extends Comparable<? super V>> Map<K, V> sort(@NotNull Map<K, V> map, @NotNull Comparator<Map.Entry<K, V>> comparator) {
        return new LinkedList<>(map.entrySet()).stream().sorted(comparator)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, nev) -> nev, LinkedHashMap::new));
    }

    @NotNull
    public static List<String> getEnums(@NotNull Class<? extends Enum<?>> clazz) {
        return new ArrayList<>(Stream.of(clazz.getEnumConstants()).map(Object::toString).toList());
    }

    @NotNull
    @Deprecated
    public static String getEnums(@NotNull Class<? extends Enum<?>> clazz, @NotNull String delimiter) {
        return StringUtil.inlineEnum(clazz, delimiter);
    }

    @NotNull
    public static <T extends Enum<T>> T next(@NotNull Enum<T> numeration) {
        return shifted(numeration, 1);
    }

    @NotNull
    public static <T extends Enum<T>> T next(@NotNull Enum<T> numeration, @NotNull Predicate<T> predicate) {
        return shifted(numeration, 1, predicate);
    }

    @NotNull
    public static <T extends Enum<T>> T previous(@NotNull Enum<T> numeration) {
        return shifted(numeration, -1);
    }

    @NotNull
    public static <T extends Enum<T>> T previous(@NotNull Enum<T> numeration, @NotNull Predicate<T> predicate) {
        return shifted(numeration, -1, predicate);
    }

    @NotNull
    public static <T extends Enum<T>> T shifted(@NotNull Enum<T> numeration, int shift) {
        return shifted(numeration, shift, null);
    }

    @NotNull
    private static <T extends Enum<T>> T shifted(@NotNull Enum<T> numeration, int shift, @Nullable Predicate<T> predicate) {
        T[] values = numeration.getDeclaringClass().getEnumConstants();
        return shifted(values, numeration/*.ordinal()*/, shift, predicate);
    }

    @NotNull
    private static <T extends Enum<T>> T shifted(T[] values, @NotNull Enum<T> origin, int shift, @Nullable Predicate<T> predicate) {
        if (predicate != null) {
            T source = origin.getDeclaringClass().cast(origin);
            List<T> filtered = new ArrayList<>(Arrays.asList(values));
            filtered.removeIf(num -> !predicate.test(num) && num != source);

            int currentIndex = filtered.indexOf(source);
            //List<T> filtered = Stream.of(values).filter(predicate).toList();
            if (currentIndex < 0 | filtered.isEmpty()) return source;//values[currentIndex];

            return shifted(filtered, currentIndex, shift);
        }
        return shifted(values, origin.ordinal(), shift);
    }

    @NotNull
    public static <T> T shifted(T[] values, int currentIndex, int shift) {
        int index = currentIndex + shift;
        return values[index >= values.length || index < 0 ? 0 : index];
    }

    @NotNull
    public static <T> T shifted(@NotNull List<T> values, int currentIndex, int shift) {
        int index = currentIndex + shift;
        if (index < 0) return values.get(values.size() - 1);

        return values.get(index >= values.size() ? 0 : index);
    }
}
