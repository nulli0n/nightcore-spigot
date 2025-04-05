package su.nightexpress.nightcore.util.text;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.TagPool;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.Tags;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.PlaceholderTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;
import su.nightexpress.nightcore.util.text.tag.impl.ResetTag;
import su.nightexpress.nightcore.util.text.tag.impl.ShortHexColorTag;
import su.nightexpress.nightcore.util.text.tag.impl.TranslationTag;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class TextRoot {

    public static final String ROOT_NAME = "root";

    private final TagPool tagPool;

    private String    string;
    private TextGroup rootGroup;
    private TextGroup currentGroup;
    private TextNode  currentNode;

    private NightComponent component;

    public TextRoot(@NotNull String string, @NotNull TagPool tagPool) {
        this.tagPool = tagPool;
        this.setString(string);
    }

    public void setString(@NotNull String string) {
        if (CoreConfig.LEGACY_COLOR_SUPPORT.get()) {
            string = TagUtils.tagPlainHex(Colorizer.plain(string));
            string = TagUtils.replaceLegacyColors(string);
        }

        this.string = string;
        this.component = null;
    }

    @NotNull
    public TextRoot copy() {
        TextRoot copy = new TextRoot(this.string, this.tagPool);
        if (this.component != null) {
            copy.component = this.component.duplicate();
        }
        return copy;
    }

    @NotNull
    public TextRoot compile() {
        this.parseIfAbsent();
        return this;
    }

    @NotNull
    public TextRoot recompile() {
        this.parse();
        return this;
    }

    @NotNull
    public String toLegacy() {
        this.parseIfAbsent();

        //if (this.string.isBlank() || isEmpty(this.component)) return "";
        if (this.string.isBlank() || this.component.isEmpty()) return "";

        return this.component.toLegacy();
        //return TextComponent.toLegacyText(this.parseIfAbsent());
    }

    @NotNull
    public String toJson() {
        return this.parseIfAbsent().toJson();
        //return ComponentSerializer.toString(this.parseIfAbsent());
    }

    @NotNull
    @Deprecated
    public NightComponent toComponent() {
        return this.parseIfAbsent();
        //return TextComponent.fromArray(this.parseIfAbsent());
    }

    @NotNull
    @Deprecated
    public TextRoot replace(@NotNull String what, @NotNull Object object) {
        return this.replace(str -> str.replace(what, String.valueOf(object)));
    }

    @NotNull
    public TextRoot replace(@NotNull UnaryOperator<String> operator) {
        this.setString(operator.apply(this.string));
        return this;
    }

    public void send(@NotNull CommandSender... senders) {
        this.parseIfAbsent();
        for (CommandSender sender : senders) {
            this.component.send(sender);
            //sender.spigot().sendMessage(this.component);
        }
    }

    @NotNull
    public TextGroup toParentGroup() {
        this.currentGroup = this.currentGroup.getParent();
        if (this.currentGroup == null) this.currentGroup = this.rootGroup;

        return this.currentGroup;
    }

    @NotNull
    public TextGroup toParentGroup(@NotNull String name) {
        TextGroup lastGroup = this.currentGroup;

        while (this.currentGroup != null && !this.currentGroup.getName().equalsIgnoreCase(name)) {
            this.currentGroup = this.currentGroup.getParent();
        }
        if (this.currentGroup == null && lastGroup != null) {
            this.currentGroup = lastGroup;
            return this.currentGroup;
        }

        if (this.currentGroup != null) this.currentGroup = this.currentGroup.getParent();
        if (this.currentGroup == null) this.currentGroup = this.rootGroup;

        return this.currentGroup;
    }

    @NotNull
    public TextNode currentNode() {
        if (this.currentNode == null) this.currentNode = this.currentGroup.createNode();

        return this.currentNode;
    }

    public NightComponent parseIfAbsent() {
        return this.component == null ? this.parse() : this.component;
    }

    public enum Mode {
        PARSE, STRIP
    }

    @NotNull
    public NightComponent parse() {
        this.rootGroup = new TextGroup(ROOT_NAME);
        this.currentGroup = this.rootGroup;

        this.doJob(Mode.PARSE, letter -> this.currentNode().append(letter));

        this.component = this.rootGroup.toComponent();

        // Clean memory
        this.rootGroup = null;
        this.currentGroup = null;
        this.currentNode = null;

        return this.component;
    }

    @NotNull
    public String strip() {
        StringBuilder builder = new StringBuilder();

        this.doJob(Mode.STRIP, builder::append);

        return builder.toString();
    }

    private void doJob(@NotNull Mode mode, @NotNull Consumer<Character> consumer) {
        int length = string.length();
        for (int index = 0; index < length; index++) {
            char letter = string.charAt(index);

            Tag:
            if (letter == TagUtils.OPEN_BRACKET && index != (length - 1)) {
                int indexEnd = indexOfIgnoreEscaped(string, TagUtils.CLOSE_BRACKET, index);//string.indexOf(Tag.CLOSE_BRACKET, index);
                //System.out.println("indexEnd = " + indexEnd);
                if (indexEnd == -1) break Tag;

                char next = string.charAt(index + 1);
                if (next == TagUtils.CLOSE_BRACKET) break Tag;

                boolean closeTag = false;
                if (next == TagUtils.CLOSE_SLASH) {
                    closeTag = true;
                    index++;
                }

                String bracketsContent = string.substring(index + 1, indexEnd);
                //System.out.println("bracketsContent = " + bracketsContent);

                String tagName = bracketsContent;
                String tagContent = null;

                // Check for content tags
                int semicolonIndex = bracketsContent.indexOf(TagUtils.SEMICOLON);
                if (semicolonIndex >= 0) {
                    tagName = bracketsContent.substring(0, semicolonIndex);
                    tagContent = bracketsContent.substring(semicolonIndex + 1);
                    //System.out.println("tagName = " + tagName);
                    //System.out.println("tagContent = " + tagContent);
                }
                else if (tagName.startsWith(ShortHexColorTag.NAME)) {
                    tagName = ShortHexColorTag.NAME;
                    tagContent = bracketsContent;
                }

                Tag tag = Tags.getTag(tagName);
                //System.out.println("found tag = " + tag);

                if (tag != null) {
                    if (mode == Mode.PARSE) {
                        if (tagPool.isGoodTag(tag)) {
                            this.proceedTag(tag, closeTag, tagContent);
                        }
                        index = indexEnd;
                        continue;
                    }
                    else {
                        if (!tagPool.isGoodTag(tag)) {
                            index = indexEnd;
                            continue;
                        }
                    }
                }

                // Move cursor back to '/' of invalid closing tag.
                if (closeTag) {
                    index--;
                }
            }

            consumer.accept(letter);
        }
    }

    private void proceedTag(@NotNull Tag tag, boolean closeTag, @Nullable String tagContent) {
        if (closeTag && !tag.isCloseable()) return;

        if (tag instanceof PlaceholderTag placeholderTag) {
            //if (!closeTag) {
                this.currentNode().append(placeholderTag.getValue()); // Insert it to the current node as if it was a regular text.
            //}
            return;
        }
        if (tag instanceof TranslationTag) {
            if (tagContent != null) {
                // Create a new node exclusively for translation text.
                TextNode node = this.currentGroup.createNode();
                node.append(TagUtils.unquoted(tagContent));
                node.setTranslation(true);
                //this.currentGroup.createNode().setTranslation(TagUtils.unquoted(tagContent));
                this.currentNode = null; // Reset current node so parser will create a new one after this one with translation.
            }
            return;
        }

        // Reset current node to create a new one inside for a group jumped/created below.
        this.currentNode = null;

        if (closeTag) {
            this.toParentGroup(tag.getName()); // Jump to the first parent group of closed tag (or root group if there is no parent for this tag).
            return;
        }

        if (tag instanceof ResetTag) {
            this.currentGroup = this.rootGroup; // Jump to the root group for 'Reset' tag, so it will create new children groups without decorators on further parsing.
            return;
        }

        this.currentGroup = this.currentGroup.createChildren(tag.getName()); // Create children tag group inside the current one.

        Decorator decorator = null;

        if (tag instanceof ContentTag contentTag && tagContent != null) {
            decorator = contentTag.parse(tagContent);
        }
        else if (tag instanceof Decorator tagDecorator) {
            decorator = tagDecorator;
        }

        if (decorator != null) {
            this.currentGroup.addDecorator(decorator);
        }
    }

    public String getString() {
        return string;
    }

    public TextGroup getRootGroup() {
        return rootGroup;
    }

    public NightComponent getComponent() {
        return component;
    }

//    public static boolean isEmpty(@NotNull BaseComponent component) {
//        return component instanceof TextComponent textComponent && textComponent.getText().isBlank() && textComponent.getExtra() == null;
//    }

    public static int indexOfIgnoreEscaped(@NotNull String string, char what, int from) {
        int length = string.length();
        if (from >= length) return -1;

        Character foundQuote = null;
        int quoteCount = 0;

        for (int index = from; index < length; index++) {
            char letter = string.charAt(index);
            if (letter == '\\') {
                index++;
                continue;
            }

            if (letter == '\'' || letter == '"') {
                quoteCount++;
                if (foundQuote != null) {
                    if (foundQuote == letter) {
                        foundQuote = null;
                    }
                }
                else foundQuote = letter;
            }

            if (letter == what) {
                if (foundQuote != null && quoteCount < 2) continue;
                return index;
            }
        }

        return -1;
    }

    /*@NotNull
    public static String substringOfQuotes(@NotNull String string) {
        char quote = string.charAt(0);
        if (quote != '\'' && quote != '"') return string;
        if (string.length() < 3) return string;

        //int indexEnd = -1;
        for (int index = 1; index < string.length(); index++) {
            char letter = string.charAt(index);
            if (letter == '\\') {
                index++;
                continue;
            }
            if (letter == quote) {
                return string.substring(1, index);
            }
        }
        //if (indexEnd == -1) return string;

        return string;//.substring(1, indexEnd);
    }*/
}
