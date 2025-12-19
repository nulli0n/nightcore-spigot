package su.nightexpress.nightcore.util.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NightMeta implements Writeable {

    private String                    itemName;
    private String                    displayName;
    private List<String>              lore;
    private Map<Enchantment, Integer> enchants;
    private Set<String>               hiddenComponents;

    private Integer       damage;
    private CachedProfile playerProfile;
    private Color         color;

    private Float         modelData;
    private NamespacedKey modelPath;
    private NamespacedKey tooltipStyle;

    private boolean unbreakable;
    private boolean enchantGlint;
    private boolean hideTooltip;

    private Replacer replacer;
    private PlaceholderContext placeholderContext;

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
            //.setSkinURL(this.skinURL)
            .setPlayerProfile(this.playerProfile)
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

    @NotNull
    @Deprecated
    public NightMeta inherit(@NotNull NightMeta other) {
        return this
            .setDamage(other.damage)
            .setUnbreakable(other.unbreakable)
            .setItemName(other.itemName)
            .setDisplayName(other.displayName)
            .setLore(other.lore == null ? null : new ArrayList<>(other.lore))
            .setEnchants(other.enchants)
            //.setSkinURL(other.skinURL)
            .setPlayerProfile(other.playerProfile)
            .setColor(other.color)
            .setCustomModelData(other.modelData)
            .setModelPath(other.modelPath)
            .setTooltipStyle(other.tooltipStyle)
            .setEnchantGlint(other.enchantGlint)
            .setHiddenComponents(other.hiddenComponents)
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

        displayMeta.setDisplayName(ItemUtil.getCustomNameSerialized(meta));
        displayMeta.setItemName(ItemUtil.getItemNameSerialized(meta));
        displayMeta.setLore(ItemUtil.getLoreSerialized(meta));
        displayMeta.setEnchants(meta.getEnchants());
        displayMeta.setUnbreakable(meta.isUnbreakable());
        displayMeta.setPlayerProfile(ItemUtil.getOwnerProfile(itemStack));
        displayMeta.setCustomModelData(ItemUtil.getCustomModelData(meta));
        if (Version.isAtLeast(Version.MC_1_21)) {
            displayMeta.setEnchantGlint((meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride())/* || meta.hasEnchants()*/);
            displayMeta.setHideTooltip(meta.isHideTooltip());
        }
        if (Version.isAtLeast(Version.MC_1_21_3)) {
            displayMeta.setModelPath(meta.getItemModel());
            displayMeta.setTooltipStyle(meta.getTooltipStyle());
        }
        if (Version.isAtLeast(Version.MC_1_21_5)) {
            displayMeta.setHiddenComponents(Engine.software().getHiddenComponents(itemStack));
        }
        else {
            displayMeta.setHiddenComponents(meta.getItemFlags().stream().map(Enum::name).collect(Collectors.toSet()));
        }

        switch (meta) {
            case LeatherArmorMeta armorMeta -> displayMeta.setColor(armorMeta.getColor());
            case PotionMeta potionMeta -> displayMeta.setColor(potionMeta.getColor());
            case FireworkEffectMeta effectMeta -> {
                FireworkEffect effect = effectMeta.getEffect();
                if (effect != null && !effect.getColors().isEmpty()) {
                    displayMeta.setColor(effect.getColors().getFirst());
                }
            }
            default -> {}
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

        String skinURL = config.getString(path + ".SkinURL");
        if (skinURL != null) {
            displayMeta.setProfileBySkinURL(skinURL);
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
            displayMeta.setHiddenComponents(Engine.software().getCommonComponentsToHide());
        }

        String rawColor = config.getString(path + ".Color");
        Color color = rawColor == null ? null : StringUtil.getColor(rawColor);
        displayMeta.setColor(color);

        return displayMeta;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.remove(path + ".Enchants");

        config.set(path + ".Item_Name", this.itemName);
        config.set(path + ".Display_Name", this.displayName);
        config.set(path + ".Lore", this.lore);
        if (this.enchants != null) {
            this.enchants.forEach((enchantment, level) -> config.set(path + ".Enchants." + BukkitThing.getAsString(enchantment), level));
        }
        config.set(path + ".SkinURL", this.playerProfile == null ? null : PlayerProfiles.getProfileSkinURL(this.playerProfile.queryNoUpdate()));
        config.set(path + ".Model.Data", this.modelData);
        config.set(path + ".Model.Path", this.modelPath == null ? null : this.modelPath.getKey());
        config.set(path + ".Tooltip.Style", this.tooltipStyle == null ? null : this.tooltipStyle.getKey());
        config.set(path + ".Durabilities.Damage", this.damage);
        config.set(path + ".Durabilities.Unbreakable", this.unbreakable ? true : null);
        config.set(path + ".Enchant_Glint", this.enchantGlint ? true : null);
        config.set(path + ".Hide_Components", this.hiddenComponents != null && !this.hiddenComponents.isEmpty() ? true : null);
        config.set(path + ".Hide_Tooltip", this.hideTooltip ? true : null);
        config.set(path + ".Color", this.color == null ? null : color.getRed() + "," + color.getGreen() + "," + color.getBlue());
    }

    public void apply(@NotNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, meta -> {
            if (meta instanceof SkullMeta skullMeta) {
                if (this.playerProfile != null) this.playerProfile.query().apply(skullMeta);
            }

            if (this.displayName != null) {
                String name;

                if (this.placeholderContext != null) {
                    name = this.placeholderContext.apply(this.displayName);
                }
                else name = this.replacer == null ? this.displayName : this.replacer.apply(this.displayName);

                ItemUtil.setCustomName(meta, name);
            }
            if (this.itemName != null && Version.isAtLeast(Version.MC_1_21)) {
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

            if (this.color != null) {
                switch (meta) {
                    case LeatherArmorMeta armorMeta -> armorMeta.setColor(this.color);
                    case PotionMeta potionMeta -> potionMeta.setColor(this.color);
                    case FireworkEffectMeta effectMeta -> effectMeta.setEffect(FireworkEffect.builder().withColor(this.color).build());
                    default -> {}
                }
            }
        });

        if (this.hiddenComponents != null && !this.hiddenComponents.isEmpty() && !this.hideTooltip) {
            if (Version.isAtLeast(Version.MC_1_21_5)) {
                Engine.software().hideComponents(itemStack, this.hiddenComponents);
            }
            else {
                SpigotBridge.hideComponentsByName(itemStack, this.hiddenComponents);
            }
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
    @Deprecated
    public NightMeta localized(@NotNull LangUIButton locale) {
        boolean formatted = locale.isFormatted();

        String name = formatted ? CoreLang.EDITOR_BUTTON_NAME.getString().replace(Placeholders.GENERIC_NAME, locale.getName()) : locale.getName();
        List<String> lore = new ArrayList<>();

        locale.getCurrentInfo().forEach((title, value) -> {
            lore.add(CoreLang.EDITOR_BUTTON_CURRENT_INFO.getString()
                .replace(Placeholders.GENERIC_NAME, title)
                .replace(Placeholders.GENERIC_VALUE, value));
        });
        lore.add(Placeholders.EMPTY_IF_ABOVE);

        for (String entry : locale.getDescription()) {
            lore.add(formatted ? CoreLang.EDITOR_BUTTON_DESCRIPTION.getString().replace(Placeholders.GENERIC_ENTRY, entry) : entry);
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

    @NotNull
    public NightMeta localized(@NotNull IconLocale locale) {
        this.setDisplayName(locale.getName());
        this.setLore(locale.getLore());
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

    @NotNull
    public Set<String> getHiddenComponents() {
        return this.hiddenComponents;
    }

    public NightMeta setHiddenComponents(@Nullable Set<String> hiddenComponents) {
        this.hiddenComponents = hiddenComponents;
        return this;
    }

    public NightMeta addHiddenComponent(@NotNull String component) {
        this.hiddenComponents.add(component);
        return this;
    }

    public NightMeta hideAllComponents() {
        this.setHiddenComponents(Engine.software().getCommonComponentsToHide());
        return this;
    }

    public NightMeta showAllComponents() {
        this.setHiddenComponents(null);
        return this;
    }

    @Deprecated
    @Nullable
    public String getSkinURL() {
        return this.playerProfile == null ? null : PlayerProfiles.getProfileSkinURL(this.playerProfile.queryNoUpdate());
    }

    @Deprecated
    public NightMeta setSkinURL(@Nullable String skinURL) {
        return skinURL == null ? this.setPlayerProfile((NightProfile) null) : this.setProfileBySkinURL(skinURL);
    }

    @NotNull
    @Deprecated
    public NightMeta setSkullOwner(@Nullable OfflinePlayer owner) {
        return this.setPlayerProfile(owner == null ? null : Players.getProfile(owner));
    }

    @NotNull
    @Deprecated
    public NightMeta setSkullOwner(@Nullable NightProfile skullOwner) {
        return this.setPlayerProfile(skullOwner);
    }

    @Nullable
    public CachedProfile getPlayerProfile() {
        return this.playerProfile;
    }

    @NotNull
    public NightMeta setProfileBySkinURL(@NotNull String skinURL) {
        return this.setPlayerProfile(PlayerProfiles.createProfileBySkinURL(skinURL));
    }

    @NotNull
    public NightMeta setPlayerProfile(@NotNull OfflinePlayer player) {
        return this.setPlayerProfile(PlayerProfiles.getProfile(player));
    }

    @NotNull
    public NightMeta setPlayerProfile(@Nullable NightProfile profile) {
        CachedProfile cached = null;
        if (profile != null && profile.getId() != null) {
            cached = PlayerProfiles.cacheExact(profile); // Do not try to fetch properties of profiles with random UUID and custom textures set.
        }

        return this.setPlayerProfile(cached);
    }

    @NotNull
    public NightMeta setPlayerProfile(@Nullable CachedProfile profile) {
        this.playerProfile = profile;
        return this;
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

    @NotNull
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

    @Deprecated
    public boolean isHideComponents() {
        return !this.hiddenComponents.isEmpty();// hideComponents;
    }

    @Deprecated
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

    @Nullable
    public Replacer getReplacer() {
        return this.replacer;
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

    @Nullable
    public PlaceholderContext getPlaceholderContext() {
        return this.placeholderContext;
    }

    @NotNull
    public NightMeta setPlaceholderContext(@Nullable PlaceholderContext placeholderContext) {
        this.placeholderContext = placeholderContext;
        return this;
    }

    @NotNull
    public NightMeta replace(@NotNull Consumer<PlaceholderContext.Builder> consumer) {
        PlaceholderContext.Builder builder = PlaceholderContext.builder();

        consumer.accept(builder);
        return this.setPlaceholderContext(builder.build());
    }
}
