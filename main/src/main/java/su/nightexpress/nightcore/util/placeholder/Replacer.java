package su.nightexpress.nightcore.util.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Replacer {

    private final PlaceholderList<Object>     placeholders;
    private final List<UnaryOperator<String>> replacers;

    public Replacer() {
        this.placeholders = new PlaceholderList<>();
        this.replacers = new ArrayList<>();
    }

    public Replacer(@NotNull Replacer other) {
        this.placeholders = new PlaceholderList<>(other.placeholders);
        this.replacers = new ArrayList<>(other.replacers);
    }

    @NotNull
    public static Replacer create() {
        return new Replacer();
    }

    public void clear() {
        this.placeholders.clear();
        this.replacers.clear();
    }

    @NotNull
    public List<UnaryOperator<String>> getReplacers() {
        List<UnaryOperator<String>> replacers = new ArrayList<>();
        replacers.add(this.placeholders.replacer(this));
        replacers.addAll(this.replacers);
        return replacers;
    }

    @NotNull
    public UnaryOperator<String> chained() {
        return this.getReplacers().stream().reduce((l, r) -> (string) -> l.andThen(r).apply(string)).orElseGet(UnaryOperator::identity);
    }

    @NotNull
    public Replacer and(@NotNull Consumer<Replacer> consumer) {
        consumer.accept(this);
        return this;
    }

    @NotNull
    @Deprecated
    public TextRoot getReplaced(@NotNull String source) {
        return this.getReplaced(NightMessage.from(source));
    }

    @NotNull
    @Deprecated
    public TextRoot getReplaced(@NotNull TextRoot source) {
//        TextRoot root = source.copy();
//        this.getReplacers().forEach(root::replace);
//        return root;
        return this.apply(source);
    }

    @NotNull
    @Deprecated
    public TextRoot apply(@NotNull TextRoot source) {
        TextRoot root = source.copy();
        this.getReplacers().forEach(root::replace);
        return root;
    }

    @NotNull
    @Deprecated
    public String getReplacedRaw(@NotNull String source) {
//        String result = source;
//        for (UnaryOperator<String> operator : this.getReplacers()) {
//            result = operator.apply(result);
//        }
//        return result;
        return this.apply(source);
    }

    @NotNull
    public String apply(@NotNull String source) {
        String result = source;
        for (UnaryOperator<String> operator : this.getReplacers()) {
            result = operator.apply(result);
        }
        return result;
    }

    @NotNull
    public List<String> apply(@NotNull List<String> list) {
        List<String> result = new ArrayList<>(list);
        for (UnaryOperator<String> operator : this.getReplacers()) {
            result = replaceList(result, operator);
        }

        return result;
    }

    @NotNull
    @Deprecated
    public ItemStack apply(@NotNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, this::apply);
        return itemStack;
    }

    @NotNull
    @Deprecated
    public ItemMeta apply(@NotNull ItemMeta meta) {
        List<String> lore = meta.getLore();

        if (Version.isAtLeast(Version.MC_1_21) && meta.hasItemName()) {
            meta.setItemName(this.apply(meta.getItemName()));
        }
        if (meta.hasDisplayName()) {
            meta.setDisplayName(this.apply(meta.getDisplayName()));
        }
        if (lore != null) {
            meta.setLore(this.apply(lore));
        }

        return meta;
    }

    @NotNull
    private static List<String> replaceList(@NotNull List<String> lore, @NotNull UnaryOperator<String> operator) {
        List<String> replaced = new ArrayList<>();
        for (String line : lore) {
            if (!line.isBlank()) {
                line = operator.apply(line);
                if (line.isBlank()) continue;

                replaced.addAll(Arrays.asList(ParserUtils.breakDownLineSplitters(line)));
            }
            else replaced.add(line);
        }
        return replaced;
    }

    @NotNull
    public Replacer replacePlaceholderAPI(@NotNull Player player) {
        if (!Plugins.hasPlaceholderAPI()) return this;

        return this.replaceOperator(line -> PlaceholderAPI.setPlaceholders(player, line));
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Consumer<List<String>> replacer) {
        List<String> list = new ArrayList<>();
        replacer.accept(list);

        return this.replace(key, list);
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull List<String> replacer) {
        return this.replacePlaceholder(key, () -> String.join(TagWrappers.BR, replacer));
    }

    @NotNull
    public <T> Replacer replace(@NotNull T source, @NotNull PlaceholderList<T> placeholders) {
        return this.replaceOperator(placeholders.replacer(source));
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Supplier<String> value) {
        return this.replacePlaceholder(key, value);
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Object value) {
        return this.replacePlaceholder(key, value);
    }

    @NotNull
    public Replacer replace(@NotNull UnaryOperator<String> replacer) {
        return this.replaceOperator(replacer);
    }

    @NotNull
    private Replacer replacePlaceholder(@NotNull String key, @NotNull Supplier<String> value) {
        this.placeholders.add(key, value);
        return this;
    }

    @NotNull
    private Replacer replacePlaceholder(@NotNull String key, @NotNull Object value) {
        this.placeholders.add(key, String.valueOf(value));
        return this;
    }

    @NotNull
    private Replacer replaceOperator(@NotNull UnaryOperator<String> replacer) {
        this.replacers.add(replacer);
        return this;
    }
}
