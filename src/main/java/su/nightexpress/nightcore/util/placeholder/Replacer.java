package su.nightexpress.nightcore.util.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.ArrayList;
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
    public TextRoot getReplaced(@NotNull String source) {
        return this.getReplaced(NightMessage.from(source));
    }

    @NotNull
    public TextRoot getReplaced(@NotNull TextRoot source) {
        TextRoot root = source.copy();
        this.getReplacers().forEach(root::replace);
        return root;
    }

    @NotNull
    public Replacer replacePlaceholderAPI(@NotNull Player player) {
        if (!Plugins.hasPlaceholderAPI()) return this;

        return this.replace(line -> PlaceholderAPI.setPlaceholders(player, line));
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Consumer<List<String>> replacer) {
        List<String> list = new ArrayList<>();
        replacer.accept(list);

        return this.replace(key, list);
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull List<String> replacer) {
        this.placeholders.add(key, () -> String.join(Placeholders.TAG_LINE_BREAK, replacer));
        return this;
    }

    @NotNull
    public <T> Replacer replace(@NotNull T source, @NotNull PlaceholderList<T> placeholders) {
        return this.replace(placeholders.replacer(source));
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Supplier<String> value) {
        this.placeholders.add(key, value);
        return this;
    }

    @NotNull
    public Replacer replace(@NotNull String key, @NotNull Object value) {
        this.placeholders.add(key, String.valueOf(value));
        return this;
    }

    @NotNull
    public Replacer replace(@NotNull UnaryOperator<String> replacer) {
        this.replacers.add(replacer);
        return this;
    }
}
