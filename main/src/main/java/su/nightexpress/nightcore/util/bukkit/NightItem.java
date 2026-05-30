package su.nightexpress.nightcore.util.bukkit;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.profile.CachedProfile;

/**
 * Utility class to create <b>cosmetic</b> items only.<br>
 * Do <b>NOT</b> use to create custom items for regular gameplay.
 */
public class NightItem implements Writeable {

    private final ItemStack backend;
    private final NightMeta meta;

    private Material material;
    private int      amount;

    public NightItem(@NonNull Material material) {
        this(material, 1);
    }

    public NightItem(@NonNull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public NightItem(@NonNull ItemStack itemStack) {
        this(itemStack, NightMeta.fromItemStack(itemStack));
    }

    private NightItem(@NonNull ItemStack itemStack, @NonNull NightMeta meta) {
        this.backend = new ItemStack(itemStack);
        this.meta = meta;

        this.material = itemStack.getType();
        this.amount = itemStack.getAmount();
    }

    @NonNull
    public static NightItem fromType(@NonNull Material material) {
        return new NightItem(material);
    }

    /**
     * Wraps ItemStack as NightItem for further modifications. Retains its original ItemMeta, modifications will
     * override specific components only.
     * 
     * @param itemStack ItemStack to wrap.
     * @return NighItem wrapper backed by the provided ItemStack.
     */
    @NonNull
    public static NightItem fromItemStack(@NonNull ItemStack itemStack) {
        return new NightItem(itemStack);
    }

    /**
     * Creates a new NightItem wrapper for the PLAYER_HEAD ItemStack with provided texture.
     * 
     * @param skinURL Skin texture URL.
     * @return NightItem wrapper backed by the PLAYER_HEAD with custom texture.
     */
    @NonNull
    public static NightItem asCustomHead(@NonNull String skinURL) {
        return new NightItem(Material.PLAYER_HEAD).setSkinURL(skinURL);
    }

    @NonNull
    public static NightItem read(@NonNull FileConfig config, @NonNull String path) {
        String materialName = config.getString(path + ".Material");
        int amount = config.getInt(path + ".Amount", 1);

        Material material = BukkitThing.getMaterial(String.valueOf(materialName));
        if (material == null) {
            Engine.core().error("Invalid material '" + materialName + "'. Found in '" + config.getFile()
                .getAbsolutePath() + "' -> '" + path + "'.");
            material = Material.BARRIER;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        NightMeta displayMeta = NightMeta.read(config, path);

        return new NightItem(itemStack, displayMeta);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Material", BukkitThing.getAsString(this.material));
        config.set(path + ".Amount", this.amount == 1 ? null : this.amount);
        this.meta.write(config, path);
    }

    @NonNull
    public NightItem copy() {
        NightItem copy = new NightItem(this.backend, this.meta.copy());
        copy.setMaterial(this.material);
        copy.setAmount(this.amount);
        return copy;
    }

    @NonNull
    @Deprecated
    public NightItem inherit(@NonNull NightItem other) {
        this.backend.setType(other.getMaterial());
        this.backend.setAmount(other.getAmount());
        this.meta.inherit(other.meta);

        return this;
    }

    /**
     * Quickly wraps NightItem as MenuItem builder.
     * 
     * @return MenuItem builder.
     */

    public MenuItem.@NonNull Builder toMenuItem() {
        return MenuItem.builder(this);
    }

    /**
     * Builds new ItemStack instance.
     * 
     * @return New ItemStack instance.
     */
    @NonNull
    public ItemStack getItemStack() {
        ItemStack stack = Engine.software().setType(this.backend, this.material);

        stack.setAmount(this.amount);

        this.meta.apply(stack);
        return stack;
    }

    @NonNull
    public NightMeta getMeta() {
        return this.meta;
    }

    @NonNull
    public Material getMaterial() {
        return this.material;
    }

    @NonNull
    public NightItem setMaterial(@NonNull Material material) {
        if (!material.isItem()) throw new IllegalStateException("Material " + material.name() + " is not item!");

        this.material = material;
        return this;
    }

    public int getAmount() {
        return this.amount;
    }

    @NonNull
    public NightItem setAmount(int amount) {
        this.amount = NumberUtil.clamp(amount, 1, this.material.getMaxStackSize());
        return this;
    }

    @Nullable
    public Replacer getReplacer() {
        return this.meta.getReplacer();
    }

    @NonNull
    public NightItem setReplacer(@Nullable Replacer replacer) {
        this.meta.setReplacer(replacer);
        return this;
    }

    @NonNull
    public NightItem replacement(@NonNull Consumer<Replacer> consumer) {
        this.meta.replacement(consumer);
        return this;
    }

    @Nullable
    public PlaceholderContext getPlaceholderContext() {
        return this.meta.getPlaceholderContext();
    }

    @NonNull
    public NightItem setPlaceholderContext(@Nullable PlaceholderContext placeholderContext) {
        this.meta.setPlaceholderContext(placeholderContext);
        return this;
    }

    public NightItem replaceNameWithLore(UnaryOperator<String> operator) {
        this.meta.replaceNameWithLore(operator);
        return this;
    }

    @NonNull
    public NightItem replace(@NonNull Consumer<PlaceholderContext.Builder> consumer) {
        this.meta.replace(consumer);
        return this;
    }

    @NonNull
    public NightItem ignoreNameAndLore() {
        this.setItemName(null);
        this.setDisplayName(null);
        this.setLore(null);
        return this;
    }

    @NonNull
    public NightItem ignoreAmount() {
        this.setAmount(1);
        return this;
    }

    @NonNull
    @Deprecated
    public NightItem localized(@NonNull LangItem langItem) {
        this.meta.localized(langItem);
        return this;
    }

    @NonNull
    @Deprecated
    public NightItem localized(@NonNull LangUIButton langUIButton) {
        this.meta.localized(langUIButton);
        return this;
    }

    @NonNull
    public NightItem localized(@NonNull IconLocale locale) {
        this.meta.localized(locale);
        return this;
    }

    @Nullable
    public String getItemName() {
        return this.meta.getItemName();
    }

    @NonNull
    public NightItem setItemName(@Nullable String itemName) {
        this.meta.setItemName(itemName);
        return this;
    }

    @Nullable
    public String getDisplayName() {
        return this.meta.getDisplayName();
    }

    @NonNull
    public NightItem setDisplayName(@Nullable String displayName) {
        this.meta.setDisplayName(displayName);
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.meta.getLore();
    }

    @NonNull
    public NightItem setLore(@Nullable List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    @NonNull
    public NightItem setDamage(@Nullable Integer damage) {
        this.meta.setDamage(damage);
        return this;
    }

    @NonNull
    public NightItem setEnchants(@NonNull Map<Enchantment, Integer> enchants) {
        this.meta.setEnchants(enchants);
        return this;
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public NightItem setSkullOwner(@Nullable OfflinePlayer owner) {
        this.meta.setSkullOwner(owner);
        return this;
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public NightItem setSkullOwner(@Nullable NightProfile skullOwner) {
        this.meta.setSkullOwner(skullOwner);
        return this;
    }


    @Deprecated(forRemoval = true)
    public @Nullable CachedProfile getPlayerProfile() {
        return this.meta.getPlayerProfile();
    }

    @Deprecated(forRemoval = true)
    public @NonNull NightItem setPlayerProfile(@Nullable CachedProfile profile) {
        this.meta.setPlayerProfile(profile);
        return this;
    }

    @Deprecated(forRemoval = true)
    public @NonNull NightItem setProfileBySkinURL(@NonNull String skinUrl) {
        this.meta.setProfileBySkinURL(skinUrl);
        return this;
    }


    @Deprecated(forRemoval = true)
    public @NonNull NightItem setPlayerProfile(@NonNull OfflinePlayer player) {
        this.meta.setPlayerProfile(player);
        return this;
    }

    public @NonNull NightItem setPlayerProfile(@Nullable NightProfile profile) {
        this.meta.setPlayerProfile(profile);
        return this;
    }

    public @NonNull NightItem setSkinURL(@Nullable URL skinUrl) {
        this.meta.setSkinURL(skinUrl);
        return this;
    }

    public @NonNull NightItem setSkinURL(@Nullable String skinUrl) {
        this.meta.setSkinURL(skinUrl);
        return this;
    }

    public @Nullable String getSkinURL() {
        return this.meta.getSkinURL();
    }

    @Nullable
    public Float getCustomModelData() {
        return this.meta.getCustomModelData();
    }

    @NonNull
    public NightItem setCustomModelData(@Nullable Float modelData) {
        this.meta.setCustomModelData(modelData);
        return this;
    }

    @NonNull
    public NightItem setModelPath(@Nullable NamespacedKey modelPath) {
        this.meta.setModelPath(modelPath);
        return this;
    }

    @NonNull
    public NightItem setTooltipStyle(@Nullable NamespacedKey tooltipStyle) {
        this.meta.setTooltipStyle(tooltipStyle);
        return this;
    }

    @NonNull
    public NightItem setColor(@NonNull Color color) {
        this.meta.setColor(color);
        return this;
    }

    @NonNull
    public NightItem setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    @NonNull
    public NightItem setEnchantGlint(boolean enchantGlint) {
        this.meta.setEnchantGlint(enchantGlint);
        return this;
    }

    @NonNull
    @Deprecated
    public NightItem setHideComponents(boolean hideComponents) {
        //this.meta.setHideComponents(hideComponents);
        return hideComponents ? this.hideAllComponents() : this.showAllComponents();
    }

    @NonNull
    public NightItem hideAllComponents() {
        this.meta.hideAllComponents();
        return this;
    }

    @NonNull
    public NightItem showAllComponents() {
        this.meta.showAllComponents();
        return this;
    }

    @NonNull
    public NightItem setHideTooltip(boolean hideTooltip) {
        this.meta.setHideTooltip(hideTooltip);
        return this;
    }
}
