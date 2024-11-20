package su.nightexpress.nightcore.util.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreLogger;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Utility class to create <b>cosmetic</b> items only.<br>
 * Do <b>NOT</b> use to create custom items for regular gameplay.
 */
public class NightItem implements Writeable {

    private Material material;
    private int      amount;

    private int     damage;
    private boolean unbreakable;

    private String itemName;
    private String       displayName;
    private List<String> lore;

    private String  skinURL;
    private Color   color;

    private Integer       modelData;
    private NamespacedKey modelPath;
    private NamespacedKey tooltipStyle;

    private boolean enchantGlint;
    private boolean hideComponents;
    private boolean hideTooltip;

    public NightItem(@NotNull Material material) {
        this(material, 1);
    }

    public NightItem(@NotNull Material material, int amount) {
        this.setMaterial(material);
        this.setAmount(amount);
    }

    @NotNull
    public static NightItem fromItemStack(@NotNull ItemStack itemStack) {
        NightItem nightItem = new NightItem(itemStack.getType());

        nightItem.setAmount(itemStack.getAmount());

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return nightItem;

        if (meta instanceof Damageable damageable) {
            nightItem.setDamage(damageable.getDamage());
        }

        nightItem.setDisplayName(meta.getDisplayName());
        nightItem.setLore(meta.getLore());
        nightItem.setUnbreakable(meta.isUnbreakable());
        nightItem.setSkinURL(ItemUtil.getHeadSkin(itemStack));
        nightItem.setModelData(meta.hasCustomModelData() ? meta.getCustomModelData() : null);
        if (Version.isAtLeast(Version.MC_1_21)) {
            nightItem.setItemName(meta.getItemName());
            nightItem.setEnchantGlint((meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride()) || meta.hasEnchants());
            nightItem.setHideTooltip(meta.isHideTooltip());
        }
        if (Version.isAtLeast(Version.MC_1_21_3)) {
            nightItem.setModelPath(meta.getItemModel());
            nightItem.setTooltipStyle(meta.getTooltipStyle());
        }
        nightItem.setHideComponents(!meta.getItemFlags().isEmpty());

        if (meta instanceof LeatherArmorMeta armorMeta) {
            nightItem.setColor(armorMeta.getColor());
        }
        else if (meta instanceof PotionMeta potionMeta) {
            nightItem.setColor(potionMeta.getColor());
        }

        return nightItem;
    }

