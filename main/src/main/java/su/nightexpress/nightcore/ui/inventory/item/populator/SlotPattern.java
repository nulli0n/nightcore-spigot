package su.nightexpress.nightcore.ui.inventory.item.populator;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public class SlotPattern implements SlotProvider, Writeable {

    private static final ConfigCodec<TreeMap<Integer, int[]>> MAP_TYPE = ConfigCodecs.forIntTreeMap(
        ConfigCodecs.INT_ARRAY
    );

    private final TreeMap<Integer, int[]> layouts;

    public SlotPattern() {
        this(new TreeMap<>());
    }

    public SlotPattern(@NonNull SortedMap<Integer, int[]> layouts) {
        this.layouts = new TreeMap<>(layouts);
    }

    public static @NonNull SlotPattern of(@NonNull SortedMap<Integer, int[]> layouts) {
        return new SlotPattern(layouts);
    }

    public static @NonNull SlotPattern read(@NonNull FileConfig config, @NonNull String path) {
        return new SlotPattern(config.get(path, MAP_TYPE, new TreeMap<>()));
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path, MAP_TYPE, this.layouts);
    }

    /**
     * Maps an item amount to a specific array of slots.
     */
    public @NonNull SlotPattern with(int amount, int... slots) {
        this.layouts.put(amount, slots);
        return this;
    }

    /**
     * Gets the best fitting slot array for the given amount.
     * Uses floorEntry to fallback to the closest smaller layout if an exact match isn't found.
     */
    @Override
    public int[] getSlots(int itemsAmount) {
        if (this.layouts.isEmpty()) return new int[0];

        Map.Entry<Integer, int[]> entry = this.layouts.floorEntry(itemsAmount);
        // If amount is smaller than the smallest defined layout, use the smallest available
        if (entry == null) {
            return this.layouts.firstEntry().getValue();
        }
        return entry.getValue();
    }

    public @NonNull SortedMap<Integer, int[]> getLayouts() {
        return this.layouts;
    }
}