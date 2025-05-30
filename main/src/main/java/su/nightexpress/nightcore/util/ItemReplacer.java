package su.nightexpress.nightcore.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderList;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Deprecated
public class ItemReplacer {

    private final ItemStack item;
    private final ItemMeta  meta;

    //private final PlaceholderMap              placeholderCache;
    //private final List<UnaryOperator<String>> operatorCache;

    private final Replacer replacer;

    private String displayName;
    private List<String> lore;

    @Deprecated private boolean trimLore;
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
        this.replacer = new Replacer();
        //this.placeholderCache = new PlaceholderMap();
        //this.operatorCache = new ArrayList<>();
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

        if (this.displayName != null) {
            this.meta.setDisplayName(NightMessage.asLegacy(this.replacer.apply(this.displayName)));
        }
        if (this.lore != null) {
            this.replacer.getReplacers().forEach(this::replaceLore);
            this.meta.setLore(this.packTrimmedLore());
        }

//        if (this.isHideFlags()) {
//            ItemUtil.hideAttributes(this.meta, this.item.getType());
//        }

        if (this.hasItem()) {
            this.item.setItemMeta(this.meta);
        }

        if (this.isHideFlags()) {
            ItemUtil.hideAttributes(this.item);
        }

        this.replacer.clear();
    }

    @Deprecated
    public static void replace(@NotNull ItemMeta meta, @NotNull UnaryOperator<String> replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NotNull ItemStack item, @NotNull PlaceholderMap replacer) {
        create(item).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NotNull ItemMeta meta, @NotNull PlaceholderMap replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replacePlaceholderAPI(@NotNull ItemMeta meta, @NotNull Player player) {
        create(meta).trimmed().readMeta().replacePlaceholderAPI(player).writeMeta();
    }

    public static void replace(@NotNull ItemStack itemStack, @NotNull UnaryOperator<String> operator) {
        //create(itemStack).trimmed().readMeta().replace(replacer).writeMeta();

        create(itemStack).readMeta().replacement(replacer -> replacer.replace(operator)).writeMeta();
    }

    public static <T> void replace(@NotNull ItemStack itemStack, @NotNull PlaceholderList<T> placeholderList, @NotNull T source) {
        //create(itemStack).trimmed().readMeta().replace(placeholderList.replacer(source)).writeMeta();

        create(itemStack).readMeta().replacement(replacer -> replacer.replace(source, placeholderList)).writeMeta();
    }

    public static void replacePlaceholderAPI(@NotNull ItemStack item, @NotNull Player player) {
        create(item).readMeta().replacement(replacer -> replacer.replacePlaceholderAPI(player)).writeMeta();

        //create(item).trimmed().readMeta().replacePlaceholderAPI(player).writeMeta();
    }

    public boolean hasMeta() {
        return this.meta != null;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    @Deprecated
    public boolean isTrimLore() {
        return trimLore;
    }

    public boolean isHideFlags() {
        return hideFlags;
    }

    @NotNull
    @Deprecated
    public ItemReplacer trimmed() {
        this.setTrimLore(true);
        return this;
    }

    @NotNull
    public ItemReplacer hideFlags() {
        this.hideFlags = true;
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer setHideFlags(boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer setTrimLore(boolean trimLore) {
        this.trimLore = trimLore;
        return this;
    }

    @NotNull
    @Deprecated
    private ItemReplacer cachePlaceholder(@NotNull String placeholder, @NotNull Supplier<String> value) {
        this.replacer.replace(placeholder, value);
        //this.placeholderCache.add(placeholder, value);
        return this;
    }

    @NotNull
    @Deprecated
    private ItemReplacer cachePlaceholder(@NotNull String placeholder, @NotNull String value) {
        this.replacer.replace(placeholder, value);
        //this.placeholderCache.add(placeholder, value);
        return this;
    }

    @NotNull
    @Deprecated
    private ItemReplacer cacheOperator(@NotNull UnaryOperator<String> operator) {
        this.replacer.replace(operator);
        //this.operatorCache.add(operator);
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer replace(@NotNull String placeholder, @NotNull String value) {
        return this.cachePlaceholder(placeholder, value);
    }

    @NotNull
    @Deprecated
    public ItemReplacer replace(@NotNull String placeholder, @NotNull Supplier<String> value) {
        return this.cachePlaceholder(placeholder, value);
    }

    @NotNull
    @Deprecated
    public ItemReplacer replace(@NotNull String placeholder, @NotNull List<String> replacer) {
        return this.cacheOperator(str -> str.replace(placeholder, String.join("\n", replacer)));
    }

    @NotNull
    @Deprecated
    public <T> ItemReplacer replace(@NotNull String placeholder, @NotNull PlaceholderList<T> placeholderList, @NotNull T source) {
        return this.cacheOperator(placeholderList.replacer(source));
    }

    public ItemReplacer replacement(@NotNull Consumer<Replacer> consumer) {
        consumer.accept(this.replacer);
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer replace(@NotNull PlaceholderMap... placeholderMaps) {
        for (PlaceholderMap placeholder : placeholderMaps) {
            //this.placeholderCache.add(placeholder);
            this.replacer.replace(placeholder.replacer());
        }
        return this;
    }

    @NotNull
    @Deprecated
    public ItemReplacer replace(@NotNull UnaryOperator<String> replacer) {
        return cacheOperator(replacer);
    }

    @NotNull
    @Deprecated
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

        List<String> fixed = addEmptyLines(lore);

        return NightMessage.asLegacy(fixed);
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
