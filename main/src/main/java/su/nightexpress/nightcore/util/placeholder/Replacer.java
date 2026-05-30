package su.nightexpress.nightcore.util.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.placeholder.PAPI;
import su.nightexpress.nightcore.util.ItemUtil;
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

    public Replacer(@NonNull Replacer other) {
        this.placeholders = new PlaceholderList<>(other.placeholders);
        this.replacers = new ArrayList<>(other.replacers);
    }

    @NonNull
    public static Replacer create() {
        return new Replacer();
    }

    public void clear() {
        this.placeholders.clear();
        this.replacers.clear();
    }

    @NonNull
    public List<UnaryOperator<String>> getReplacers() {
        List<UnaryOperator<String>> replacers = new ArrayList<>();
        replacers.add(this.placeholders.replacer(this));
        replacers.addAll(this.replacers);
        return replacers;
    }

    @NonNull
    public UnaryOperator<String> chained() {
        return this.getReplacers().stream().reduce((l, r) -> (string) -> l.andThen(r).apply(string)).orElseGet(
            UnaryOperator::identity);
    }

    @NonNull
    public Replacer and(@NonNull Consumer<Replacer> consumer) {
        consumer.accept(this);
        return this;
    }

    @NonNull
    @Deprecated
    public TextRoot getReplaced(@NonNull String source) {
        return this.getReplaced(NightMessage.from(source));
    }

    @NonNull
    @Deprecated
    public TextRoot getReplaced(@NonNull TextRoot source) {
        //        TextRoot root = source.copy();
        //        this.getReplacers().forEach(root::replace);
        //        return root;
        return this.apply(source);
    }

    @NonNull
    @Deprecated
    public TextRoot apply(@NonNull TextRoot source) {
        TextRoot root = source.copy();
        this.getReplacers().forEach(root::replace);
        return root;
    }

    @NonNull
    @Deprecated
    public String getReplacedRaw(@NonNull String source) {
        //        String result = source;
        //        for (UnaryOperator<String> operator : this.getReplacers()) {
        //            result = operator.apply(result);
        //        }
        //        return result;
        return this.apply(source);
    }

    @NonNull
    public String apply(@NonNull String source) {
        String result = source;
        for (UnaryOperator<String> operator : this.getReplacers()) {
            result = operator.apply(result);
        }
        return result;
    }

    @NonNull
    public List<String> apply(@NonNull List<String> list) {
        List<String> result = new ArrayList<>(list);
        for (UnaryOperator<String> operator : this.getReplacers()) {
            result = replaceList(result, operator);
        }

        return result;
    }

    @NonNull
    @Deprecated
    public ItemStack apply(@NonNull ItemStack itemStack) {
        ItemUtil.editMeta(itemStack, this::apply);
        return itemStack;
    }

    @NonNull
    @Deprecated
    public ItemMeta apply(@NonNull ItemMeta meta) {
        List<String> lore = meta.getLore();

        if (meta.hasItemName()) {
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

    @NonNull
    private static List<String> replaceList(@NonNull List<String> lore, @NonNull UnaryOperator<String> operator) {
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

    @NonNull
    public Replacer replacePlaceholderAPI(@NonNull Player player) {
        if (!PAPI.isPresent()) return this;

        return this.replaceOperator(line -> CommonPlaceholders.setPAPIPlaceholders(player, line));
    }

    @NonNull
    public Replacer replace(@NonNull String key, @NonNull Consumer<List<String>> replacer) {
        List<String> list = new ArrayList<>();
        replacer.accept(list);

        return this.replace(key, list);
    }

    @NonNull
    public Replacer replace(@NonNull String key, @NonNull List<String> replacer) {
        return this.replacePlaceholder(key, () -> String.join(TagWrappers.BR, replacer));
    }

    @NonNull
    public <T> Replacer replace(@NonNull T source, @NonNull PlaceholderList<T> placeholders) {
        return this.replaceOperator(placeholders.replacer(source));
    }

    @NonNull
    public Replacer replace(@NonNull String key, @NonNull Supplier<String> value) {
        return this.replacePlaceholder(key, value);
    }

    @NonNull
    public Replacer replace(@NonNull String key, @NonNull Object value) {
        return this.replacePlaceholder(key, value);
    }

    @NonNull
    public Replacer replace(@NonNull UnaryOperator<String> replacer) {
        return this.replaceOperator(replacer);
    }

    @NonNull
    private Replacer replacePlaceholder(@NonNull String key, @NonNull Supplier<String> value) {
        this.placeholders.add(key, value);
        return this;
    }

    @NonNull
    private Replacer replacePlaceholder(@NonNull String key, @NonNull Object value) {
        this.placeholders.add(key, String.valueOf(value));
        return this;
    }

    @NonNull
    private Replacer replaceOperator(@NonNull UnaryOperator<String> replacer) {
        this.replacers.add(replacer);
        return this;
    }
}
