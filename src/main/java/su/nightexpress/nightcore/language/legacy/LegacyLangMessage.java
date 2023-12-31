package su.nightexpress.nightcore.language.legacy;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.language.message.OutputType;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.message.NexParser;
import su.nightexpress.nightcore.util.regex.TimedMatcher;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Deprecated
public class LegacyLangMessage {

    private static final Pattern OPTIONS_PATTERN = Pattern.compile("<\\!(.*?)\\!>");

    private final NightCorePlugin plugin;
    private final LegacyMessageOptions  options;
    private final List<String>    defaultText;

    private List<String> text;

    public LegacyLangMessage(@NotNull NightCorePlugin plugin, @NotNull List<String> defaultText) {
        this.plugin = plugin;
        this.options = new LegacyMessageOptions();
        this.defaultText = defaultText;
        this.setText(defaultText);
        this.setOptions();
    }

    private LegacyLangMessage(@NotNull LegacyLangMessage from) {
        this.plugin = from.plugin;
        this.options = from.options.copy();
        this.defaultText = new ArrayList<>(from.defaultText);
        this.text = new ArrayList<>(from.text);
    }

    private void setOptions() {
        List<String> text = this.getText();
        if (text.isEmpty()) return;

        String msg = text.get(0);
        TimedMatcher matcher = TimedMatcher.create(OPTIONS_PATTERN, msg);
        if (!matcher.find()) return;

        String optionTag = matcher.getMatcher().group(0);
        String optionArgs = matcher.getMatcher().group(1).trim();
        this.text.set(0, msg.replace(optionTag, ""));
        this.text.removeIf(String::isEmpty);
        this.options.read(optionArgs);
    }

    @NotNull
    public List<String> getDefaultText() {
        return this.defaultText;
    }

    @NotNull
    public List<String> getText() {
        return this.text;
    }

    @NotNull
    public List<String> getText(@NotNull CommandSender sender) {
        if (!this.options.hasPlaceholderAPI() || !(sender instanceof Player player)) return this.getText();

        List<String> text = new ArrayList<>(this.getText());
        text.replaceAll(line -> PlaceholderAPI.setPlaceholders(player, line));
        return text;
    }

    private void setText(@NotNull List<String> text) {
        this.text = Colorizer.apply(text);
        this.text.removeIf(String::isEmpty);
    }

    @NotNull
    public LegacyLangMessage replace(@NotNull String var, @NotNull Object replacer) {
        return this.replace(str -> str.replace(var, String.valueOf(replacer)));
    }

    @NotNull
    public LegacyLangMessage replace(@NotNull UnaryOperator<String> replacer) {
        if (this.isEmpty()) return this;

        LegacyLangMessage msgCopy = new LegacyLangMessage(this);
        msgCopy.getText().replaceAll(replacer);
        return msgCopy;
    }

    @NotNull
    public LegacyLangMessage replace(@NotNull Predicate<String> predicate, @NotNull BiConsumer<String, List<String>> replacer) {
        if (this.isEmpty()) return this;

        LegacyLangMessage msgCopy = new LegacyLangMessage(this);
        List<String> replaced = new ArrayList<>();
        msgCopy.getText().forEach(line -> {
            if (predicate.test(line)) {
                replacer.accept(line, replaced);
                return;
            }
            replaced.add(line);
        });
        msgCopy.setText(replaced);
        return msgCopy;
    }

    public boolean isEmpty() {
        return (this.options.getType() == OutputType.NONE || this.getText().isEmpty());
    }

    public void broadcast() {
        if (this.isEmpty()) return;

        Bukkit.getServer().getOnlinePlayers().forEach(this::send);
        this.send(Bukkit.getServer().getConsoleSender());
    }

    public void send(@NotNull CommandSender sender) {
        if (this.isEmpty()) return;

        if (this.options.getSound() != null && sender instanceof Player player) {
            UniSound.of(this.options.getSound()).play(player);
        }

        if (this.options.getType() == OutputType.CHAT) {
            String prefix = this.options.hasPrefix() ? plugin.getPrefix() : "";
            this.getText(sender).forEach(line -> {
                Players.sendRichMessage(sender, prefix + line);
            });
            return;
        }

        if (sender instanceof Player player) {
            if (this.options.getType() == OutputType.ACTION_BAR) {
                Players.sendActionBar(player, String.join(" ", this.getText(player)));
            }
            else if (this.options.getType() == OutputType.TITLES) {
                List<String> list = this.getText(player);
                String title = list.size() > 0 ? NexParser.toPlainText(list.get(0)) : "";
                String subtitle = list.size() > 1 ? NexParser.toPlainText(list.get(1)) : "";
                player.sendTitle(title, subtitle, this.options.getTitleTimes()[0], this.options.getTitleTimes()[1], this.options.getTitleTimes()[2]);
            }
        }
    }

    /*@NotNull
    public List<String> asList() {
        return this.asList(this.getText());
    }

    @NotNull
    public List<String> asList(@NotNull CommandSender sender) {
        return this.asList(this.getText(sender));
    }

    @NotNull
    private List<String> asList(@NotNull String localized) {
        return this.isEmpty() ? Collections.emptyList() : Stream.of(localized.split("\\\\n"))
            .filter(Predicate.not(String::isEmpty)).toList();
    }

    @NotNull
    @Deprecated
    public String normalizeLines() {
        return String.join("\n", this.asList());
    }*/
}
