package su.nightexpress.nightcore.integration.item;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;

import java.util.List;

public class ItemPlugins {

    public static final String EXECUTABLE_ITEMS = "ExecutableItems";
    public static final String EXCELLENT_CRATES = "ExcellentCrates";
    public static final String ITEMS_ADDER      = "ItemsAdder";
    public static final String MMOITEMS         = "MMOItems";
    public static final String NEXO             = "Nexo";
    public static final String ORAXEN           = "Oraxen";
    public static final String CRAFT_ENGINE     = "CraftEngine";

    @NotNull
    public static List<String> values() {
        return Lists.newList(EXCELLENT_CRATES, EXECUTABLE_ITEMS, ITEMS_ADDER, MMOITEMS, NEXO, ORAXEN, CRAFT_ENGINE);
    }
}
