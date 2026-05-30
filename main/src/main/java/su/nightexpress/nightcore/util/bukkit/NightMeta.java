package su.nightexpress.nightcore.util.bukkit;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;

public class NightMeta implements Writeable {

    private String                    itemName;
    private String                    displayName;
    private List<String>              lore;
    private Map<Enchantment, Integer> enchants;
    private Set<String>               hiddenComponents;

    private Integer damage;
    private String  skinUrl;
    private Color   color;

    private Float         modelData;
    private NamespacedKey modelPath;
    private NamespacedKey tooltipStyle;

    private boolean unbreakable;
    private boolean enchantGlint;
    private boolean hideTooltip;

    private Replacer           replacer;
    private PlaceholderContext placeholderContext;

    public NightMeta() {

    }

    @NonNull
    public NightMeta copy() {
        return new NightMeta()
            .setDamage(this.damage)
            .setUnbreakable(this.unbreakable)
            .setItemName(this.itemName)
            .setDisplayName(this.displayName)
            .setLore(this.lore == null ? null : new ArrayList<>(this.lore))
            .setEnchants(this.enchants)
            //.setSkinURL(this.skinURL)
            .setSkinURL(this.skinUrl)
            .setColor(this.color)
            .setCustomModelData(this.modelData)
            .setModelPath(this.modelPath)
            .setTooltipStyle(this.tooltipStyle)
            .setEnchantGlint(this.enchantGlint)
            .setHiddenComponents(this.hiddenComponents)
            .setHideTooltip(this.hideTooltip)
            .setReplacer(this.replacer == null ? null : new Replacer(this.replacer))
            .setPlaceholderContext(this.placeholderContext);
    }

    @NonNull
    @Deprecated
    public NightMeta inherit(@NonNull NightMeta other) {
        return this
            .setDamage(other.damage)
            .setUnbreakable(other.unbreakable)
            .setItemName(other.itemName)
            .setDisplayName(other.displayName)
            .setLore(other.lore == null ? null : new ArrayList<>(other.lore))
            .setEnchants(other.enchants)
            //.setSkinURL(other.skinURL)
            .setSkinURL(other.skinUrl)
            .setColor(other.color)
            .setCustomModelData(other.modelData)
            .setModelPath(other.modelPath)
            .setTooltipStyle(other.tooltipStyle)
            .setEnchantGlint(other.enchantGlint)
            .setHiddenComponents(other.hiddenComponents)
            .setHideTooltip(other.hideTooltip)
            .setReplacer(other.replacer == null ? null : new Replacer(other.replacer));
    }

    @NonNull
    public static NightMeta fromItemStack(@NonNull ItemStack itemStack) {
        NightMeta displayMeta = new NightMeta();

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return displayMeta;

        if (meta instanceof Damageable damageable && damageable.getDamage() != 0) {
            displayMeta.setDamage(damageable.getDamage());
        }

        displayMeta.setDisplayName(ItemUtil.getCustomNameSerialized(meta));
        displayMeta.setItemName(ItemUtil.getItemNameSerialized(meta));
        displayMeta.setLore(ItemUtil.getLoreSerialized(meta));
        displayMeta.setEnchants(meta.getEnchants());
        displayMeta.setUnbreakable(meta.isUnbreakable());
        displayMeta.setPlayerProfile(ItemUtil.getOwnerProfile(itemStack));
        displayMeta.setCustomModelData(ItemUtil.getCustomModelData(meta));
        displayMeta.setEnchantGlint((meta.hasEnchantmentGlintOverride() && meta
            .getEnchantmentGlintOverride())/* || meta.hasEnchants()*/);
        displayMeta.setHideTooltip(meta.isHideTooltip());
        displayMeta.setModelPath(meta.getItemModel());
        displayMeta.setTooltipStyle(meta.getTooltipStyle());
        displayMeta.setHiddenComponents(Software.get().getHiddenComponents(itemStack));

        switch (meta) {
            case LeatherArmorMeta armorMeta -> displayMeta.setColor(armorMeta.getColor());
            case PotionMeta potionMeta -> displayMeta.setColor(potionMeta.getColor());
            case FireworkEffectMeta effectMeta -> {
                FireworkEffect effect = effectMeta.getEffect();
                if (effect != null && !effect.getColors().isEmpty()) {
                    displayMeta.setColor(effect.getColors().getFirst());
                }
            }
            default -> {
            }
        }

        return displayMeta;
    }