    @NotNull
    public static NightItem read(@NotNull FileConfig config, @NotNull String path) {
        // -------- UPDATE OLD FIELDS - START --------
        String headTexture = config.getString(path + ".Head_Texture");
        if (headTexture != null && !headTexture.isEmpty()) {
            try {
                byte[] decoded = Base64.getDecoder().decode(headTexture);
                String decodedStr = new String(decoded, StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(decodedStr);

                String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                url = url.substring(ItemUtil.TEXTURES_HOST.length());

                config.set(path + ".SkinURL", url);
                config.remove(path + ".Head_Texture");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (config.contains(path + ".Durability")) {
            int oldDurability = config.getInt(path + ".Durability");
            config.set(path + ".Durabilities.Damage", oldDurability);
            config.remove(path + ".Durability");
        }

        if (config.contains(path + ".Unbreakable")) {
            boolean oldUnbreakable = config.getBoolean(path + ".Unbreakable");
            config.set(path + ".Durabilities.Unbreakable", oldUnbreakable);
            config.remove(path + ".Unbreakable");
        }

        if (config.contains(path + ".Name")) {
            String oldName = config.getString(path + ".Name", "null");
            config.set(path + ".Display_Name", oldName);
            config.remove(path + ".Name");
        }

        if (config.contains(path + ".Enchants")) {
            config.set(path + ".Enchant_Glint", true);
            config.remove(path + ".Enchants");
        }

        if (config.contains(path + ".Item_Flags")) {
            config.set(path + ".Hide_Components", true);
            config.remove(path + ".Item_Flags");
        }

        if (config.contains(path + ".Custom_Model_Data")) {
            int oldModel = config.getInt(path + ".Custom_Model_Data");
            config.set(path + ".Model.Data", oldModel);
            config.remove(path + ".Custom_Model_Data");
        }
        // -------- UPDATE OLD FIELDS - END --------

        String materialName = config.getString(path + ".Material");

        Material material = BukkitThing.getMaterial(String.valueOf(materialName));
        if (material == null) {
            CoreLogger.error("Invalid material '" + materialName + "'. Caused by '" + config.getFile().getAbsolutePath() + "'.");
            material = Material.AIR;
        }
        NightItem nightItem = new NightItem(material);

        nightItem.setAmount(config.getInt(path + ".Amount", 1));
        nightItem.setItemName(config.getString(path + ".Item_Name"));
        nightItem.setDisplayName(config.getString(path + ".Display_Name"));
        nightItem.setLore(config.getStringList(path + ".Lore"));

        if (material == Material.PLAYER_HEAD) {
            nightItem.setSkinURL(config.getString(path + ".SkinURL"));
        }
        if (config.contains(path + ".Model.Data")) {
            nightItem.setModelData(config.getInt(path + ".Model.Data"));
        }
        if (config.contains(path + ".Model.Path")) {
            String packPath = config.getString(path + ".Model.Path");
            NamespacedKey key = packPath == null ? null : NamespacedKey.minecraft(packPath);
            nightItem.setModelPath(key);
        }
        if (config.contains(path + ".Tooltip.Style")) {
            String packPath = config.getString(path + ".Tooltip.Style");
            NamespacedKey key = packPath == null ? null : NamespacedKey.minecraft(packPath);
            nightItem.setTooltipStyle(key);
        }

        nightItem.setDamage(config.getInt(path + ".Durabilities.Damage", 0));
        nightItem.setUnbreakable(config.getBoolean(path + ".Durabilities.Unbreakable", false));

        nightItem.setEnchantGlint(config.getBoolean(path + ".Enchant_Glint", false));
        nightItem.setHideComponents(config.getBoolean(path + ".Hide_Components", false));
        nightItem.setHideTooltip(config.getBoolean(path + ".Hide_Tooltip", false));

        String rawColor = config.getString(path + ".Color");
        Color color = rawColor == null ? null : StringUtil.getColor(rawColor);
        nightItem.setColor(color);

        return nightItem;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Material", BukkitThing.toString(this.material));
        config.set(path + ".Amount", this.amount == 1 ? null : this.amount);
        config.set(path + ".Item_Name", this.itemName);
        config.set(path + ".Display_Name", this.displayName);
        config.set(path + ".Lore", this.lore);
        config.set(path + ".SkinURL", this.skinURL);
        config.set(path + ".Model.Data", this.modelData);
        config.set(path + ".Model.Path", this.modelPath == null ? null : this.modelPath.getKey());
        config.set(path + ".Tooltip.Style", this.tooltipStyle == null ? null : this.tooltipStyle.getKey());
        config.set(path + ".Durabilities.Damage", this.damage == 0 ? null : this.damage);
        config.set(path + ".Durabilities.Unbreakable", this.unbreakable ? true : null);
        config.set(path + ".Enchant_Glint", this.enchantGlint ? true : null);
        config.set(path + ".Hide_Components", this.hideComponents ? true : null);
        config.set(path + ".Hide_Tooltip", this.hideTooltip ? true : null);
        config.set(path + ".Color", this.color == null ? null : color.getRed() + "," + color.getBlue() + "," + color.getGreen());
    }

    @NotNull
    public ItemStack getPlain() {
        return this.getPlain(null);
    }

    @NotNull
    public ItemStack getPlain(@Nullable Replacer replacer) {
        return this.build(false, replacer);
    }

    @NotNull
    public ItemStack getTranslated() {
        return this.getTranslated(null);
    }

    @NotNull
    public ItemStack getTranslated(@Nullable Replacer replacer) {
        return this.build(true, replacer);
    }

//    @NotNull
//    private ItemStack build(boolean legacy, @NotNull Consumer<Replacer> consumer) {
//        Replacer replacer = new Replacer();
//        consumer.accept(replacer);
//
//        return this.build(legacy, replacer);
//    }

    @NotNull
    private ItemStack build(boolean legacy, @Nullable Replacer replacer) {
        ItemStack itemStack = new ItemStack(this.material);

        if (this.skinURL != null) ItemUtil.setHeadSkin(itemStack, this.skinURL);

        ItemUtil.editMeta(itemStack, meta -> {
            String replacedItemName = replacer == null || this.itemName == null ? this.itemName : replacer.apply(this.itemName);
            String replacedDisplayName = replacer == null || this.displayName == null ? this.displayName : replacer.apply(this.displayName);
            List<String> replacedLore = replacer == null || this.lore == null ? this.lore : replacer.apply(this.lore);

            meta.setDisplayName(replacedDisplayName == null ? null : (legacy ? NightMessage.asLegacy(replacedDisplayName) : replacedDisplayName));
            meta.setLore(replacedLore == null ? null : (legacy ? NightMessage.asLegacy(replacedLore) : replacedLore));
            meta.setCustomModelData(this.modelData);
            meta.setUnbreakable(this.unbreakable);

            if (this.hideComponents) ItemUtil.hideAttributes(meta, this.material);

            if (Version.isAtLeast(Version.MC_1_21)) {
                meta.setItemName(replacedItemName == null ? null : (legacy ? NightMessage.asLegacy(replacedItemName) : replacedItemName));
                if (this.enchantGlint) meta.setEnchantmentGlintOverride(true);
                meta.setHideTooltip(this.hideTooltip);
            }
            if (Version.isAtLeast(Version.MC_1_21_3)) {
                meta.setItemModel(this.modelPath);
                meta.setTooltipStyle(this.tooltipStyle);
            }

            if (meta instanceof Damageable damageable) {
                damageable.setDamage(this.damage);
            }

            switch (meta) {
                case LeatherArmorMeta armorMeta -> armorMeta.setColor(this.color);
                case PotionMeta potionMeta -> potionMeta.setColor(this.color);
                default -> {}
            }
        });

        return itemStack;
    }

    @NotNull
    public NightItem removeNameAndLore() {
        this.setItemName(null);
        this.setDisplayName(null);
        this.setLore(null);
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public NightItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public NightItem setAmount(int amount) {
        this.amount = Math.max(1, Math.min(64, amount));
        return this;
    }

    public int getDamage() {
        return damage;
    }

    public NightItem setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    @Nullable
    public String getItemName() {
        return this.itemName;
    }

    public NightItem setItemName(@Nullable String itemName) {
        this.itemName = itemName;
        return this;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    public NightItem setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.lore;
    }

    public NightItem setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public NightItem setSkinURL(@Nullable String skinURL) {
        this.skinURL = skinURL;
        return this;
    }

    @Nullable
    public Integer getModelData() {
        return this.modelData;
    }

    public NightItem setModelData(@Nullable Integer modelData) {
        this.modelData = modelData;
        return this;
    }

    @Nullable
    public NamespacedKey getModelPath() {
        return this.modelPath;
    }

    public NightItem setModelPath(@Nullable NamespacedKey modelPath) {
        this.modelPath = modelPath;
        return this;
    }

    @Nullable
    public NamespacedKey getTooltipStyle() {
        return this.tooltipStyle;
    }

    public NightItem setTooltipStyle(@Nullable NamespacedKey tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public NightItem setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public NightItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isEnchantGlint() {
        return enchantGlint;
    }

    public NightItem setEnchantGlint(boolean enchantGlint) {
        this.enchantGlint = enchantGlint;
        return this;
    }

    public boolean isHideComponents() {
        return hideComponents;
    }

    public NightItem setHideComponents(boolean hideComponents) {
        this.hideComponents = hideComponents;
        return this;
    }

    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    public NightItem setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
        return this;
    }
}
