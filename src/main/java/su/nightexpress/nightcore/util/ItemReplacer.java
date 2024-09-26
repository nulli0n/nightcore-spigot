package su.nightexpress.nightcore.util;

import com.google.common.base.Splitter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemReplacer {

    private final ItemStack item;
    private final ItemMeta  meta;

    private final PlaceholderMap              placeholderCache;
    private final List<UnaryOperator<String>> operatorCache;

    private String displayName;
    private List<String> lore;

    private boolean trimLore;
    private boolean hideFlags;
    //private Player papi;

    public ItemReplacer(@NotNull ItemStack item) {
        this(item, item.getItemMeta());
    }

    public ItemReplacer(@NotNull ItemMeta meta) {
        this(null, meta);
    }

    public ItemReplacer(@Nullable ItemStack item, @Nullable ItemMeta meta) {
        this.item = item;
        this.meta = meta;
        this.placeholderCache = new PlaceholderMap();
        this.operatorCache = new ArrayList<>();
    }

    @NotNull
    public static ItemReplacer create(@NotNull ItemStack item) {
        return new ItemReplacer(item);
    }

    @NotNull
    public static ItemReplacer create(@NotNull ItemMeta meta) {
        return new ItemReplacer(meta);
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

        List<UnaryOperator<String>> operators = new ArrayList<>(this.operatorCache);
        operators.add(this.placeholderCache.replacer());

        boolean hasDisplayName = this.displayName != null;
        //boolean hasLore = this.lore != null;

        operators.forEach(operator -> {
            if (hasDisplayName) this.setDisplayName(operator.apply(this.displayName));
            this.replaceLore(operator);
            //if (hasLore) this.setLore(operator.apply(this.lore));
        });

        this.meta.setDisplayName(this.getDisplayName() == null ? null : NightMessage.asLegacy(this.getDisplayName()));
        this.meta.setLore(this.packTrimmedLore());

        if (this.isHideFlags()) {
            ItemUtil.hideAttributes(this.meta, this.item.getType());
        }

        if (this.hasItem()) {
            this.item.setItemMeta(this.meta);
        }

        this.placeholderCache.clear();
        this.operatorCache.clear();
    }

    public static void replace(@NotNull ItemStack item, @NotNull UnaryOperator<String> replacer) {
        create(item).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NotNull ItemMeta meta, @NotNull UnaryOperator<String> replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    public static void replace(@NotNull ItemStack item, @NotNull PlaceholderMap replacer) {
        create(item).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NotNull ItemMeta meta, @NotNull PlaceholderMap replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    public static void replacePlaceholderAPI(@NotNull ItemStack item, @NotNull Player player) {
        create(item).trimmed().readMeta().replacePlaceholderAPI(player).writeMeta();
    }

    @Deprecated
    public static void replacePlaceholderAPI(@NotNull ItemMeta meta, @NotNull Player player) {
        create(meta).trimmed().readMeta().replacePlaceholderAPI(player).writeMeta();
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
    private ItemReplacer cachePlaceholder(@NotNull String placeholder, @NotNull Supplier<String> value) {
        this.placeholderCache.add(placeholder, value);
        return this;
    }

    @NotNull
    private ItemReplacer cacheOperator(@NotNull UnaryOperator<String> operator) {
        this.operatorCache.add(operator);
        return this;
    }

    @NotNull
    public ItemReplacer replace(@NotNull String placeholder, @NotNull String value) {
        return this.cachePlaceholder(placeholder, () -> value);
    }

    @NotNull
    public ItemReplacer replace(@NotNull String placeholder, @NotNull Supplier<String> value) {
        return this.cachePlaceholder(placeholder, value);
    }

    @NotNull
    public ItemReplacer replace(@NotNull String placeholder, @NotNull List<String> replacer) {
        return this.cacheOperator(str -> str.replace(placeholder, String.join("\n", replacer)));
    }

    @NotNull
    public ItemReplacer replace(@NotNull PlaceholderMap... placeholderMaps) {
        for (PlaceholderMap placeholder : placeholderMaps) {
            this.placeholderCache.add(placeholder);
        }
        return this;
    }

    @NotNull
    public ItemReplacer replace(@NotNull UnaryOperator<String> replacer) {
        return cacheOperator(replacer);
    }

    @NotNull
    public ItemReplacer replacePlaceholderAPI(@NotNull Player player) {
        if (Plugins.hasPlaceholderAPI()) {
            this.cacheOperator(str -> PlaceholderAPI.setPlaceholders(player, str));
        }
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer replaceLoreExact(@NotNull String placeholder, @NotNull List<String> replacer) {
        return this.replace(placeholder, replacer);
    }

    @NotNull
    @Deprecated
    public ItemReplacer replaceLore(@NotNull String placeholder, @NotNull Supplier<List<String>> replacer) {
        return this.replace(placeholder, replacer.get());
    }

    @NotNull
    @Deprecated
    public ItemReplacer injectLore(@NotNull String placeholder, @NotNull List<String> replacer) {
        return this.replace(placeholder, replacer);
    }

    @NotNull
    @Deprecated
    public ItemReplacer injectPlaceholderAPI(@NotNull Player player) {
        return this.replacePlaceholderAPI(player);
    }

    @NotNull
    @Deprecated
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
//        if (this.lore == null) return new ArrayList<>();
//
//        return Splitter.on("\n").splitToList(this.lore);
        return this.lore == null ? new ArrayList<>() : this.lore;
    }

    private void replaceLore(@NotNull UnaryOperator<String> operator) {
        if (this.lore == null) return;

        List<String> replaced = new ArrayList<>();
        for (String line : this.lore) {
            if (!line.isBlank()) {
                line = operator.apply(line);
                if (line.isBlank()) continue;

                replaced.addAll(Arrays.asList(line.split("\n")));
            }
            else replaced.add(line);
        }
        this.setLore(replaced);
    }

    @NotNull
    public List<String> packTrimmedLore() {
        List<String> lore = this.packLore();
        if (this.isTrimLore()) {
            lore = Lists.stripEmpty(lore);
        }

        List<String> fixed = fixLore(lore);

        return NightMessage.asLegacy(fixed);
    }

    @NotNull
    private List<String> fixLore(@NotNull List<String> lore) {
        for (int index = 0; index < lore.size(); index++) {
            String line = lore.get(index);
            if (line.equalsIgnoreCase(Placeholders.EMPTY_IF_ABOVE)) {
                if (index == 0 || !this.isNotEmpty(lore.get(index - 1))) {
                    lore.remove(index);
                }
                else lore.set(index, "");

                return fixLore(lore);
            }
            else if (line.equalsIgnoreCase(Placeholders.EMPTY_IF_BELOW)) {
                if (index == lore.size() - 1 || !this.isNotEmpty(lore.get(index + 1))) {
                    lore.remove(index);
                }
                else lore.set(index, "");

                return fixLore(lore);
            }
        }

        return lore;
    }

    private boolean isNotEmpty(@NotNull String line) {
        return !line.isBlank() && !line.equalsIgnoreCase(Placeholders.EMPTY_IF_ABOVE) && !line.equalsIgnoreCase(Placeholders.EMPTY_IF_BELOW);
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @NotNull
    public ItemReplacer setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.lore;
    }

    @NotNull
    public ItemReplacer setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
        //return this.setLore(lore == null ? null : String.join("\n", lore));
    }

    @NotNull
    public ItemReplacer setLore(@Nullable String lore) {
        this.lore = lore == null ? null : Lists.newList(lore.split("\n"));
        //this.lore = lore;
        return this;
    }
}