    @NonNull
    public static NightMeta read(@NonNull FileConfig config, @NonNull String path) {
        // -------- UPDATE OLD FIELDS - START --------
        String headTexture = config.getString(path + ".Head_Texture");
        if (headTexture != null && !headTexture.isEmpty()) {
            try {
                byte[] decoded = Base64.getDecoder().decode(headTexture);
                String decodedStr = new String(decoded, StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(decodedStr);

                String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url")
                    .getAsString();
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

        String skinUrl = config.getString(path + ".SkinURL");
        if (skinUrl != null) {
            displayMeta.setSkinURL(skinUrl);
        }

        Map<Enchantment, Integer> enchants = new HashMap<>();
        config.getSection(path + ".Enchants").forEach(sId -> {
            Enchantment enchantment = BukkitThing.getEnchantment(sId);
            if (enchantment == null) return;

            int level = config.getInt(path + ".Enchants." + sId);
            enchants.put(enchantment, level);
        });
        displayMeta.setEnchants(enchants);

        if (config.contains(path + ".Model.Data")) {
            displayMeta.setCustomModelData((float) config.getDouble(path + ".Model.Data"));
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
        displayMeta.setHideTooltip(config.getBoolean(path + ".Hide_Tooltip", false));

        if (config.getBoolean(path + ".Hide_Components", false)) {
            displayMeta.setHiddenComponents(Software.get().getCommonComponentsToHide());
        }

        String rawColor = config.getString(path + ".Color");
        Color color = rawColor == null ? null : StringUtil.getColor(rawColor);
        displayMeta.setColor(color);

        return displayMeta;
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.remove(path + ".Enchants");

        config.set(path + ".Item_Name", this.itemName);
        config.set(path + ".Display_Name", this.displayName);
        config.set(path + ".Lore", this.lore);
        if (this.enchants != null) {
            this.enchants.forEach((enchantment, level) -> config.set(path + ".Enchants." + BukkitThing.getAsString(
                enchantment), level));
        }
        config.set(path + ".SkinURL", this.skinUrl);
        config.set(path + ".Model.Data", this.modelData);
        config.set(path + ".Model.Path", this.modelPath == null ? null : this.modelPath.getKey());
        config.set(path + ".Tooltip.Style", this.tooltipStyle == null ? null : this.tooltipStyle.getKey());
        config.set(path + ".Durabilities.Damage", this.damage);
        config.set(path + ".Durabilities.Unbreakable", this.unbreakable ? true : null);
        config.set(path + ".Enchant_Glint", this.enchantGlint ? true : null);
        config.set(path + ".Hide_Components", this.hiddenComponents != null && !this.hiddenComponents
            .isEmpty() ? true : null);
        config.set(path + ".Hide_Tooltip", this.hideTooltip ? true : null);
        config.set(path + ".Color", this.color == null ? null : color.getRed() + "," + color.getGreen() + "," + color
            .getBlue());
    }

    public void apply(@NonNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, meta -> {
            if (meta instanceof SkullMeta skullMeta && this.skinUrl != null) {
                PlayerProfiles.createStaticTexturedProfile(this.skinUrl).apply(skullMeta);
            }

            if (this.displayName != null) {
                String name;

                if (this.placeholderContext != null) {
                    name = this.placeholderContext.apply(this.displayName);
                }
                else name = this.replacer == null ? this.displayName : this.replacer.apply(this.displayName);

                ItemUtil.setCustomName(meta, name);
            }
            if (this.itemName != null) {
                String name;

                if (this.placeholderContext != null) {
                    name = this.placeholderContext.apply(this.itemName);
                }
                else name = this.replacer == null ? this.itemName : this.replacer.apply(this.itemName);

                ItemUtil.setItemName(meta, name);
            }
            if (this.lore != null) {
                List<String> lore;

                if (this.placeholderContext != null) {
                    lore = this.placeholderContext.apply(this.lore);
                }
                else lore = this.replacer == null ? this.lore : this.replacer.apply(this.lore);

                ItemUtil.setLore(meta, this.addEmptyLines(lore));
            }
            if (this.modelData != null) {
                ItemUtil.setCustomModelData(meta, this.modelData);
            }
            if (this.enchants != null) {
                meta.getEnchants().keySet().forEach(meta::removeEnchant);
                this.enchants.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
            }

            meta.setUnbreakable(this.unbreakable);

            if (this.enchantGlint) meta.setEnchantmentGlintOverride(true);
            if (this.hideTooltip) meta.setHideTooltip(true);
            if (this.modelPath != null) meta.setItemModel(this.modelPath);
            if (this.tooltipStyle != null) meta.setTooltipStyle(this.tooltipStyle);

            if (this.damage != null && meta instanceof Damageable damageable) {
                damageable.setDamage(this.damage);
            }

            if (this.color != null) {
                switch (meta) {
                    case LeatherArmorMeta armorMeta -> armorMeta.setColor(this.color);
                    case PotionMeta potionMeta -> potionMeta.setColor(this.color);
                    case FireworkEffectMeta effectMeta -> effectMeta.setEffect(FireworkEffect.builder().withColor(
                        this.color).build());
                    default -> {
                    }
                }
            }
        });

        if (this.hiddenComponents != null && !this.hiddenComponents.isEmpty() && !this.hideTooltip) {
            Software.get().hideComponents(itemStack, this.hiddenComponents);
        }
    }

    @NonNull
    private List<String> addEmptyLines(@NonNull List<String> lore) {
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

    private boolean isEmpty(@NonNull String line) {
        return line.isBlank() || line.equalsIgnoreCase(Placeholders.EMPTY_IF_ABOVE) || line.equalsIgnoreCase(
            Placeholders.EMPTY_IF_BELOW);
    }

    @NonNull
    @Deprecated
    public NightMeta localized(@NonNull LangItem langItem) {
        this.setDisplayName(langItem.getLocalizedName());
        this.setLore(langItem.getLocalizedLore());
        return this;
    }

    @NonNull
    @Deprecated
    public NightMeta localized(@NonNull LangUIButton locale) {
        boolean formatted = locale.isFormatted();

        String name = formatted ? CoreLang.EDITOR_BUTTON_NAME.getString().replace(Placeholders.GENERIC_NAME, locale
            .getName()) : locale.getName();
        List<String> lore = new ArrayList<>();

        locale.getCurrentInfo().forEach((title, value) -> {
            lore.add(CoreLang.EDITOR_BUTTON_CURRENT_INFO.getString()
                .replace(Placeholders.GENERIC_NAME, title)
                .replace(Placeholders.GENERIC_VALUE, value));
        });
        lore.add(Placeholders.EMPTY_IF_ABOVE);

        for (String entry : locale.getDescription()) {
            lore.add(formatted ? CoreLang.EDITOR_BUTTON_DESCRIPTION.getString().replace(Placeholders.GENERIC_ENTRY,
                entry) : entry);
        }
        lore.add(Placeholders.EMPTY_IF_BELOW);

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

    @NonNull
    public NightMeta localized(@NonNull IconLocale locale) {
        this.setDisplayName(locale.getName());
        this.setLore(locale.getLore());
        return this;
    }

    @NonNull
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

    @NonNull
    public Set<String> getHiddenComponents() {
        return this.hiddenComponents;
    }

    public NightMeta setHiddenComponents(@Nullable Set<String> hiddenComponents) {
        this.hiddenComponents = hiddenComponents;
        return this;
    }

    public NightMeta addHiddenComponent(@NonNull String component) {
        this.hiddenComponents.add(component);
        return this;
    }

    public NightMeta hideAllComponents() {
        this.setHiddenComponents(Software.get().getCommonComponentsToHide());
        return this;
    }

    public NightMeta showAllComponents() {
        this.setHiddenComponents(null);
        return this;
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public NightMeta setSkullOwner(@Nullable OfflinePlayer owner) {
        return this.setPlayerProfile(owner == null ? null : Players.getProfile(owner));
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public NightMeta setSkullOwner(@Nullable NightProfile skullOwner) {
        return this.setPlayerProfile(skullOwner);
    }


    @Deprecated(forRemoval = true)
    public @Nullable CachedProfile getPlayerProfile() {
        return this.skinUrl == null ? null : PlayerProfiles.createProfileBySkinURL(this.skinUrl);
    }

    @Deprecated(forRemoval = true)
    public @NonNull NightMeta setPlayerProfile(@Nullable CachedProfile profile) {
        if (profile == null) {
            return this.setSkinURL((URL) null);
        }

        return this.setPlayerProfile(profile.query());
    }

    @Deprecated(forRemoval = true)
    public @NonNull NightMeta setProfileBySkinURL(@NonNull String skinUrl) {
        return this.setSkinURL(skinUrl);
    }


    @Deprecated(forRemoval = true)
    public @NonNull NightMeta setPlayerProfile(@NonNull OfflinePlayer player) {
        return this.setPlayerProfile(PlayerProfiles.getProfile(player));
    }

    public @NonNull NightMeta setPlayerProfile(@Nullable NightProfile profile) {
        if (profile == null) {
            return this.setSkinURL((URL) null);
        }

        return this.setSkinURL(profile.getTextures().getSkin());
    }

    public @NonNull NightMeta setSkinURL(@Nullable URL skinUrl) {
        return this.setSkinURL(skinUrl == null ? null : skinUrl.toString());
    }

    public @NonNull NightMeta setSkinURL(@Nullable String skinUrl) {
        this.skinUrl = skinUrl;
        return this;
    }

    public @Nullable String getSkinURL() {
        return this.skinUrl;
    }

    @Nullable
    @Deprecated
    public Integer getModelData() {
        return this.modelData == null ? null : this.modelData.intValue();
    }

    @Deprecated
    public NightMeta setModelData(@Nullable Integer modelData) {
        return this.setCustomModelData(modelData == null ? null : modelData.floatValue());
    }

    @Nullable
    public Float getCustomModelData() {
        return this.modelData;
    }

    @NonNull
    public NightMeta setCustomModelData(@Nullable Float modelData) {
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

    @Deprecated(forRemoval = true)
    public boolean isHideComponents() {
        return !this.hiddenComponents.isEmpty();// hideComponents;
    }

    @Deprecated(forRemoval = true)
    public NightMeta setHideComponents(boolean hideComponents) {
        return hideComponents ? this.hideAllComponents() : this.showAllComponents();
    }

    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    public NightMeta setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
        return this;
    }

    public NightMeta replaceNameWithLore(UnaryOperator<String> operator) {
        if (this.itemName != null) {
            this.itemName = operator.apply(this.itemName);
        }
        if (this.displayName != null) {
            this.displayName = operator.apply(this.displayName);
        }
        if (this.lore != null) {
            this.lore = Lists.modify(this.lore, operator);
        }
        return this;
    }

    @Nullable
    public Replacer getReplacer() {
        return this.replacer;
    }

    public NightMeta setReplacer(@Nullable Replacer replacer) {
        this.replacer = replacer;
        return this;
    }

    @NonNull
    public NightMeta replacement(@NonNull Consumer<Replacer> consumer) {
        if (this.replacer == null) this.replacer = Replacer.create();

        consumer.accept(this.replacer);
        return this;
    }

    @Nullable
    public PlaceholderContext getPlaceholderContext() {
        return this.placeholderContext;
    }

    @NonNull
    public NightMeta setPlaceholderContext(@Nullable PlaceholderContext placeholderContext) {
        this.placeholderContext = placeholderContext;
        return this;
    }

    @NonNull
    public NightMeta replace(@NonNull Consumer<PlaceholderContext.Builder> consumer) {
        PlaceholderContext.Builder builder = PlaceholderContext.builder();

        consumer.accept(builder);
        return this.setPlaceholderContext(builder.build());
    }
}
