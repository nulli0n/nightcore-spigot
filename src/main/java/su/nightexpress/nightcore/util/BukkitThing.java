package su.nightexpress.nightcore.util;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.MenuType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BukkitThing {

    @NotNull
    public static List<String> worldNames() {
        return Bukkit.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
    }

    @Nullable
    public static <T extends Keyed> T fromRegistry(@NotNull Registry<T> registry, @NotNull String key) {
        try {
            NamespacedKey namespacedKey = NamespacedKey.minecraft(key.toLowerCase());
            return registry.get(namespacedKey);
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @NotNull
    public static <T extends Keyed> Set<T> allFromRegistry(@NotNull Registry<T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).collect(Collectors.toSet());
        }
        return registry.stream().collect(Collectors.toSet());
    }

    @NotNull
    public static <T extends Keyed> List<String> getNames(@NotNull Registry<T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).map(BukkitThing::toString).toList();
        }
        return registry.stream().map(BukkitThing::toString).toList();
    }

    @NotNull
    public static String toString(@NotNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @Nullable
    public static Material getMaterial(@NotNull String name) {
        return fromRegistry(Registry.MATERIAL, name);
    }

    @NotNull
    public static Set<Material> getMaterials() {
        return allFromRegistry(Registry.MATERIAL);
    }

    @NotNull
    public static Set<Enchantment> getEnchantments() {
        return allFromRegistry(Registry.ENCHANTMENT);
    }

    @NotNull
    public static Set<PotionEffectType> getEffectTypes() {
        if (Version.isBehind(Version.V1_20_R2)) {
            return Stream.of(PotionEffectType.values()).collect(Collectors.toSet());
        }
        return allFromRegistry(Registry.EFFECT);
    }

    @Nullable
    public static Enchantment getEnchantment(@NotNull String name) {
        return fromRegistry(Registry.ENCHANTMENT, name);
    }

    @Nullable
    public static EntityType getEntityType(@NotNull String name) {
        return fromRegistry(Registry.ENTITY_TYPE, name);
    }

    @Nullable
    public static Attribute getAttribute(@NotNull String name) {
        return fromRegistry(Registry.ATTRIBUTE, name);
    }

    @Nullable
    public static PotionEffectType getPotionEffect(@NotNull String name) {
        return fromRegistry(Registry.EFFECT, name);
    }

    @Nullable
    public static PotionType getPotionType(@NotNull String name) {
        return fromRegistry(Registry.POTION, name);
    }

    @Nullable
    public static Sound getSound(@NotNull String name) {
//        if (Version.isBehind(Version.MC_1_21_3)) {
//            return Sound.valueOf(name.toUpperCase());
//        }
        return fromRegistry(Registry.SOUNDS, name);
    }

    @Nullable
    public static Particle getParticle(@NotNull String name) {
        return fromRegistry(Registry.PARTICLE_TYPE, name);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Nullable
    public static MenuType getMenuType(@NotNull String name) {
        return fromRegistry(Registry.MENU, name);
    }
}
