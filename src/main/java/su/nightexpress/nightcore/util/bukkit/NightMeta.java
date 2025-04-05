package su.nightexpress.nightcore.util.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class NightMeta implements Writeable {

    private String                    itemName;
    private String                    displayName;
    private List<String>              lore;
    private Map<Enchantment, Integer> enchants;

    private Integer    damage;
    private String skinURL;
    private PlayerProfile skullOwner;
    private Color  color;

    private Integer       modelData;
    private NamespacedKey modelPath;
    private NamespacedKey tooltipStyle;

    private boolean unbreakable;
    private boolean enchantGlint;
    private boolean hideComponents;
    private boolean hideTooltip;

    private Replacer replacer;

    public NightMeta() {

    }

    @NotNull
    public NightMeta copy() {
        return new NightMeta()
            .setDamage(this.damage)
            .setUnbreakable(this.unbreakable)
            .setItemName(this.itemName)
            .setDisplayName(this.displayName)
            .setLore(this.lore == null ? null : new ArrayList<>(this.lore))
            .setEnchants(this.enchants)
            .setSkinURL(this.skinURL)
            .setSkullOwner(this.skullOwner)
            .setColor(this.color)
            .setModelData(this.modelData)
            .setModelPath(this.modelPath)
            .setTooltipStyle(this.tooltipStyle)
            .setEnchantGlint(this.enchantGlint)
            .setHideComponents(this.hideComponents)
            .setHideTooltip(this.hideTooltip)
            .setReplacer(this.replacer == null ? null : new Replacer(this.replacer));
    }

    @NotNull
    public NightMeta inherit(@NotNull NightMeta other) {
        return this
            .setDamage(other.damage)
            .setUnbreakable(other.unbreakable)
            .setItemName(other.itemName)
            .setDisplayName(other.displayName)
            .setLore(other.lore == null ? null : new ArrayList<>(other.lore))
            .setEnchants(other.enchants)
            .setSkinURL(other.skinURL)
            .setSkullOwner(other.skullOwner)
            .setColor(other.color)
            .setModelData(other.modelData)
            .setModelPath(other.modelPath)
            .setTooltipStyle(other.tooltipStyle)
            .setEnchantGlint(other.enchantGlint)
            .setHideComponents(other.hideComponents)
            .setHideTooltip(other.hideTooltip)
            .setReplacer(other.replacer == null ? null : new Replacer(other.replacer));
    }

    @NotNull
    public static NightMeta fromItemStack(@NotNull ItemStack itemStack) {
        NightMeta displayMeta = new NightMeta();

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return displayMeta;

        if (meta instanceof Damageable damageable && damageable.getDamage() != 0) {
            displayMeta.setDamage(damageable.getDamage());
        }
        if (meta instanceof SkullMeta skullMeta) {
            displayMeta.setSkullOwner(skullMeta.getOwnerProfile());
        }

        displayMeta.setDisplayName(meta.hasDisplayName() ? ItemUtil.getSerializedDisplayName(meta)/*meta.getDisplayName()*/ : null);
        displayMeta.setLore(ItemUtil.getSerializedLore(meta));
        displayMeta.setEnchants(meta.getEnchants());
        displayMeta.setUnbreakable(meta.isUnbreakable());
        displayMeta.setSkinURL(ItemUtil.getHeadSkin(itemStack));
        displayMeta.setModelData(meta.hasCustomModelData() ? meta.getCustomModelData() : null);
        if (Version.isAtLeast(Version.MC_1_21)) {
            displayMeta.setItemName(meta.hasItemName() ? ItemUtil.getSerializedItemName(meta)/*meta.getItemName()*/ : null);
            displayMeta.setEnchantGlint((meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride())/* || meta.hasEnchants()*/);
            displayMeta.setHideTooltip(meta.isHideTooltip());
        }
        if (Version.isAtLeast(Version.MC_1_21_3)) {
            displayMeta.setModelPath(meta.getItemModel());
            displayMeta.setTooltipStyle(meta.getTooltipStyle());
        }
        displayMeta.setHideComponents(!meta.getItemFlags().isEmpty());

        if (meta instanceof LeatherArmorMeta armorMeta) {
            displayMeta.setColor(armorMeta.getColor());
        }
        else if (meta instanceof PotionMeta potionMeta) {
            displayMeta.setColor(potionMeta.getColor());
        }

        return displayMeta;
    }

    @NotNull
    public static NightMeta read(@NotNull FileConfig config, @NotNull String path) {
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
            String oldName = config.getString(path + ".Name");
            config.set(path + ".Display_Name", oldName);
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

        NightMeta displayMeta = new NightMeta();

        displayMeta.setItemName(config.getString(path + ".Item_Name"));
        displayMeta.setDisplayName(config.getString(path + ".Display_Name"));
        displayMeta.setLore(config.getStringList(path + ".Lore"));
        displayMeta.setSkinURL(config.getString(path + ".SkinURL"));

        Map<Enchantment, Integer> enchants = new HashMap<>();
        config.getSection(path + ".Enchants").forEach(sId -> {
            Enchantment enchantment = BukkitThing.getEnchantment(sId);
            if (enchantment == null) return;

            int level = config.getInt(path + ".Enchants." + sId);
            enchants.put(enchantment, level);
        });
        displayMeta.setEnchants(enchants);

        if (config.contains(path + ".Model.Data")) {
            displayMeta.setModelData(config.getInt(path + ".Model.Data"));
        }
        if (config.contains(path + ".Model.Path")) {
            String packPath = config.getString(path + ".Model.Path");
            NamespacedKey key = packPath == null ? null : NamespacedKey.fromString(packPath, null);
            displayMeta.setModelPath(key);
        }
        if (config.contains(path + ".Tooltip.Style")) {
            String packPath = config.getString(path + ".Tooltip.Style");
            NamespacedKey key = packPath == null ? null : NamespacedKey.fromString(packPath, null);
            displayMeta.setTooltipStyle(key);
        }

        if (config.contains(path + ".Durabilities.Damage")) {
            displayMeta.setDamage(config.getInt(path + ".Durabilities.Damage", 0));
        }
        displayMeta.setUnbreakable(config.getBoolean(path + ".Durabilities.Unbreakable", false));

        displayMeta.setEnchantGlint(config.getBoolean(path + ".Enchant_Glint", false));
        displayMeta.setHideComponents(config.getBoolean(path + ".Hide_Components", false));
        displayMeta.setHideTooltip(config.getBoolean(path + ".Hide_Tooltip", false));

        String rawColor = config.getString(path + ".Color");
        Color color = rawColor == null ? null : StringUtil.getColor(rawColor);
        displayMeta.setColor(color);

        return displayMeta;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.remove(path + ".Enchants");

        config.set(path + ".Item_Name", /*this.itemName == null || this.itemName.isBlank() ? null :*/ this.itemName);
        config.set(path + ".Display_Name", /*this.displayName == null || this.displayName.isBlank() ? null :*/ this.displayName);
        config.set(path + ".Lore", this.lore);
        if (this.enchants != null) {
            this.enchants.forEach((enchantment, level) -> config.set(path + ".Enchants." + BukkitThing.toString(enchantment), level));
        }
        config.set(path + ".SkinURL", this.skinURL);
        config.set(path + ".Model.Data", this.modelData);
        config.set(path + ".Model.Path", this.modelPath == null ? null : this.modelPath.getKey());
        config.set(path + ".Tooltip.Style", this.tooltipStyle == null ? null : this.tooltipStyle.getKey());
        config.set(path + ".Durabilities.Damage", this.damage);
        config.set(path + ".Durabilities.Unbreakable", this.unbreakable ? true : null);
        config.set(path + ".Enchant_Glint", this.enchantGlint ? true : null);
        config.set(path + ".Hide_Components", this.hideComponents ? true : null);
        config.set(path + ".Hide_Tooltip", this.hideTooltip ? true : null);
        config.set(path + ".Color", this.color == null ? null : color.getRed() + "," + color.getBlue() + "," + color.getGreen());
    }

    public void apply(@NotNull ItemStack itemStack/*, boolean legacy*/) {
        ItemUtil.editMeta(itemStack, meta -> {
            if (this.displayName != null) {
                String name = this.replacer == null ? this.displayName : this.replacer.apply(this.displayName);
                ItemUtil.setDisplayName(meta, name);
                //meta.setDisplayName(legacy ? NightMessage.asLegacy(name) : name);
            }
            if (this.itemName != null && Version.isAtLeast(Version.MC_1_21)) {
                String name = this.replacer == null ? this.itemName : this.replacer.apply(this.itemName);
                ItemUtil.setItemName(meta, name);
                //meta.setItemName(name == null ? null : (legacy ? NightMessage.asLegacy(name) : name));
            }
            if (this.lore != null) {
                List<String> lore = this.replacer == null ? this.lore : this.replacer.apply(this.lore);
                ItemUtil.setLore(meta, this.addEmptyLines(lore));
                //meta.setLore(legacy ? NightMessage.asLegacy(this.addEmptyLines(lore)) : this.addEmptyLines(lore));
            }
            if (this.modelData != null) {
                meta.setCustomModelData(this.modelData);
            }
            if (this.enchants != null) {
                meta.getEnchants().keySet().forEach(meta::removeEnchant);
                this.enchants.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
            }

            meta.setUnbreakable(this.unbreakable);

//            if (this.hideComponents) {
//                ItemUtil.hideAttributes(meta, itemStack.getType());
//            }

            if (Version.isAtLeast(Version.MC_1_21)) {
                if (this.enchantGlint) meta.setEnchantmentGlintOverride(true);
                if (this.hideTooltip) meta.setHideTooltip(true);
            }
            if (Version.isAtLeast(Version.MC_1_21_3)) {
                if (this.modelPath != null) meta.setItemModel(this.modelPath);
                if (this.tooltipStyle != null) meta.setTooltipStyle(this.tooltipStyle);
            }

            if (this.damage != null && meta instanceof Damageable damageable) {
                damageable.setDamage(this.damage);
            }
            if (meta instanceof SkullMeta skullMeta) {
                PlayerProfile profile = this.skinURL != null ? ItemUtil.createSkinProfile(this.skinURL) : this.skullOwner;
                skullMeta.setOwnerProfile(profile);
            }

            if (this.color != null) {
                switch (meta) {
                    case LeatherArmorMeta armorMeta -> armorMeta.setColor(this.color);
                    case PotionMeta potionMeta -> potionMeta.setColor(this.color);
                    default -> {}
                }
            }
        });

        if (this.hideComponents && !this.hideTooltip) {
            ItemUtil.hideAttributes(itemStack);
        }
    }

    @NotNull
    private List<String> addEmptyLines(@NotNull List<String> lore) {
        for (int index = 0; index < lore.size(); index++) {
            String line = lore.get(index);
            if (line.equalsIgnoreCase(Placeholders.EMPTY_IF_ABOVE)) {
                if (index == 0 || this.isEmpty(lore.get(index - 1))) {
                    lore.remove(index);
                }
                else lore.set(index, "");

                return addEmptyLines(lore);
            }
            else if (line.equalsIgnoreCase(Placeholders.EMPTY_IF_BELOW)) {
                if (index == lore.size() - 1 || this.isEmpty(lore.get(index + 1))) {
                    lore.remove(index);
                }
                else lore.set(index, "");

                return addEmptyLines(lore);
            }
        }

        return lore;
    }

    private boolean isEmpty(@NotNull String line) {
        return line.isBlank() || line.equalsIgnoreCase(Placeholders.EMPTY_IF_ABOVE) || line.equalsIgnoreCase(Placeholders.EMPTY_IF_BELOW);
    }

    @NotNull
    @Deprecated
    public NightMeta localized(@NotNull LangItem langItem) {
        this.setDisplayName(langItem.getLocalizedName());
        this.setLore(langItem.getLocalizedLore());
        return this;
    }

    @NotNull
    public NightMeta localized(@NotNull LangUIButton locale) {
        String name = CoreLang.EDITOR_BUTTON_NAME.getString().replace(Placeholders.GENERIC_NAME, locale.getName());
        List<String> lore = new ArrayList<>();

        locale.getCurrentInfo().forEach((title, value) -> {
            lore.add(CoreLang.EDITOR_BUTTON_CURRENT_INFO.getString()
                .replace(Placeholders.GENERIC_NAME, title)
                .replace(Placeholders.GENERIC_VALUE, value));
        });
        lore.add(Placeholders.EMPTY_IF_ABOVE);

        for (String entry : locale.getDescription()) {
            lore.add(CoreLang.EDITOR_BUTTON_DESCRIPTION.getString().replace(Placeholders.GENERIC_ENTRY, entry));
        }
        lore.add(Placeholders.EMPTY_IF_ABOVE);

        locale.getClickActions().forEach((key, action) -> {
            lore.add(CoreLang.EDITOR_BUTTON_CLICK_KEY.getString()
                .replace(Placeholders.GENERIC_NAME, CoreLang.CLICK_KEY.getLocalized(key))
                .replace(Placeholders.GENERIC_VALUE, action)
            );
        });

        this.setDisplayName(name);
        this.setLore(lore);
        return this;
    }

    @NotNull
    public NightMeta ignoreNameAndLore() {
        this.setItemName(null);
        this.setDisplayName(null);
        this.setLore(null);
        return this;
    }

    @Nullable
    public Integer getDamage() {
        return this.damage;
    }

    public NightMeta setDamage(@Nullable Integer damage) {
        this.damage = damage;
        return this;
    }

    @Nullable
    public String getItemName() {
        return this.itemName;
    }

    public NightMeta setItemName(@Nullable String itemName) {
        this.itemName = itemName;
        return this;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    public NightMeta setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.lore;
    }

    public NightMeta setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }

    @Nullable
    public Map<Enchantment, Integer> getEnchants() {
        return this.enchants;
    }

    public NightMeta setEnchants(@Nullable Map<Enchantment, Integer> enchants) {
        this.enchants = enchants == null ? null : new HashMap<>(enchants);
        return this;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public NightMeta setSkinURL(@Nullable String skinURL) {
        this.skinURL = skinURL;
        return this;
    }

    @Nullable
    public PlayerProfile getSkullOwner() {
        return this.skullOwner;
    }

    @NotNull
    public NightMeta setSkullOwner(@Nullable OfflinePlayer owner) {
        return this.setSkullOwner(owner == null ? null : owner.getPlayerProfile());
    }

    @NotNull
    public NightMeta setSkullOwner(@Nullable PlayerProfile skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    @Nullable
    public Integer getModelData() {
        return this.modelData;
    }

    public NightMeta setModelData(@Nullable Integer modelData) {
        this.modelData = modelData;
        return this;
    }

    @Nullable
    public NamespacedKey getModelPath() {
        return this.modelPath;
    }

    public NightMeta setModelPath(@Nullable NamespacedKey modelPath) {
        this.modelPath = modelPath;
        return this;
    }

    @Nullable
    public NamespacedKey getTooltipStyle() {
        return this.tooltipStyle;
    }

    public NightMeta setTooltipStyle(@Nullable NamespacedKey tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public NightMeta setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public NightMeta setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isEnchantGlint() {
        return enchantGlint;
    }

    public NightMeta setEnchantGlint(boolean enchantGlint) {
        this.enchantGlint = enchantGlint;
        return this;
    }

    public boolean isHideComponents() {
        return hideComponents;
    }

    public NightMeta setHideComponents(boolean hideComponents) {
        this.hideComponents = hideComponents;
        return this;
    }

    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    public NightMeta setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
        return this;
    }

    public NightMeta setReplacer(@Nullable Replacer replacer) {
        this.replacer = replacer;
        return this;
    }

    @NotNull
    public NightMeta replacement(@NotNull Consumer<Replacer> consumer) {
        if (this.replacer == null) this.replacer = Replacer.create();

        consumer.accept(this.replacer);
        return this;
    }
}
