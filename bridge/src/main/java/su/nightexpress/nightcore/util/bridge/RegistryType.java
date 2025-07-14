package su.nightexpress.nightcore.util.bridge;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Version;

import java.util.function.Function;

public interface RegistryType<T extends Keyed> {

    RegistryType<Material>            MATERIAL            = type(isSpigot -> Registry.MATERIAL);
    RegistryType<Enchantment>         ENCHANTMENT         = type(isSpigot -> isSpigot ? forSpigot(Enchantment.class) : forPaper(RegistryKey.ENCHANTMENT));
    RegistryType<GameEvent>           GAME_EVENT          = type(isSpigot -> isSpigot ? Registry.GAME_EVENT : forPaper(RegistryKey.GAME_EVENT));
    RegistryType<StructureType>       STRUCTURE_TYPE      = type(isSpigot -> isSpigot ? Registry.STRUCTURE_TYPE : forPaper(RegistryKey.STRUCTURE_TYPE));
    RegistryType<PotionEffectType>    MOB_EFFECT          = type(isSpigot -> isSpigot ? Registry.EFFECT : forPaper(RegistryKey.MOB_EFFECT));
    RegistryType<Villager.Profession> VILLAGER_PROFESSION = type(isSpigot -> isSpigot ? Registry.VILLAGER_PROFESSION : forPaper(RegistryKey.VILLAGER_PROFESSION));
    RegistryType<Villager.Type>       VILLAGER_TYPE       = type(isSpigot -> isSpigot ? Registry.VILLAGER_TYPE : forPaper(RegistryKey.VILLAGER_TYPE));
    RegistryType<Attribute>           ATTRIBUTE           = type(isSpigot -> isSpigot ? Registry.ATTRIBUTE : forPaper(RegistryKey.ATTRIBUTE));
    RegistryType<Fluid>               FLUID               = type(isSpigot -> isSpigot ? Registry.FLUID : forPaper(RegistryKey.FLUID));
    RegistryType<Sound>               SOUND               = type(isSpigot -> isSpigot ? Registry.SOUNDS : forPaper(RegistryKey.SOUND_EVENT));
    RegistryType<Biome>               BIOME               = type(isSpigot -> isSpigot ? forSpigot(Biome.class) : forPaper(RegistryKey.BIOME));
    RegistryType<Structure>           STRUCTURE           = type(isSpigot -> isSpigot ? forSpigot(Structure.class) : forPaper(RegistryKey.STRUCTURE));
    RegistryType<TrimMaterial>        TRIM_MATERIAL       = type(isSpigot -> isSpigot ? forSpigot(TrimMaterial.class) : forPaper(RegistryKey.TRIM_MATERIAL));
    RegistryType<TrimPattern>         TRIM_PATTERN        = type(isSpigot -> isSpigot ? forSpigot(TrimPattern.class) : forPaper(RegistryKey.TRIM_PATTERN));
    RegistryType<DamageType>          DAMAGE_TYPE         = type(isSpigot -> isSpigot ? forSpigot(DamageType.class) : forPaper(RegistryKey.DAMAGE_TYPE));
    RegistryType<PatternType>         BANNER_PATTERN      = type(isSpigot -> isSpigot ? forSpigot(PatternType.class) : forPaper(RegistryKey.BANNER_PATTERN));
    RegistryType<Art>                 PAINTING_VARIANT    = type(isSpigot -> isSpigot ? forSpigot(Art.class) : forPaper(RegistryKey.PAINTING_VARIANT));
    RegistryType<MusicInstrument>     INSTRUMENT          = type(isSpigot -> isSpigot ? forSpigot(MusicInstrument.class) : forPaper(RegistryKey.INSTRUMENT));
    RegistryType<Cat.Type>            CAT_VARIANT         = type(isSpigot -> isSpigot ? forSpigot(Cat.Type.class) : forPaper(RegistryKey.CAT_VARIANT));
    RegistryType<Frog.Variant>        FROG_VARIANT        = type(isSpigot -> isSpigot ? forSpigot(Frog.Variant.class) : forPaper(RegistryKey.FROG_VARIANT));
    RegistryType<EntityType>          ENTITY_TYPE         = type(isSpigot -> isSpigot ? Registry.ENTITY_TYPE : forPaper(RegistryKey.ENTITY_TYPE));
    RegistryType<Particle>            PARTICLE_TYPE       = type(isSpigot -> isSpigot ? Registry.PARTICLE_TYPE : forPaper(RegistryKey.PARTICLE_TYPE));
    RegistryType<PotionType>          POTION              = type(isSpigot -> isSpigot ? Registry.POTION : forPaper(RegistryKey.POTION));

