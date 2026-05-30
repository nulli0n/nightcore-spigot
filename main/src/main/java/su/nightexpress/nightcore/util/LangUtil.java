package su.nightexpress.nightcore.util;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class LangUtil {

    @NonNull
    public static String getTranslationKey(@NonNull Material material) {
        return Engine.software().getTranslationKey(material);
    }

    @NonNull
    public static String getTranslationKey(@NonNull Attribute attribute) {
        return Engine.software().getTranslationKey(attribute);
    }

    @NonNull
    public static String getTranslationKey(@NonNull Enchantment enchantment) {
        return Engine.software().getTranslationKey(enchantment);
    }

    @NonNull
    public static String getTranslationKey(@NonNull EntityType entityType) {
        return Engine.software().getTranslationKey(entityType);
    }

    @NonNull
    public static String getTranslationKey(@NonNull PotionEffectType effectType) {
        return Engine.software().getTranslationKey(effectType);
    }


    @NonNull
    public static String getSerializedName(@NonNull Material material) {
        return TagWrappers.LANG.apply(getTranslationKey(material));
    }

    @NonNull
    public static String getSerializedName(@NonNull Attribute attribute) {
        return TagWrappers.LANG.apply(getTranslationKey(attribute));
    }

    @NonNull
    public static String getSerializedName(@NonNull Enchantment enchantment) {
        return Version.isPaper() ? PaperBridge.serializeComponent(enchantment.description()) : TagWrappers.LANG_OR
            .apply(getTranslationKey(enchantment), BukkitThing.getValue(enchantment));
    }

    @NonNull
    public static String getSerializedName(@NonNull EntityType entityType) {
        return TagWrappers.LANG.apply(getTranslationKey(entityType));
    }

    @NonNull
    public static String getSerializedName(@NonNull PotionEffectType effectType) {
        return TagWrappers.LANG.apply(getTranslationKey(effectType));
    }

    @NonNull
    public static String getEnchantmentLevelLang(int level) {
        return TagWrappers.LANG_OR.apply("enchantment.level." + level, String.valueOf(level));
    }
}
