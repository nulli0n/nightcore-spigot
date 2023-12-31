package su.nightexpress.nightcore.util;

import com.google.common.base.Splitter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemReplacer {

    private final ItemStack      item;
    private final ItemMeta       meta;
    private final PlaceholderMap placeholderMap;

    private String displayName;
    private String lore;

    private boolean trimLore;
    private boolean hideFlags;

    public ItemReplacer(@NotNull ItemStack item) {
        this(item, item.getItemMeta());
    }

    public ItemReplacer(@NotNull ItemMeta meta) {
        this(null, meta);
    }

    public ItemReplacer(@Nullable ItemStack item, @Nullable ItemMeta meta) {
        this.item = item;
        this.meta = meta;
        this.placeholderMap = new PlaceholderMap();
    }

    @NotNull
    public ItemReplacer readMeta() {
        if (this.hasMeta()) {
            this.setDisplayName(this.meta.getDisplayName());
            this.setLore(this.meta.getLore());
        }
        return this;
    }

    @NotNull
    public ItemReplacer readLocale(@NotNull LangItem locale) {
        if (this.hasMeta()) {
            this.setDisplayName(locale.getLocalizedName());
            this.setLore(locale.getLocalizedLore());
        }
        return this;
    }

    public void writeMeta() {
        if (!this.hasMeta()) return;

        this.replace(this.placeholderMap.replacer());

        this.meta.setDisplayName(this.getDisplayName() == null ? null : NightMessage.asLegacy(this.getDisplayName()));
        this.meta.setLore(NightMessage.asLegacy(this.packTrimmedLore()));

        if (this.isHideFlags()) {
            this.meta.addItemFlags(ItemFlag.values());
        }

        if (this.hasItem()) {
            this.item.setItemMeta(this.meta);
        }
    }

    @NotNull
    public static ItemReplacer create(@NotNull ItemStack item) {
        return new ItemReplacer(item);
    }

    @NotNull
    public static ItemReplacer create(@NotNull ItemMeta meta) {
        return new ItemReplacer(meta);
    }

    public static void replace(@NotNull ItemStack item, @NotNull UnaryOperator<String> replacer) {
        create(item).trimmed().readMeta().replace(replacer).writeMeta();
    }

    public static void replace(@NotNull ItemMeta meta, @NotNull UnaryOperator<String> replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    public boolean hasMeta() {
        return this.meta != null;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public boolean isTrimLore() {
        return trimLore;
    }

    public boolean isHideFlags() {
        return hideFlags;
    }

    @NotNull
    public ItemReplacer trimmed() {
        this.setTrimLore(true);
        return this;
    }

    @NotNull
    public ItemReplacer hideFlags() {
        this.setHideFlags(true);
        return this;
    }

    @NotNull
    public ItemReplacer setHideFlags(boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this;
    }

    @NotNull
    public ItemReplacer setTrimLore(boolean trimLore) {
        this.trimLore = trimLore;
        return this;
    }

    @NotNull
    public ItemReplacer replace(@NotNull String placeholder, @NotNull String value) {
        return this.replace(placeholder, () -> value);
    }

    @NotNull
    public ItemReplacer replace(@NotNull String placeholder, @NotNull Supplier<String> value) {
        this.placeholderMap.add(placeholder, value);
        return this;
    }

    @NotNull
    public ItemReplacer replace(@NotNull PlaceholderMap... placeholderMaps) {
        for (PlaceholderMap placeholder : placeholderMaps) {
            this.placeholderMap.add(placeholder);
        }
        return this;
    }

    @NotNull
    public ItemReplacer replace(@NotNull UnaryOperator<String> replacer) {
        if (this.getDisplayName() != null) {
            this.setDisplayName(replacer.apply(this.getDisplayName()));
        }

        if (this.getLore() != null) {
            this.setLore(replacer.apply(this.getLore()));
        }
        return this;
    }

    @NotNull
    public ItemReplacer replacePlaceholderAPI(@NotNull Player player) {
        if (!Plugins.hasPlaceholderAPI()) return this;

        this.replace(str -> PlaceholderAPI.setPlaceholders(player, str));
        return this;
    }

    @NotNull
    public ItemReplacer replaceLoreExact(@NotNull String placeholder, @NotNull List<String> replacer) {
        if (this.getLore() == null) return this;

        this.setLore(Lists.replace(this.packLore(), placeholder, replacer));
        return this;
    }

    @NotNull
    public ItemReplacer replaceLoreTrail(@NotNull String placeholder, @NotNull List<String> replacer) {
        if (this.getLore() == null) return this;

        List<String> loreReplaced = new ArrayList<>();
        for (String lineHas : this.packLore()) {
            if (lineHas.contains(placeholder)) {
                replacer.forEach(lineRep -> {
                    loreReplaced.add(lineHas.replace(placeholder, lineRep));
                });
                continue;
            }
            loreReplaced.add(lineHas);
        }
        this.setLore(loreReplaced);
        return this;
    }

    @NotNull
    public List<String> packLore() {
        if (this.getLore() == null) return new ArrayList<>();

        return Splitter.on("\n").splitToList(this.getLore());
    }

    @NotNull
    public List<String> packTrimmedLore() {
        List<String> lore = this.packLore();
        if (this.isTrimLore()) {
            lore = Lists.stripEmpty(lore);
        }
        return lore;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public ItemReplacer setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Nullable
    public String getLore() {
        return lore;
    }

    @NotNull
    public ItemReplacer setLore(@Nullable List<String> lore) {
        return this.setLore(lore == null ? null : String.join("\n", lore));
    }

    @NotNull
    public ItemReplacer setLore(@Nullable String lore) {
        this.lore = lore;
        return this;
    }
}
