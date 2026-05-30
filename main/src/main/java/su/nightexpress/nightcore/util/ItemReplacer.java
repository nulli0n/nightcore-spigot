package su.nightexpress.nightcore.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderList;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.night.NightMessage;

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

    private String       displayName;
    private List<String> lore;

    @Deprecated
    private boolean trimLore;
    private boolean hideFlags;

    public ItemReplacer(@NonNull ItemStack item) {
        this(item, item.getItemMeta());
    }

    public ItemReplacer(@NonNull ItemMeta meta) {
        this(null, meta);
    }

    public ItemReplacer(@Nullable ItemStack item, @Nullable ItemMeta meta) {
        this.item = item;
        this.meta = meta;
        this.replacer = new Replacer();
        //this.placeholderCache = new PlaceholderMap();
        //this.operatorCache = new ArrayList<>();
    }

    @NonNull
    public static ItemReplacer create(@NonNull ItemStack item) {
        return new ItemReplacer(item);
    }

    @NonNull
    public static ItemReplacer create(@NonNull ItemMeta meta) {
        return new ItemReplacer(meta);
    }

    @NonNull
    public ItemReplacer readMeta() {
        if (this.hasMeta()) {
            this.setDisplayName(this.meta.getDisplayName());
            this.setLore(this.meta.getLore());
        }
        return this;
    }

    @NonNull
    public ItemReplacer readLocale(@NonNull LangItem locale) {
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
    public static void replace(@NonNull ItemMeta meta, @NonNull UnaryOperator<String> replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NonNull ItemStack item, @NonNull PlaceholderMap replacer) {
        create(item).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replace(@NonNull ItemMeta meta, @NonNull PlaceholderMap replacer) {
        create(meta).trimmed().readMeta().replace(replacer).writeMeta();
    }

    @Deprecated
    public static void replacePlaceholderAPI(@NonNull ItemMeta meta, @NonNull Player player) {
        create(meta).trimmed().readMeta().replacePlaceholderAPI(player).writeMeta();
    }

    public static void replace(@NonNull ItemStack itemStack, @NonNull UnaryOperator<String> operator) {
        //create(itemStack).trimmed().readMeta().replace(replacer).writeMeta();

        create(itemStack).readMeta().replacement(replacer -> replacer.replace(operator)).writeMeta();
    }

    public static <T> void replace(@NonNull ItemStack itemStack, @NonNull PlaceholderList<T> placeholderList,
                                   @NonNull T source) {
        //create(itemStack).trimmed().readMeta().replace(placeholderList.replacer(source)).writeMeta();

        create(itemStack).readMeta().replacement(replacer -> replacer.replace(source, placeholderList)).writeMeta();
    }

    public static void replacePlaceholderAPI(@NonNull ItemStack item, @NonNull Player player) {
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

    @NonNull
    @Deprecated
    public ItemReplacer trimmed() {
        this.setTrimLore(true);
        return this;
    }

    @NonNull
    public ItemReplacer hideFlags() {
        this.hideFlags = true;
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer setHideFlags(boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer setTrimLore(boolean trimLore) {
        this.trimLore = trimLore;
        return this;
    }

    @NonNull
    @Deprecated
    private ItemReplacer cachePlaceholder(@NonNull String placeholder, @NonNull Supplier<String> value) {
        this.replacer.replace(placeholder, value);
        //this.placeholderCache.add(placeholder, value);
        return this;
    }

    @NonNull
    @Deprecated
    private ItemReplacer cachePlaceholder(@NonNull String placeholder, @NonNull String value) {
        this.replacer.replace(placeholder, value);
        //this.placeholderCache.add(placeholder, value);
        return this;
    }

    @NonNull
    @Deprecated
    private ItemReplacer cacheOperator(@NonNull UnaryOperator<String> operator) {
        this.replacer.replace(operator);
        //this.operatorCache.add(operator);
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer replace(@NonNull String placeholder, @NonNull String value) {
        return this.cachePlaceholder(placeholder, value);
    }

    @NonNull
    @Deprecated
    public ItemReplacer replace(@NonNull String placeholder, @NonNull Supplier<String> value) {
        return this.cachePlaceholder(placeholder, value);
    }

    @NonNull
    @Deprecated
    public ItemReplacer replace(@NonNull String placeholder, @NonNull List<String> replacer) {
        return this.cacheOperator(str -> str.replace(placeholder, String.join("\n", replacer)));
    }

    @NonNull
    @Deprecated
    public <T> ItemReplacer replace(@NonNull String placeholder, @NonNull PlaceholderList<T> placeholderList,
                                    @NonNull T source) {
        return this.cacheOperator(placeholderList.replacer(source));
    }

    public ItemReplacer replacement(@NonNull Consumer<Replacer> consumer) {
        consumer.accept(this.replacer);
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer replace(@NonNull PlaceholderMap... placeholderMaps) {
        for (PlaceholderMap placeholder : placeholderMaps) {
            //this.placeholderCache.add(placeholder);
            this.replacer.replace(placeholder.replacer());
        }
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer replace(@NonNull UnaryOperator<String> replacer) {
        return cacheOperator(replacer);
    }

    @NonNull
    @Deprecated
    public ItemReplacer replacePlaceholderAPI(@NonNull Player player) {
        if (Plugins.hasPlaceholderAPI()) {
            this.cacheOperator(str -> CommonPlaceholders.setPAPIPlaceholders(player, str));
        }
        return this;
    }

    @NonNull
    @Deprecated
    public ItemReplacer replaceLoreExact(@NonNull String placeholder, @NonNull List<String> replacer) {
        return this.replace(placeholder, replacer);
    }

    @NonNull
    @Deprecated
    public ItemReplacer replaceLore(@NonNull String placeholder, @NonNull Supplier<List<String>> replacer) {
        return this.replace(placeholder, replacer.get());
    }

    @NonNull
    @Deprecated
    public ItemReplacer injectLore(@NonNull String placeholder, @NonNull List<String> replacer) {
        return this.replace(placeholder, replacer);
    }

    @NonNull
    @Deprecated
    public ItemReplacer injectPlaceholderAPI(@NonNull Player player) {
        return this.replacePlaceholderAPI(player);
    }

    @NonNull
    @Deprecated
    public ItemReplacer replaceLoreTrail(@NonNull String placeholder, @NonNull List<String> replacer) {
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

    @NonNull
    public List<String> packLore() {
        //        if (this.lore == null) return new ArrayList<>();
        //
        //        return Splitter.on("\n").splitToList(this.lore);
        return this.lore == null ? new ArrayList<>() : this.lore;
    }

    private void replaceLore(@NonNull UnaryOperator<String> operator) {
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

    @NonNull
    public List<String> packTrimmedLore() {
        List<String> lore = this.packLore();
        if (this.isTrimLore()) {
            lore = Lists.stripEmpty(lore);
        }

        List<String> fixed = addEmptyLines(lore);

        return Lists.modify(fixed, NightMessage::asLegacy);

        //return su.nightexpress.nightcore.util.text.NightMessage.asLegacy(fixed);
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

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @NonNull
    public ItemReplacer setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return this.lore;
    }

    @NonNull
    public ItemReplacer setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
        //return this.setLore(lore == null ? null : String.join("\n", lore));
    }

    @NonNull
    public ItemReplacer setLore(@Nullable String lore) {
        this.lore = lore == null ? null : Lists.newList(lore.split("\n"));
        //this.lore = lore;
        return this;
    }
}
