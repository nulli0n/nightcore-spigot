package su.nightexpress.nightcore.util;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class LangUtil {

    @NotNull
    public static String getTranslationKey(@NotNull Material material) {
        return Engine.software().getTranslationKey(material);
    }

    @NotNull
    public static String getTranslationKey(@NotNull Attribute attribute) {
        return Engine.software().getTranslationKey(attribute);
    }

    @NotNull
    public static String getTranslationKey(@NotNull Enchantment enchantment) {
        return Engine.software().getTranslationKey(enchantment);
    }

    @NotNull
    public static String getTranslationKey(@NotNull EntityType entityType) {
        return Engine.software().getTranslationKey(entityType);
    }

    @NotNull
    public static String getTranslationKey(@NotNull PotionEffectType effectType) {
        return Engine.software().getTranslationKey(effectType);
    }



    @NotNull
    public static String getSerializedName(@NotNull Material material) {
        return TagWrappers.LANG.apply(getTranslationKey(material));
    }

    @NotNull
    public static String getSerializedName(@NotNull Attribute attribute) {
        return TagWrappers.LANG.apply(getTranslationKey(attribute));
    }

    @NotNull
    public static String getSerializedName(@NotNull Enchantment enchantment) {
        return Version.isPaper() ? PaperBridge.serializeComponent(enchantment.description()) : TagWrappers.LANG_OR.apply(getTranslationKey(enchantment), BukkitThing.getValue(enchantment));
    }

    @NotNull
    public static String getSerializedName(@NotNull EntityType entityType) {
        return TagWrappers.LANG.apply(getTranslationKey(entityType));
    }

    @NotNull
    public static String getSerializedName(@NotNull PotionEffectType effectType) {
        return TagWrappers.LANG.apply(getTranslationKey(effectType));
    }

    @NotNull
    public static String getEnchantmentLevelLang(int level) {
        return TagWrappers.LANG_OR.apply("enchantment.level." + level, String.valueOf(level));
    }
}
