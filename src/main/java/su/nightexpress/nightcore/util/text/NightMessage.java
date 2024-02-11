package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.regex.TimedMatcher;
import su.nightexpress.nightcore.util.text.decoration.*;
import su.nightexpress.nightcore.util.text.tag.*;
import su.nightexpress.nightcore.util.text.tag.api.DynamicTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.impl.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class NightMessage {

    private static final Map<String, Tag> TAG_MAP = new HashMap<>();

    static {
        registerTags(
            Tags.BLACK, Tags.WHITE, Tags.GRAY, Tags.GREEN,
            Tags.YELLOW, Tags.ORANGE, Tags.RED,
            Tags.BLUE, Tags.CYAN, Tags.PURPLE, Tags.PINK,

            Tags.DARK_GRAY, Tags.LIGHT_GRAY, Tags.LIGHT_GREEN,
            Tags.LIGHT_YELLOW, Tags.LIGHT_ORANGE, Tags.LIGHT_RED,
            Tags.LIGHT_BLUE, Tags.LIGHT_CYAN, Tags.LIGHT_PURPLE, Tags.LIGHT_PINK
        );

        registerTags(Tags.BOLD, Tags.ITALIC, Tags.OBFUSCATED, Tags.STRIKETHROUGH, Tags.UNDERLINED);

        registerTags(
            Tags.FONT, Tags.HEX_COLOR, Tags.GRADIENT,
            Tags.HOVER, Tags.CLICK,
            Tags.LINE_BREAK, Tags.RESET
        );
    }

    @NotNull
    public static Collection<Tag> getTags() {
        return TAG_MAP.values();
    }

    public static void registerTags(@NotNull Tag... tags) {
        for (Tag tag : tags) {
            registerTag(tag);
        }
    }

    public static void registerTag(@NotNull Tag tag, @NotNull String... aliases) {
        TAG_MAP.put(tag.getName(), tag);
        for (String alias : aliases) {
            TAG_MAP.put(alias, tag);
        }
    }

    @Nullable
    public static Tag getTag(@NotNull String name) {
        return TAG_MAP.get(name.toLowerCase());
    }

    @NotNull
    public static String asLegacy(@NotNull String string) {
        return create(string).toLegacy();
    }

    @NotNull
    public static List<String> asLegacy(@NotNull List<String> string) {
        List<String> list = new ArrayList<>();
        for (String str : string) {
            for (String br : str.split(Placeholders.TAG_LINE_BREAK)) {
                list.add(asLegacy(br));
            }
        }
        return list;
    }

    /**
     *
     * @param string Text to deserialize.
     * @return Precompiled WrappedMessage object.
     */
    @NotNull
    public static WrappedMessage create(@NotNull String string) {
        return create(string, TagPool.ALL);
    }

    @NotNull
    public static WrappedMessage from(@NotNull String string) {
        return from(string, TagPool.ALL);
    }

    @NotNull
    public static WrappedMessage create(@NotNull String string, @NotNull TagPool tagPool) {
        return from(string, tagPool).compile();
    }

    @NotNull
    public static WrappedMessage from(@NotNull String string, @NotNull TagPool tagPool) {
        return new WrappedMessage(string, tagPool);
    }



    public static BaseComponent[] parse(@NotNull String string) {
        return parse(string, TagPool.ALL);
    }

    public static BaseComponent[] parse(@NotNull String string, @NotNull TagPool tagPool) {
        if (CoreConfig.LEGACY_COLOR_SUPPORT.get()) {
            TimedMatcher timedMatcher = TimedMatcher.create(Colorizer.PATTERN_HEX_LEGACY, string);
            Set<String> rawCodes = new HashSet<>();
            while (timedMatcher.find()) {
                String hex = timedMatcher.getMatcher().group(1);
                rawCodes.add(hex);
            }
            for (String hex : rawCodes) {
                string = string.replace(hex, Tag.brackets(hex));
            }
        }

        List<WrappedText> parts = new ArrayList<>();

        WrappedText currentText = new WrappedText();
        parts.add(currentText);

        Tag lastTag = null;

        int length = string.length();
        for (int index = 0; index < length; index++) {
            char letter = string.charAt(index);

            Tag:
            if (letter == Tag.OPEN_BRACKET && index != (length - 1)) {
                int indexEnd = string.indexOf(Tag.CLOSE_BRACKET, index);
                if (indexEnd == -1) break Tag;

                char next = string.charAt(index + 1);
                if (next == Tag.CLOSE_BRACKET) break Tag;

                boolean closeTag = false;
                if (next == Tag.CLOSE_MARK) {
                    closeTag = true;
                    index++;
                }

                String leading = string.substring(index + 1);
                String brackets = string.substring(index + 1, indexEnd);

                Tag tag = getTag(brackets);
                Decorator decorator = null;
                //System.out.println("brackets = " + brackets);

                if (tag == null) {
                    for (Tag registered : getTags()) {
                        String tagName = registered.getName();
                        if (leading.startsWith(tagName)) {
                            char latest = leading.charAt(tagName.length());

                            if (latest == Tag.CLOSE_BRACKET || registered instanceof DecoratorParser) {
                                tag = registered;
                                break;
                            }
                        }
                    }
                }
                //System.out.println("found tag = " + tag);

                //if (tag == null) break Tag;
                /*if (tag != null && !tagPool.isGoodTag(tag)) {
                    break Tag;
                }*/

                if (!closeTag) {
                    // If tag is variable like hex codes or hover/click events.
                    if (tag instanceof DecoratorParser parser) {
                        // If tag has no dynamic content, strip it to the next close bracket.
                        if (!(tag instanceof DynamicTag)) {
                            leading = brackets;
                        }
                        //System.out.println("tag = " + tag.getName());
                        //System.out.println("content = " + leading);
                        ParsedDecorator parsedDecorator = parser.parse(leading);
                        if (parsedDecorator != null) {
                            decorator = parsedDecorator.getDecorator();
                            indexEnd = index + parsedDecorator.getLength() + 1; // 1 for close bracket
                        }
                    }
                    else if (tag instanceof Decorator textDecorator) {
                        decorator = textDecorator;
                    }
                }

                if (tag != null) {
                    if (tagPool.isGoodTag(tag)) {
                        if (tag == Tags.LINE_BREAK) {
                            if (currentText.getTextBuilder().isEmpty()) {
                                currentText.getTextBuilder().append("\n");
                            }
                            else {
                                currentText = currentText.nested();
                                currentText.setText("\n");
                                parts.add(currentText);
                            }
                            currentText = currentText.nested();
                            parts.add(currentText);
                        }

                        // Если был текст ДО текущего тега и не было ДРУГИХ тегов прямо перед ним,
                        // то вставляем новую часть текста
                        if (lastTag == null && !currentText.getTextBuilder().isEmpty()) {
                            currentText = currentText.nested();
                            parts.add(currentText);
                        }

                        if (closeTag) {
                            currentText.removeLatestTag(tag);
                        }
                        else if (tag != Tags.LINE_BREAK) {
                            currentText.addTag(tag, decorator);
                        }

                        lastTag = tag;
                    }
                    index = indexEnd;
                    continue;
                }

                // Move cursor back to '/' of invalid closing tag.
                if (closeTag) {
                    index--;
                }
            }
            currentText.getTextBuilder().append(letter);
            lastTag = null;
        }

        //System.out.println("parsedTexts = " + parts);

        parts.removeIf(wrappedText -> wrappedText.getText().isEmpty());

        fixGradients(parts);

        ComponentBuilder builder = new ComponentBuilder();
        for (WrappedText wrappedText : parts) {
            if (CoreConfig.LEGACY_COLOR_SUPPORT.get()) {
                wrappedText.setText(Colorizer.apply(wrappedText.getText()));
            }
            builder.append(wrappedText.toComponent(), ComponentBuilder.FormatRetention.NONE);
        }

        return builder.create();
    }

    // Fix gradient smoothness when multiple WrappedTexts have the same gradient colors, but different tag lists.
    private static void fixGradients(@NotNull List<WrappedText> parts) {
        if (parts.size() < 2) return;

        List<Pair<GradientDecorator, List<WrappedText>>> pairs = new ArrayList<>();

        Pair<GradientDecorator, List<WrappedText>> currentList = null;
        GradientDecorator lastGradient = null;
        int lastIndex = -1;

        // Create a list of sequental parts with the same gradient.
        for (int index = 0; index < parts.size(); index++) {
            WrappedText text = parts.get(index);
            GradientDecorator gradient = (GradientDecorator) text.getDecorator(GradientTag.NAME);

            // Check for next list
            if (gradient != null) {
                boolean bigStep = (index - lastIndex) > 1;
                boolean sameGradient = lastGradient != null && gradient.isSimilar(lastGradient);
                boolean lastElement = (index == parts.size() - 1);
                if (bigStep || !sameGradient || lastElement) {
                    if (currentList != null && currentList.getSecond().size() > 1) {
                        pairs.add(currentList);
                    }
                    currentList = Pair.of(gradient, new ArrayList<>());
                }
                currentList.getSecond().add(text);
            }
            else if (currentList != null) {
                pairs.add(currentList);
                currentList = null;
            }

            lastGradient = gradient;
            lastIndex = index;
        }
        //System.out.println("pairs = " + pairs);

        // Recreate Gradient Decorator with shifted colors depends on text length.
        for (var pair : pairs) {
            GradientDecorator gradient = pair.getFirst();
            List<WrappedText> texts = pair.getSecond();

            int length = texts.stream().mapToInt(text -> text.getText().length()).sum();

            Color[] colors = gradient.createGradient(length);
            int shift = 0;

            for (WrappedText text : texts) {
                if (shift >= colors.length) break;

                int textLength = text.getText().length();

                Color start = colors[shift];
                Color end = colors[textLength + shift - 1];

                GradientDecorator decorator = new GradientDecorator(start, end);
                text.getDecoratorMap().put(GradientTag.NAME, decorator);

                shift += textLength;
            }
        }
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<WrappedText> texts;

        public Builder() {
            this.texts = new ArrayList<>();
        }

        public BaseComponent[] create() {
            ComponentBuilder builder = new ComponentBuilder();
            for (WrappedText wrappedText : this.texts) {
                builder.append(wrappedText.toComponent(), ComponentBuilder.FormatRetention.NONE);
            }
            return builder.create();
        }

        public void send(@NotNull CommandSender sender) {
            sender.spigot().sendMessage(this.create());
        }

        public Builder append(@NotNull WrappedText.Builder textBuilder) {
            this.texts.add(textBuilder.getText());
            return this;
        }

        public WrappedText.Builder append(@NotNull String text, @NotNull Tag... tags) {
            WrappedText.Builder builder = WrappedText.builder(text);
            this.texts.add(builder.getText());
            for (Tag tag : tags) {
                builder.tag(tag);
            }
            return builder;
        }

        public Builder appendLineBreak() {
            return this.append(WrappedText.builder("\n"));
        }
    }
}
