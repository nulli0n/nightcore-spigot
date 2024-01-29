package su.nightexpress.nightcore.util;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitThing {

    @Nullable
    public static <T extends Keyed> T fromRegistry(@NotNull Registry<T> registry, @NotNull String key) {
        key = StringUtil.lowerCaseUnderscoreStrict(key);

        NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
        return registry.get(namespacedKey);
    }

    @Nullable
    public static Material getMaterial(@NotNull String name) {
        return fromRegistry(Registry.MATERIAL, name);
    }

    @Nullable
    public static Enchantment getEnchantment(@NotNull String name) {
        if (Version.isBehind(Version.V1_19_R3)) {
            return Enchantment.getByKey(NamespacedKey.minecraft(StringUtil.lowerCaseUnderscoreStrict(name)));
        }
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
}
