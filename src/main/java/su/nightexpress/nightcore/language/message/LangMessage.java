package su.nightexpress.nightcore.language.message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.language.tag.MessageTag;
import su.nightexpress.nightcore.language.tag.MessageTags;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightSound;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class LangMessage {

    private final NightCorePlugin plugin;
    private final String          defaultText;
    private final MessageOptions  options;

    private TextRoot message;

    public LangMessage(@NotNull NightCorePlugin plugin, @NotNull String defaultText, @NotNull MessageOptions options) {
        this(plugin, defaultText, options, null);
    }

    public LangMessage(@NotNull NightCorePlugin plugin, @NotNull String defaultText, @NotNull MessageOptions options, @Nullable String prefix) {
        this.plugin = plugin;
        this.defaultText = defaultText;
        this.options = options;

        this.setPrefix(prefix);
//        this.message = NightMessage.from(prefix == null || !options.hasPrefix() ? defaultText : prefix + defaultText);
//        if (CoreConfig.MODERN_TEXT_PRECOMPILE_LANG.get() && options.getOutputType() != OutputType.TITLES) {
//            this.message.compile();
//        }
    }

    @NotNull
    public LangMessage setPrefix(@NotNull NightCorePlugin plugin) {
        return this.setPrefix(plugin.getPrefix());
    }

    @NotNull
    public LangMessage setPrefix(@Nullable String prefix) {
        boolean init = this.message == null;
        this.message = NightMessage.from(prefix == null || !options.hasPrefix() ? defaultText : prefix + defaultText);

        if (init && this.options.getOutputType() != OutputType.TITLES) {
            this.message.compile();
        }
        return this;
    }

    @Deprecated
    private LangMessage(@NotNull LangMessage from) {
        this.plugin = from.plugin;
        this.defaultText = from.defaultText;
        this.options = from.options.copy();
        this.message = from.message.copy();
    }

    @NotNull
    public static LangMessage parse(@NotNull NightCorePlugin plugin, @NotNull String string) {
        MessageOptions options = new MessageOptions();
        StringBuilder builder = new StringBuilder();

        // LEGACY STUFF START
        int legTagOpen = string.indexOf("<!");
        int legTagClose = string.indexOf("!>");
        if (legTagOpen >= 0 && legTagClose > legTagOpen) {
            String legContent = string.substring(legTagOpen, legTagClose + 2);
            string = string.substring(legTagClose + 2);

            String[] types = {"type", "sound", "prefix", "papi"};
            for (String type : types) {
                int typeIndex = legContent.indexOf(type + ":");
                if (typeIndex == -1) continue;

                String leading = legContent.substring(typeIndex + type.length() + 1);
                String typeContent = StringUtil.parseQuotedContent(leading);
                if (typeContent == null) continue;

                if (type.equalsIgnoreCase("type")) {
                    if (typeContent.equalsIgnoreCase("action_bar")) {
                        options.setOutputType(OutputType.ACTION_BAR);
                    }
                    else if (typeContent.equalsIgnoreCase("chat")) {
                        options.setOutputType(OutputType.CHAT);
                    }
                    else if (typeContent.equalsIgnoreCase("none")) {
                        options.setOutputType(OutputType.NONE);
                    }
                    else if (typeContent.startsWith("titles")) {
                        String[] split = typeContent.split(":");
                        int fadeIn = NumberUtil.getAnyInteger(split[1], 20);
                        int stay = NumberUtil.getAnyInteger(split[2], 60);
                        int fadeOut = NumberUtil.getAnyInteger(split[3], 20);
                        options.setOutputType(OutputType.TITLES);
                        options.setTitleTimes(new int[]{fadeIn, stay, fadeOut});
                    }
                }
                else if (type.equalsIgnoreCase("sound")) {
                    options.setSound(NightSound.deserialize(typeContent));
                }
                else if (type.equalsIgnoreCase("prefix")) {
                    boolean b = Boolean.parseBoolean(typeContent);
                    options.setHasPrefix(b);
                }
                else if (type.equalsIgnoreCase("papi")) {
                    boolean b = Boolean.parseBoolean(typeContent);
                    options.setUsePlaceholderAPI(b);
                }
            }
        }
        // LEGACY STUFF END

        int length = string.length();
        for (int index = 0; index < length; index++) {
            char letter = string.charAt(index);

            Tag:
            if (letter == TagUtils.OPEN_BRACKET && index != (length - 1)) {
                int indexEnd = TextRoot.indexOfIgnoreEscaped(string, TagUtils.CLOSE_BRACKET, index);
                if (indexEnd == -1) break Tag;

                char next = string.charAt(index + 1);
                if (next == TagUtils.CLOSE_BRACKET) break Tag;

                String bracketsContent = string.substring(index + 1, indexEnd);

                String tagName = bracketsContent;
                String tagContent = null;

                // Check for content tags
                int semicolonIndex = bracketsContent.indexOf(':');
                if (semicolonIndex >= 0) {
                    tagName = bracketsContent.substring(0, semicolonIndex);
                    tagContent = TagUtils.unquoted(bracketsContent.substring(semicolonIndex + 1));
                }

                MessageTag tag = MessageTags.getTag(tagName);

                if (tag != null) {
                    tag.apply(options, tagContent);
                    index = indexEnd;
                    continue;
                }
            }
            builder.append(letter);
        }

        String text = builder.toString();
        String prefix = options.getOutputType() == OutputType.CHAT && options.hasPrefix() ? plugin.getPrefix() : null;

        // Remove completely empty lines.
        // Especially useful for message tags lines without extra text.
        StringBuilder stripper = new StringBuilder();
        for (String part : Tags.LINE_BREAK.split(text)) {
            if (part.isEmpty()) continue;

            if (!stripper.isEmpty()) stripper.append(Placeholders.TAG_LINE_BREAK);
            stripper.append(part);
        }
        String strippedText = stripper.toString();

        return new LangMessage(plugin, strippedText, options, prefix);
    }

//    @NotNull
//    public LangMessage setPrefix(@NotNull String prefix) {
//        return new LangMessage(this.plugin, this.defaultText, this.options, prefix);
//    }

    @NotNull
    public String getDefaultText() {
        return this.defaultText;
    }

    @NotNull
    public TextRoot getMessage() {
        return this.message;
    }

    @NotNull
    public MessageOptions getOptions() {
        return this.options;
    }

    @NotNull
    @Deprecated
    public TextRoot getMessage(@NotNull CommandSender sender) {
        if (!this.options.usePlaceholderAPI() || !(sender instanceof Player player)) return this.getMessage();

        return this.message.copy().replace(line -> PlaceholderAPI.setPlaceholders(player, line));
    }

    @NotNull
    @Deprecated
    public LangMessage replace(@NotNull String var, @NotNull Object replacer) {
        return this.replace(str -> str.replace(var, String.valueOf(replacer)));
    }

    @NotNull
    @Deprecated
    public LangMessage replace(@NotNull String var, @NotNull Consumer<List<String>> replacer) {
        List<String> list = new ArrayList<>();
        replacer.accept(list);

        return this.replace(var, list);
    }

    @NotNull
    @Deprecated
    public LangMessage replace(@NotNull String var, @NotNull List<String> replacer) {
        return this.replace(str -> str.replace(var, String.join(Placeholders.TAG_LINE_BREAK, replacer)));
    }

    @NotNull
    @Deprecated
    public LangMessage replace(@NotNull UnaryOperator<String> replacer) {
        if (this.isDisabled()) return this;

        LangMessage copy = new LangMessage(this);
        copy.getMessage().replace(replacer);

        return copy;
    }

    public boolean isDisabled() {
        return this.options.getOutputType() == OutputType.NONE;
    }

    public void broadcast() {
        this.broadcast(null);
    }

    public void broadcast(@Nullable Consumer<Replacer> consumer) {
        if (this.isDisabled()) return;

        Players.getOnline().forEach(player -> this.send(player, consumer));
        this.send(plugin.getServer().getConsoleSender(), consumer);
    }

    public void send(@NotNull CommandSender sender) {
        this.send(sender, replacer -> {});
    }

    public void send(@NotNull CommandSender sender, @Nullable Consumer<Replacer> consumer) {
        if (this.isDisabled()) return;

        if (this.options.getSound() != null && sender instanceof Player player) {
            this.options.getSound().play(player);
        }

        Replacer replacer = new Replacer();
        if (consumer != null) consumer.accept(replacer);
        if (this.options.usePlaceholderAPI() && sender instanceof Player player) {
            replacer.replacePlaceholderAPI(player);
        }

        TextRoot message = replacer.apply(this.message);

        if (this.options.getOutputType() == OutputType.CHAT) {
            message.send(sender);
            return;
        }

        if (sender instanceof Player player) {
            if (this.options.getOutputType() == OutputType.ACTION_BAR) {
                Players.sendActionBar(player, message);
            }
            else if (this.options.getOutputType() == OutputType.TITLES) {
                String[] split = Tags.LINE_BREAK.split(message.getString());

                String title = split[0];//NightMessage.asLegacy(split[0]);
                String subtitle = split.length >= 2 ? split[1]/*NightMessage.asLegacy(split[1])*/ : "";

                //player.sendTitle(title, subtitle, this.options.getTitleTimes()[0], this.options.getTitleTimes()[1], this.options.getTitleTimes()[2]);
                Players.sendTitle(player, title, subtitle, this.options.getTitleTimes()[0], this.options.getTitleTimes()[1], this.options.getTitleTimes()[2]);
            }
        }
    }
}