package su.nightexpress.nightcore.util;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BlockUtil {

    private static final Set<Material> FUNCTIONAL = new HashSet<>();

    public static boolean isFunctional(@NotNull Block block) {
        return isFunctional(block.getType());
    }

    public static boolean isFunctional(@NotNull Material material) {
        return FUNCTIONAL.contains(material);
    }

    static {
        FUNCTIONAL.addAll(Tag.ANVIL.getValues());
        FUNCTIONAL.add(Material.BARREL);
        FUNCTIONAL.add(Material.BEACON);
        FUNCTIONAL.add(Material.BREWING_STAND);
        FUNCTIONAL.add(Material.CARTOGRAPHY_TABLE);
        FUNCTIONAL.add(Material.CHEST);
        FUNCTIONAL.add(Material.TRAPPED_CHEST);
        FUNCTIONAL.add(Material.ENDER_CHEST);
        FUNCTIONAL.add(Material.CRAFTING_TABLE);
        FUNCTIONAL.add(Material.ENCHANTING_TABLE);
        FUNCTIONAL.add(Material.FURNACE);
        FUNCTIONAL.add(Material.BLAST_FURNACE);
        FUNCTIONAL.add(Material.SMOKER);
        FUNCTIONAL.add(Material.GRINDSTONE);
        FUNCTIONAL.add(Material.LECTERN);
        FUNCTIONAL.add(Material.LOOM);
        FUNCTIONAL.addAll(Tag.SHULKER_BOXES.getValues());
        FUNCTIONAL.addAll(Tag.SIGNS.getValues());
        FUNCTIONAL.add(Material.SMITHING_TABLE);
        FUNCTIONAL.add(Material.STONECUTTER);
        FUNCTIONAL.addAll(Tag.ITEMS_BANNERS.getValues());
        FUNCTIONAL.add(Material.BEEHIVE);
        FUNCTIONAL.addAll(Tag.BEDS.getValues());
        FUNCTIONAL.add(Material.BELL);
        FUNCTIONAL.add(Material.CAKE);
        FUNCTIONAL.addAll(Tag.CANDLE_CAKES.getValues());
        FUNCTIONAL.add(Material.CAMPFIRE);
        FUNCTIONAL.add(Material.SOUL_CAMPFIRE);
        FUNCTIONAL.addAll(Tag.CAULDRONS.getValues());
        FUNCTIONAL.add(Material.CHISELED_BOOKSHELF);
        FUNCTIONAL.add(Material.COMPOSTER);
        FUNCTIONAL.add(Material.CONDUIT);
        FUNCTIONAL.add(Material.DECORATED_POT);
        FUNCTIONAL.add(Material.END_GATEWAY);
        FUNCTIONAL.add(Material.END_PORTAL);
        FUNCTIONAL.add(Material.END_PORTAL_FRAME);
        FUNCTIONAL.add(Material.FARMLAND);
        FUNCTIONAL.add(Material.FLETCHING_TABLE);
        FUNCTIONAL.add(Material.FLOWER_POT);
        FUNCTIONAL.add(Material.FROSTED_ICE);
        FUNCTIONAL.addAll(Tag.ITEMS_SKULLS.getValues());
        FUNCTIONAL.add(Material.HEAVY_CORE);
        FUNCTIONAL.add(Material.JUKEBOX);
        FUNCTIONAL.add(Material.LADDER);
        FUNCTIONAL.add(Material.LODESTONE);
        FUNCTIONAL.add(Material.SPAWNER);
        FUNCTIONAL.add(Material.NETHER_PORTAL);
        FUNCTIONAL.add(Material.RESPAWN_ANCHOR);
        FUNCTIONAL.add(Material.SCAFFOLDING);
        FUNCTIONAL.add(Material.SPONGE);
        FUNCTIONAL.add(Material.WET_SPONGE);
        FUNCTIONAL.add(Material.SUSPICIOUS_GRAVEL);
        FUNCTIONAL.add(Material.SUSPICIOUS_SAND);
        FUNCTIONAL.add(Material.TNT);
        FUNCTIONAL.add(Material.TRIAL_SPAWNER);
        FUNCTIONAL.add(Material.VAULT);
        FUNCTIONAL.addAll(Tag.BUTTONS.getValues());
        FUNCTIONAL.add(Material.COPPER_BULB);
        FUNCTIONAL.add(Material.CRAFTER);
        FUNCTIONAL.add(Material.DAYLIGHT_DETECTOR);
        FUNCTIONAL.add(Material.DISPENSER);
        FUNCTIONAL.add(Material.DROPPER);
        FUNCTIONAL.addAll(Tag.DOORS.getValues());
        FUNCTIONAL.add(Material.HONEY_BLOCK);
        FUNCTIONAL.add(Material.HOPPER);
        FUNCTIONAL.add(Material.LEVER);
        FUNCTIONAL.add(Material.LIGHTNING_ROD);
        FUNCTIONAL.add(Material.NOTE_BLOCK);
        FUNCTIONAL.add(Material.OBSERVER);
        FUNCTIONAL.add(Material.PISTON);
        FUNCTIONAL.add(Material.PISTON_HEAD);
        FUNCTIONAL.add(Material.STICKY_PISTON);
        FUNCTIONAL.add(Material.MOVING_PISTON);
        FUNCTIONAL.addAll(Tag.PRESSURE_PLATES.getValues());
        FUNCTIONAL.addAll(Tag.RAILS.getValues());
        FUNCTIONAL.add(Material.REDSTONE_LAMP);
        FUNCTIONAL.add(Material.REDSTONE_WIRE);
        FUNCTIONAL.add(Material.COMPARATOR);
        FUNCTIONAL.add(Material.REPEATER);
        FUNCTIONAL.add(Material.REDSTONE_TORCH);
        FUNCTIONAL.add(Material.SLIME_BLOCK);
        FUNCTIONAL.add(Material.TARGET);
        FUNCTIONAL.addAll(Tag.TRAPDOORS.getValues());
        FUNCTIONAL.addAll(Tag.FENCE_GATES.getValues());
        FUNCTIONAL.add(Material.TRIPWIRE);
        FUNCTIONAL.add(Material.TRIPWIRE_HOOK);
    }
}
