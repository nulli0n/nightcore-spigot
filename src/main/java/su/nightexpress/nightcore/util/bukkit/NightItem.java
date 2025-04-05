package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreLogger;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Utility class to create <b>cosmetic</b> items only.<br>
 * Do <b>NOT</b> use to create custom items for regular gameplay.
 */
public class NightItem implements Writeable {

    private final ItemStack itemStack;
    private final NightMeta meta;

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
        this.itemStack = new ItemStack(itemStack);
        this.meta = meta;
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
        return new NightItem(Material.PLAYER_HEAD).setSkinURL(skinURL);
    }

    @NotNull
    public static NightItem read(@NotNull FileConfig config, @NotNull String path) {
        String materialName = config.getString(path + ".Material");
        int amount = config.getInt(path + ".Amount", 1);

        Material material = BukkitThing.getMaterial(String.valueOf(materialName));
        if (material == null) {
            CoreLogger.error("Invalid material '" + materialName + "'. Caused by '" + config.getFile().getAbsolutePath() + "'.");
            material = Material.AIR;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        NightMeta displayMeta = NightMeta.read(config, path);

        return new NightItem(itemStack, displayMeta);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Material", BukkitThing.toString(this.itemStack.getType()));
        config.set(path + ".Amount", this.itemStack.getAmount() == 1 ? null : this.itemStack.getAmount());
        this.meta.write(config, path);
    }

    @NotNull
    public NightItem copy() {
        return new NightItem(this.itemStack, this.meta.copy());
    }

    @NotNull
    public NightItem inherit(@NotNull NightItem other) {
        this.itemStack.setType(other.getMaterial());
        this.itemStack.setAmount(other.getAmount());
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
        ItemStack stack = new ItemStack(this.itemStack);
        this.meta.apply(stack/*, true*/);
        return stack;
    }

    @NotNull
    public NightMeta getMeta() {
        return this.meta;
    }

    @NotNull
    public Material getMaterial() {
        return this.itemStack.getType();
    }

    @NotNull
    public NightItem setMaterial(@NotNull Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }

    @NotNull
    public NightItem setAmount(int amount) {
        this.itemStack.setAmount(amount);
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
    public NightItem localized(@NotNull LangUIButton langUIButton) {
        this.meta.localized(langUIButton);
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
    public NightItem setSkinURL(@Nullable String skinURL) {
        this.meta.setSkinURL(skinURL);
        return this;
    }

    @Nullable
    public PlayerProfile getSkullOwner() {
        return this.meta.getSkullOwner();
    }

    @NotNull
    public NightItem setSkullOwner(@Nullable OfflinePlayer owner) {
        return this.setSkullOwner(owner == null ? null : owner.getPlayerProfile());
    }

    @NotNull
    public NightItem setSkullOwner(@Nullable PlayerProfile skullOwner) {
        this.meta.setSkullOwner(skullOwner);
        return this;
    }

    @NotNull
    public NightItem setModelData(@Nullable Integer modelData) {
        this.meta.setModelData(modelData);
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
    public NightItem setHideComponents(boolean hideComponents) {
        this.meta.setHideComponents(hideComponents);
        return this;
    }

    @NotNull
    public NightItem setHideTooltip(boolean hideTooltip) {
        this.meta.setHideTooltip(hideTooltip);
        return this;
    }
}
