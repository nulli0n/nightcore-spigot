package su.nightexpress.nightcore.util;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BukkitThing {

    public static final char DEFAULT_SEPARATOR = ':';

    @NotNull
    public static List<String> worldNames() {
        return Bukkit.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
    }

    private static boolean isValidNamespaceChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }

    private static boolean isValidKeyChar(char c) {
        return isValidNamespaceChar(c) || c == '/';
    }

    @NotNull
    private static String validateNamespaceOrValue(@NotNull String str, @NotNull Predicate<Character> predicate) {
        char[] chars = LowerCase.INTERNAL.apply(str).toCharArray();

        StringBuilder builder = new StringBuilder();
        for (char letter : chars) {
            if (Character.isWhitespace(letter)) {
                builder.append("_");
                continue;
            }
            if (!predicate.test(letter)) {
                continue;
            }
            builder.append(Character.toLowerCase(letter));
        }

        return builder.toString();
    }

    @NotNull
    public static String validateNamespace(@NotNull String namespace) {
        return validateNamespaceOrValue(namespace, BukkitThing::isValidNamespaceChar);
    }

    @NotNull
    public static String validateValue(@NotNull String value) {
        return validateNamespaceOrValue(value, BukkitThing::isValidKeyChar);
    }

    @NotNull
    public static NamespacedKey parseKey(@NotNull String string) {
        int index = string.indexOf(DEFAULT_SEPARATOR);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;

        return parseKey(namespace, value);
    }

    @NotNull
    public static NamespacedKey parseKey(@NotNull String namespace, @NotNull String value) {
        namespace = BukkitThing.validateNamespace(namespace);
        value = BukkitThing.validateValue(value);

        return new NamespacedKey(namespace, value);
    }

    @Nullable
    @Deprecated
    public static <T extends Keyed> T fromRegistry(@NotNull Registry<@NotNull T> registry, @NotNull String key) {
        return registry.get(parseKey(key));

        //return fromRegistry(registry, NamespacedKey.MINECRAFT, key);
    }

    @Nullable
    @Deprecated
    public static <T extends Keyed> T fromRegistry(@NotNull Registry<@NotNull T> registry, @NotNull String namespace, @NotNull String key) {
        return registry.get(parseKey(namespace, key));

//        try {
//            NamespacedKey namespacedKey = new NamespacedKey(namespace.toLowerCase(), key.toLowerCase());
//            return registry.get(namespacedKey);
//        }
//        catch (IllegalArgumentException exception) {
//            return null;
//        }
    }

    @NotNull
    @Deprecated
    public static <T extends Keyed> Set<T> allFromRegistry(@NotNull Registry<@NotNull T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).collect(Collectors.toSet());
        }
        return registry.stream().collect(Collectors.toSet());
    }

    @NotNull
    @Deprecated
    public static <T extends Keyed> List<String> getNames(@NotNull Registry<@NotNull T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).map(BukkitThing::toString).toList();
        }
        return registry.stream().map(BukkitThing::toString).toList();
    }

    @NotNull
    @Deprecated
    public static String toString(@NotNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @Nullable
    public static <T extends Keyed> T getByString(@NotNull RegistryType<T> registryKey, @NotNull String string) {
        return getByKey(registryKey, BukkitThing.parseKey(string));
    }

    @Nullable
    public static <T extends Keyed> T getByNamespaceValue(@NotNull RegistryType<T> registryKey, @NotNull String namespace, @NotNull String value) {
        return getByKey(registryKey, BukkitThing.parseKey(namespace, value));
    }

    @Nullable
    public static <T extends Keyed> T getByKey(@NotNull RegistryType<T> registryType, @NotNull NamespacedKey key) {
        return registryType.getRegistry().get(key);
    }

    @NotNull
    public static <T extends Keyed> Set<T> getAll(@NotNull RegistryType<T> registryType) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registryType.getRegistry().spliterator(), false).collect(Collectors.toSet());
        }
        return registryType.getRegistry().stream().collect(Collectors.toSet());
    }

    @NotNull
    public static <T extends Keyed> List<NamespacedKey> getKeys(@NotNull RegistryType<T> registryType) {
        var registry = registryType.getRegistry();

        return Version.isPaper() && Version.isAtLeast(Version.MC_1_21_5) ? registry.keyStream().toList() : registry.stream().map(Keyed::getKey).toList();
    }

    @NotNull
    public static <T extends Keyed> List<String> getValues(@NotNull RegistryType<T> registryType) {
        return registryType.getRegistry().stream().map(BukkitThing::getValue).toList();
    }

    @NotNull
    public static <T extends Keyed> List<String> getAsStrings(@NotNull RegistryType<T> registryType) {
        return getKeys(registryType).stream().map(BukkitThing::getAsString).toList();
    }

    @NotNull
    public static String getNamespace(@NotNull Keyed keyed) {
        return keyed.getKey().getNamespace();
    }

    @NotNull
    public static String getValue(@NotNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @NotNull
    public static String getAsString(@NotNull Keyed keyed) {
        return getAsString(keyed.getKey());
    }

    @NotNull
    public static String getAsString(@NotNull NamespacedKey key) {
        return Version.isPaper() ? key.asString() : key.getNamespace() + DEFAULT_SEPARATOR + key.getKey();
    }



    @NotNull
    public static Set<Material> getMaterials() {
        return getAll(RegistryType.MATERIAL);
    }

    @NotNull
    public static Set<Enchantment> getEnchantments() {
        return getAll(RegistryType.ENCHANTMENT);
    }

    @NotNull
    public static Set<Attribute> getAttributes() {
        return getAll(RegistryType.ATTRIBUTE);
    }

    @NotNull
    public static Set<ItemType> getItemTypes() {
        return getAll(RegistryType.MC_1_21.ITEM);
    }

    @NotNull
    public static Set<BlockType> getBlockTypes() {
        return getAll(RegistryType.MC_1_21.BLOCK);
    }

    @NotNull
    public static Set<PotionEffectType> getEffectTypes() {
        if (Version.isBehind(Version.V1_20_R2)) {
            return Stream.of(PotionEffectType.values()).collect(Collectors.toSet());
        }
        return getAll(RegistryType.MOB_EFFECT);
    }





    @Nullable
    public static Material getMaterial(@NotNull String name) {
        return getByString(RegistryType.MATERIAL, name);
    }

    @Nullable
    public static ItemType getItemType(@NotNull String name) {
        return getByString(RegistryType.MC_1_21.ITEM, name);
    }

    @Nullable
    public static BlockType getBlockType(@NotNull String name) {
        return getByString(RegistryType.MC_1_21.BLOCK, name);
    }

    @Nullable
    public static Enchantment getEnchantment(@NotNull String name) {
        return getByString(RegistryType.ENCHANTMENT, name);
    }

    @Nullable
    public static EntityType getEntityType(@NotNull String name) {
        return getByString(RegistryType.ENTITY_TYPE, name);
    }

    @Nullable
    public static Attribute getAttribute(@NotNull String name) {
        return getByString(RegistryType.ATTRIBUTE, name);
    }

    @Nullable
    public static PotionEffectType getPotionEffect(@NotNull String name) {
        return getByString(RegistryType.MOB_EFFECT, name);
    }

    @Nullable
    public static PotionType getPotionType(@NotNull String name) {
        return getByString(RegistryType.POTION, name);
    }

    @Nullable
    public static Sound getSound(@NotNull String name) {
//        if (Version.isBehind(Version.MC_1_21_3)) {
//            return Sound.valueOf(name.toUpperCase());
//        }
        return getByString(RegistryType.SOUND, name);
    }

    @Nullable
    public static Particle getParticle(@NotNull String name) {
        return getByString(RegistryType.PARTICLE_TYPE, name);
    }

    @Nullable
    public static MenuType getMenuType(@NotNull String name) {
        return getByString(RegistryType.MC_1_21.MENU, name);
    }
}
