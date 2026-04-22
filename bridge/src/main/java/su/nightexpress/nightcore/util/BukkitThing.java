package su.nightexpress.nightcore.util;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.bridge.RegistryType;

public class BukkitThing {

    @Deprecated
    public static final char DEFAULT_SEPARATOR = ':';

    @NonNull
    public static List<String> worldNames() {
        return Bukkit.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
    }

    @Deprecated
    private static boolean isValidNamespaceChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }

    @Deprecated
    private static boolean isValidKeyChar(char c) {
        return isValidNamespaceChar(c) || c == '/';
    }

    @NonNull
    @Deprecated
    private static String validateNamespaceOrValue(@NonNull String str, @NonNull Predicate<Character> predicate) {
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

    @NonNull
    @Deprecated
    public static String validateNamespace(@NonNull String namespace) {
        return validateNamespaceOrValue(namespace, BukkitThing::isValidNamespaceChar);
    }

    @NonNull
    @Deprecated
    public static String validateValue(@NonNull String value) {
        return validateNamespaceOrValue(value, BukkitThing::isValidKeyChar);
    }

    @NonNull
    @Deprecated
    public static NamespacedKey parseKey(@NonNull String string) {
        return NightKey.key(string).toBukkit();

        /*int index = string.indexOf(DEFAULT_SEPARATOR);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;
        
        return parseKey(namespace, value);*/
    }

    @NonNull
    @Deprecated
    public static NamespacedKey parseKey(@NonNull String namespace, @NonNull String value) {
        return NightKey.key(namespace, value).toBukkit();

        /*namespace = BukkitThing.validateNamespace(namespace);
        value = BukkitThing.validateValue(value);
        
        return new NamespacedKey(namespace, value);*/
    }

    @Nullable
    @Deprecated
    public static <T extends Keyed> T fromRegistry(@NonNull Registry<@NonNull T> registry, @NonNull String key) {
        return registry.get(parseKey(key));

        //return fromRegistry(registry, NamespacedKey.MINECRAFT, key);
    }

    @Nullable
    @Deprecated
    public static <T extends Keyed> T fromRegistry(@NonNull Registry<@NonNull T> registry, @NonNull String namespace,
                                                   @NonNull String key) {
        return registry.get(parseKey(namespace, key));

        //        try {
        //            NamespacedKey namespacedKey = new NamespacedKey(namespace.toLowerCase(), key.toLowerCase());
        //            return registry.get(namespacedKey);
        //        }
        //        catch (IllegalArgumentException exception) {
        //            return null;
        //        }
    }

    @NonNull
    @Deprecated
    public static <T extends Keyed> Set<T> allFromRegistry(@NonNull Registry<@NonNull T> registry) {
        return registry.stream().collect(Collectors.toSet());
    }

    @NonNull
    @Deprecated
    public static <T extends Keyed> List<String> getNames(@NonNull Registry<@NonNull T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).map(BukkitThing::toString).toList();
        }
        return registry.stream().map(BukkitThing::toString).toList();
    }

    @NonNull
    @Deprecated
    public static String toString(@NonNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @Nullable
    public static <T extends Keyed> T getByString(@NonNull RegistryType<T> registryKey, @NonNull String string) {
        return NightKey.parse(string).map(key -> getByKey(registryKey, key)).orElse(null);
    }

    @Nullable
    public static <T extends Keyed> T getByNamespaceValue(@NonNull RegistryType<T> registryKey,
                                                          @NonNull String namespace, @NonNull String value) {
        return NightKey.parse(namespace, value).map(key -> getByKey(registryKey, key)).orElse(null);
    }

    @Nullable
    public static <T extends Keyed> T getByKey(@NonNull RegistryType<T> registryType, @NonNull NightKey key) {
        return getByKey(registryType, key.toBukkit());
    }

    @Nullable
    public static <T extends Keyed> T getByKey(@NonNull RegistryType<T> registryType, @NonNull NamespacedKey key) {
        return registryType.getRegistry().get(key);
    }

    @NonNull
    public static <T extends Keyed> Set<T> getAll(@NonNull RegistryType<T> registryType) {
        return registryType.getRegistry().stream().collect(Collectors.toSet());
    }

    @NonNull
    public static <T extends Keyed> List<NamespacedKey> getKeys(@NonNull RegistryType<T> registryType) {
        var registry = registryType.getRegistry();

        return Version.isPaper() ? registry.keyStream().toList() : registry.stream().map(Keyed::getKey).toList();
    }

    @NonNull
    public static <T extends Keyed> List<String> getValues(@NonNull RegistryType<T> registryType) {
        return registryType.getRegistry().stream().map(BukkitThing::getValue).toList();
    }

    @NonNull
    public static <T extends Keyed> List<String> getAsStrings(@NonNull RegistryType<T> registryType) {
        return getKeys(registryType).stream().map(BukkitThing::getAsString).toList();
    }

    @NonNull
    public static String getNamespace(@NonNull Keyed keyed) {
        return keyed.getKey().getNamespace();
    }

    @NonNull
    public static String getValue(@NonNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @NonNull
    public static String getAsString(@NonNull Keyed keyed) {
        return getAsString(keyed.getKey());
    }

    @NonNull
    public static String getAsString(@NonNull NamespacedKey key) {
        return Version.isPaper() ? key.asString() : key.getNamespace() + NightKey.DELIMITER + key.getKey();
    }


    @NonNull
    public static Set<Material> getMaterials() {
        return getAll(RegistryType.MATERIAL);
    }

    @NonNull
    public static Set<Enchantment> getEnchantments() {
        return getAll(RegistryType.ENCHANTMENT);
    }

    @NonNull
    public static Set<Attribute> getAttributes() {
        return getAll(RegistryType.ATTRIBUTE);
    }

    @NonNull
    public static Set<ItemType> getItemTypes() {
        return getAll(RegistryType.MC_1_21.ITEM);
    }

    @NonNull
    public static Set<BlockType> getBlockTypes() {
        return getAll(RegistryType.MC_1_21.BLOCK);
    }

    @NonNull
    public static Set<PotionEffectType> getEffectTypes() {
        return getAll(RegistryType.MOB_EFFECT);
    }


    @Nullable
    public static Material getMaterial(@NonNull String name) {
        return getByString(RegistryType.MATERIAL, name);
    }

    @Nullable
    public static ItemType getItemType(@NonNull String name) {
        return getByString(RegistryType.MC_1_21.ITEM, name);
    }

    @Nullable
    public static BlockType getBlockType(@NonNull String name) {
        return getByString(RegistryType.MC_1_21.BLOCK, name);
    }

    @Nullable
    public static Enchantment getEnchantment(@NonNull String name) {
        return getByString(RegistryType.ENCHANTMENT, name);
    }

    @Nullable
    public static EntityType getEntityType(@NonNull String name) {
        return getByString(RegistryType.ENTITY_TYPE, name);
    }

    @Nullable
    public static Attribute getAttribute(@NonNull String name) {
        return getByString(RegistryType.ATTRIBUTE, name);
    }

    @Nullable
    @Deprecated
    public static PotionEffectType getPotionEffect(@NonNull String name) {
        return getEffectType(name);
    }

    @Nullable
    public static PotionEffectType getEffectType(@NonNull String name) {
        return getByString(RegistryType.MOB_EFFECT, name);
    }

    @Nullable
    public static PotionType getPotionType(@NonNull String name) {
        return getByString(RegistryType.POTION, name);
    }

    @Nullable
    public static Sound getSound(@NonNull String name) {
        return getByString(RegistryType.SOUND, name);
    }

    @Nullable
    public static Particle getParticle(@NonNull String name) {
        return getByString(RegistryType.PARTICLE_TYPE, name);
    }

    @Nullable
    public static MenuType getMenuType(@NonNull String name) {
        return getByString(RegistryType.MC_1_21.MENU, name);
    }
}