    class MC_1_21 {

        public static final RegistryType<BlockType>      BLOCK               = type(isSpigot -> isSpigot ? Registry.BLOCK : forPaper(RegistryKey.BLOCK));
        public static final RegistryType<ItemType>       ITEM                = type(isSpigot -> isSpigot ? Registry.ITEM : forPaper(RegistryKey.ITEM));
        public static final RegistryType<JukeboxSong>    JUKEBOX_SONG        = type(isSpigot -> isSpigot ? forSpigot(JukeboxSong.class) : forPaper(RegistryKey.JUKEBOX_SONG));
        public static final RegistryType<MapCursor.Type> MAP_DECORATION_TYPE = type(isSpigot -> isSpigot ? Registry.MAP_DECORATION_TYPE : forPaper(RegistryKey.MAP_DECORATION_TYPE));
        public static final RegistryType<Wolf.Variant>   WOLF_VARIANT        = type(isSpigot -> isSpigot ? forSpigot(Wolf.Variant.class) : forPaper(RegistryKey.WOLF_VARIANT));
        public static final RegistryType<MenuType>       MENU                = type(isSpigot -> isSpigot ? Registry.MENU : forPaper(RegistryKey.MENU));
    }

    class MC_1_21_5 {

        public static final RegistryType<Chicken.Variant> CHICKEN_VARIANT = type(isSpigot -> isSpigot ? forSpigot(Chicken.Variant.class) : forPaper(RegistryKey.CHICKEN_VARIANT));
        public static final RegistryType<Cow.Variant>     COW_VARIANT     = type(isSpigot -> isSpigot ? forSpigot(Cow.Variant.class) : forPaper(RegistryKey.COW_VARIANT));
        public static final RegistryType<Pig.Variant>     PIG_VARIANT     = type(isSpigot -> isSpigot ? forSpigot(Pig.Variant.class) : forPaper(RegistryKey.PIG_VARIANT));
    }

    class Paper {

        public static final RegistryType<DataComponentType> DATA_COMPONENT_TYPE = type(isSpigot -> forPaper(RegistryKey.DATA_COMPONENT_TYPE));
        public static final RegistryType<MemoryKey<?>>      MEMORY_MODULE_TYPE  = type(isSpigot -> forPaper(RegistryKey.MEMORY_MODULE_TYPE));
        public static final RegistryType<Wolf.SoundVariant> WOLF_SOUND_VARIANT  = type(isSpigot -> forPaper(RegistryKey.WOLF_SOUND_VARIANT));
    }

    @NotNull Registry<@NotNull T> getRegistry();

    private static <T extends Keyed> RegistryType<T> type(@NotNull Function<Boolean, Registry<@NotNull T>> function) {
        var registry = function.apply(Version.isSpigot() || Version.isBehind(Version.MC_1_21));

        return () -> registry;
    }

    @SuppressWarnings("deprecation")
    private static <T extends Keyed> Registry<@NotNull T> forSpigot(@NotNull Class<T> clazz) {
        return Bukkit.getRegistry(clazz);
    }

    private static <T extends Keyed> Registry<@NotNull T> forPaper(@NotNull RegistryKey<T> key) {
        return RegistryAccess.registryAccess().getRegistry(key);
    }
}
