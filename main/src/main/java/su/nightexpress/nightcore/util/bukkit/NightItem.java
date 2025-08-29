package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.profile.CachedProfile;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Utility class to create <b>cosmetic</b> items only.<br>
 * Do <b>NOT</b> use to create custom items for regular gameplay.
 */
public class NightItem implements Writeable {

    private final ItemStack backend;
    private final NightMeta meta;

    private Material material;
    private int amount;

    public NightItem(@NotNull Material material) {
        this(material, 1);
    }

    public NightItem(@NotNull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public NightItem(@NotNull ItemStack itemStack) {
        this(itemStack, NightMeta.fromItemStack(itemStack));
    }

    private NightItem(@NotNull ItemStack itemStack, @NotNull NightMeta meta) {
        this.backend = new ItemStack(itemStack);
        this.meta = meta;

        this.material = itemStack.getType();
        this.amount = itemStack.getAmount();
    }

    @NotNull
    public static NightItem fromType(@NotNull Material material) {
        return new NightItem(material);
    }

    /**
     * Wraps ItemStack as NightItem for further modifications. Retains its original ItemMeta, modifications will override specific components only.
     * @param itemStack ItemStack to wrap.
     * @return NighItem wrapper backed by the provided ItemStack.
     */
    @NotNull
    public static NightItem fromItemStack(@NotNull ItemStack itemStack) {
        return new NightItem(itemStack);
    }

    /**
     * Creates a new NightItem wrapper for the PLAYER_HEAD ItemStack with provided texture.
     * @param skinURL Skin texture URL.
     * @return NightItem wrapper backed by the PLAYER_HEAD with custom texture.
     */
    @NotNull
    public static NightItem asCustomHead(@NotNull String skinURL) {
        return new NightItem(Material.PLAYER_HEAD).setProfileBySkinURL(skinURL);
    }

    @NotNull
    public static NightItem read(@NotNull FileConfig config, @NotNull String path) {
        String materialName = config.getString(path + ".Material");
        int amount = config.getInt(path + ".Amount", 1);

        Material material = BukkitThing.getMaterial(String.valueOf(materialName));
        if (material == null) {
            Engine.core().error("Invalid material '" + materialName + "'. Found in '" + config.getFile().getAbsolutePath() + "' -> '" + path + "'.");
            material = Material.BARRIER;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        NightMeta displayMeta = NightMeta.read(config, path);

        return new NightItem(itemStack, displayMeta);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Material", BukkitThing.getAsString(this.material));
        config.set(path + ".Amount", this.amount == 1 ? null : this.amount);
        this.meta.write(config, path);
    }

    @NotNull
    public NightItem copy() {
        NightItem copy = new NightItem(this.backend, this.meta.copy());
        copy.setMaterial(this.material);
        copy.setAmount(this.amount);
        return copy;
    }

    @NotNull
    @Deprecated
    public NightItem inherit(@NotNull NightItem other) {
        this.backend.setType(other.getMaterial());
        this.backend.setAmount(other.getAmount());
        this.meta.inherit(other.meta);

        return this;
    }

    /**
     * Quickly wraps NightItem as MenuItem builder.
     * @return MenuItem builder.
     */
    @NotNull
    public MenuItem.Builder toMenuItem() {
        return MenuItem.builder(this);
    }

    /**
     * Builds new ItemStack instance.
     * @return New ItemStack instance.
     */
    @NotNull
    public ItemStack getItemStack() {
        ItemStack stack = Engine.software().setType(this.backend, this.material);

        stack.setAmount(this.amount);

        this.meta.apply(stack);
        return stack;
    }

    @NotNull
    public NightMeta getMeta() {
        return this.meta;
    }

    @NotNull
    public Material getMaterial() {
        return this.material;
    }

    @NotNull
    public NightItem setMaterial(@NotNull Material material) {
        if (!material.isItem()) throw new IllegalStateException("Material " + material.name() + " is not item!");

        this.material = material;
        return this;
    }

    public int getAmount() {
        return this.amount;
    }

    @NotNull
    public NightItem setAmount(int amount) {
        this.amount = NumberUtil.clamp(amount, 1, this.material.getMaxStackSize());
        return this;
    }

    @NotNull
    public NightItem setReplacer(@Nullable Replacer replacer) {
        this.meta.setReplacer(replacer);
        return this;
    }

    @NotNull
    public NightItem replacement(@NotNull Consumer<Replacer> consumer) {
        this.meta.replacement(consumer);
        return this;
    }

    @NotNull
    public NightItem ignoreNameAndLore() {
        this.setItemName(null);
        this.setDisplayName(null);
        this.setLore(null);
        return this;
    }

    @NotNull
    public NightItem ignoreAmount() {
        this.setAmount(1);
        return this;
    }

    @NotNull
    @Deprecated
    public NightItem localized(@NotNull LangItem langItem) {
//        this.setDisplayName(langItem.getLocalizedName());
//        this.setLore(langItem.getLocalizedLore());
        this.meta.localized(langItem);
        return this;
    }

    @NotNull
    @Deprecated
    public NightItem localized(@NotNull LangUIButton langUIButton) {
        this.meta.localized(langUIButton);
        return this;
    }

    @NotNull
    public NightItem localized(@NotNull IconLocale locale) {
        this.meta.localized(locale);
        return this;
    }

    @Nullable
    public String getItemName() {
        return this.meta.getItemName();
    }

    @NotNull
    public NightItem setItemName(@Nullable String itemName) {
        this.meta.setItemName(itemName);
        return this;
    }

    @Nullable
    public String getDisplayName() {
        return this.meta.getDisplayName();
    }

    @NotNull
    public NightItem setDisplayName(@Nullable String displayName) {
        this.meta.setDisplayName(displayName);
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.meta.getLore();
    }

    @NotNull
    public NightItem setLore(@Nullable List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    @NotNull
    public NightItem setDamage(@Nullable Integer damage) {
        this.meta.setDamage(damage);
        return this;
    }

    @NotNull
    public NightItem setEnchants(@NotNull Map<Enchantment, Integer> enchants) {
        this.meta.setEnchants(enchants);
        return this;
    }

    @NotNull
    @Deprecated
    public NightItem setSkinURL(@Nullable String skinURL) {
        this.meta.setSkinURL(skinURL);
        return this;
    }

//    @Nullable
//    @Deprecated
//    public PlayerProfile getSkullOwner() {
//        return this.meta.getSkullOwner();
//    }

    @NotNull
    @Deprecated
    public NightItem setSkullOwner(@Nullable OfflinePlayer owner) {
        this.meta.setSkullOwner(owner);
        return this;
    }

//    @NotNull
//    @Deprecated
//    public NightItem setSkullOwner(@Nullable PlayerProfile skullOwner) {
//        this.meta.setSkullOwner(skullOwner);
//        return this;
//    }

    @Nullable
    public CachedProfile getPlayerProfile() {
        return this.meta.getPlayerProfile();
    }

    @NotNull
    public NightItem setProfileBySkinURL(@NotNull String skinURL) {
        this.meta.setProfileBySkinURL(skinURL);
        return this;
    }

    @NotNull
    public NightItem setPlayerProfile(@NotNull OfflinePlayer player) {
        this.meta.setPlayerProfile(player);
        return this;
    }

    @NotNull
    public NightItem setPlayerProfile(@Nullable NightProfile profile) {
        this.meta.setPlayerProfile(profile);
        return this;
    }

    @NotNull
    public NightItem setPlayerProfile(@Nullable CachedProfile profile) {
        this.meta.setPlayerProfile(profile);
        return this;
    }

    @NotNull
    @Deprecated
    public NightItem setModelData(@Nullable Integer modelData) {
        this.meta.setModelData(modelData);
        return this;
    }

    @Nullable
    public Float getCustomModelData() {
        return this.meta.getCustomModelData();
    }

    @NotNull
    public NightItem setCustomModelData(@Nullable Float modelData) {
        this.meta.setCustomModelData(modelData);
        return this;
    }

    @NotNull
    public NightItem setModelPath(@Nullable NamespacedKey modelPath) {
        this.meta.setModelPath(modelPath);
        return this;
    }

    @NotNull
    public NightItem setTooltipStyle(@Nullable NamespacedKey tooltipStyle) {
        this.meta.setTooltipStyle(tooltipStyle);
        return this;
    }

    @NotNull
    public NightItem setColor(@NotNull Color color) {
        this.meta.setColor(color);
        return this;
    }

    @NotNull
    public NightItem setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    @NotNull
    public NightItem setEnchantGlint(boolean enchantGlint) {
        this.meta.setEnchantGlint(enchantGlint);
        return this;
    }

    @NotNull
    @Deprecated
    public NightItem setHideComponents(boolean hideComponents) {
        //this.meta.setHideComponents(hideComponents);
        return hideComponents ? this.hideAllComponents() : this.showAllComponents();
    }

    @NotNull
    public NightItem hideAllComponents() {
        this.meta.hideAllComponents();
        return this;
    }

    @NotNull
    public NightItem showAllComponents() {
        this.meta.showAllComponents();
        return this;
    }

    @NotNull
    public NightItem setHideTooltip(boolean hideTooltip) {
        this.meta.setHideTooltip(hideTooltip);
        return this;
    }
}
