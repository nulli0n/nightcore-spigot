package su.nightexpress.nightcore.util.text.night;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.LegacyColors;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.tag.TagHandler;
import su.nightexpress.nightcore.util.text.night.tag.TagHandlerRegistry;
import su.nightexpress.nightcore.util.text.night.tag.TagPool;
import su.nightexpress.nightcore.util.text.night.wrapper.SimpleTagWrapper;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrapper;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class TextParser {

    public static final String ROOT_NAME = "root";

    private final TagPool tagPool;
    private final ParserMode mode;
    private final boolean legacyMode;

    private String        string;
    private EntryGroup    currentGroup;
    private StringBuilder eater;

    private NightComponent component;

    TextParser(@NotNull String string, @NotNull ParserMode mode, @NotNull TagPool tagPool) {
        this.mode = mode;
        this.tagPool = tagPool;
        this.legacyMode = true;// TODO CoreConfig.LEGACY_COLOR_SUPPORT.get();
        this.setString(string);

        this.eater = new StringBuilder();
        this.currentGroup = new EntryGroup(ROOT_NAME);
    }

    @NotNull
    public static NightComponent parse(@NotNull String string) {
        return parse(string, TagPool.ALL);
    }

    @NotNull
    public static NightComponent parse(@NotNull String string, @NotNull TagPool tagPool) {
        return runParser(string, ParserMode.PARSE, tagPool).component;
    }

    @NotNull
    public static String strip(@NotNull String string) {
        return strip(string, TagPool.NONE);
    }

    @NotNull
    public static String strip(@NotNull String string, @NotNull TagPool tagPool) {
        return runParser(string, ParserMode.STRIP, tagPool).eater.toString();
    }

    @NotNull
    private static TextParser runParser(@NotNull String string, @NotNull ParserMode mode, @NotNull TagPool tagPool) {
        TextParser parser = new TextParser(string, mode, tagPool);
        parser.run();
        return parser;
    }

    private void setString(@NotNull String string) {
        if (this.legacyMode) {
            string = LegacyColors.plainColors(string); // Translate legacy colors to plain values: '§a' -> '&a', '§x§f§f§f§f§f§f' -> '#ffffff'.
            string = ParserUtils.wrapHexCodesAsTags(string); // Wrap HEX colors in tags: '#ffffff' -> '<#ffffff>'.
        }

        this.string = string;
    }

    @NotNull
    public EntryGroup downward(@NotNull String childrenName) {
        this.consumeEaten();
        this.currentGroup = this.currentGroup.downward(childrenName);
        return this.currentGroup;
    }

    public void jumpToRoot() {
        this.upward(ROOT_NAME); // To root's children group.
        this.upward(); // To the root group from children.
    }

    @NotNull
    public EntryGroup backTo(@NotNull String parentName) {
        this.consumeEaten();
        this.currentGroup = this.currentGroup.backTo(parentName);
        return this.currentGroup;
    }

    @NotNull
    public EntryGroup upward(@NotNull String parentName) {
        this.consumeEaten();
        this.currentGroup = this.currentGroup.upward(parentName);
        return this.currentGroup;
    }

    @NotNull
    public EntryGroup upward() {
        this.consumeEaten();
        this.currentGroup = this.currentGroup.upward();
        return this.currentGroup;
    }

    public void consumeEaten() {
        if (this.mode == ParserMode.STRIP) return;

        if (!this.eater.isEmpty()) {
            this.currentGroup.appendTextEntry(this.eater.toString());
        }
        this.eater = new StringBuilder();
    }

    private void eat(char c) {
        this.eater.append(c);
    }

    private void eat(@NotNull String string) {
        this.eater.append(string);
    }

    public void run() {
        int length = string.length();
        int lastIndex = length - 1;
        int tagEndIndex = -1;
        boolean isEscaped = false;
        boolean isTagEntered = false;
        boolean isTagClosing = false;
        boolean isLegacyChar = false;

        for (int index = 0; index < length; index++) {
            char letter = string.charAt(index);

            // Skip escaped characters \b and tags <\b> </\b>.
            if (isEscaped) {
                isEscaped = false;
                isTagEntered = false;
                isTagClosing = false;
                isLegacyChar = false;
                this.eat(letter);
                continue;
            }

            // Mark escaped character and move.
            if (letter == ParserUtils.ESCAPE) {
                isEscaped = true;

                // Keep escaped characters for the STRIP mode to make the String reusable.
                if (this.mode == ParserMode.STRIP) {
                    this.eat(letter);
                }
                continue;
            }

            if (!isTagEntered) {
                if (!isLegacyChar) {
                    if (letter == LegacyColors.ALT_COLOR_CHAR && this.legacyMode) {
                        isLegacyChar = true;
                        isTagEntered = true;
                        continue;
                    }

                    // Look only for open tag brackets and not on the string's end.
                    if (letter != ParserUtils.OPEN_BRACKET || index == lastIndex) {
                        this.eat(letter);
                        continue;
                    }

                    // There is no closing brackets at all, skip.
                    tagEndIndex = ParserUtils.findUnescapedUnquotedUnprecededByChar(this.string, ParserUtils.CLOSE_BRACKET, ParserUtils.OPEN_BRACKET, index + 1);
                    if (tagEndIndex == -1) {
                        this.eat(letter);
                        continue;
                    }
                }

                // Mark to proceed the found tag.
                isTagEntered = true;
                continue;
            }

            if (!isTagClosing) {
                // Mark found tag as closing tag.
                if (letter == ParserUtils.CLOSE_SLASH) {
                    isTagClosing = true;
                    continue;
                }
            }

            SimpleTagWrapper legacyWrapper = null;
            if (isLegacyChar) {
                legacyWrapper = ParserUtils.legacyToNamedWrapper(letter);
                if (legacyWrapper == null) {
                    isTagEntered = false;
                    isLegacyChar = false;
                    this.eat(LegacyColors.ALT_COLOR_CHAR);
                    this.eat(letter);
                    continue;
                }
            }

            String bracketsContent;
            if (legacyWrapper != null) {
                bracketsContent = legacyWrapper.openingNoBrackets();
                tagEndIndex = index;
            }
            else {
                bracketsContent = this.string.substring(index, tagEndIndex);
            }
            if (bracketsContent.startsWith("#")) {
                bracketsContent = TagWrappers.COLOR.with(bracketsContent).openingNoBrackets(); // Append 'color:' tag prefix before raw hex code to handle it as color tag.
            }

            String tagName;
            String tagContent;

            int semicolonIndex = bracketsContent.indexOf(ParserUtils.DELIMITER);
            if (semicolonIndex >= 0) {
                tagName = LowerCase.INTERNAL.apply(bracketsContent.substring(0, semicolonIndex));
                tagContent = bracketsContent.substring(semicolonIndex + 1);
            }
            else {
                tagName = LowerCase.INTERNAL.apply(bracketsContent);
                tagContent = null;
            }

            TagHandler handler = TagHandlerRegistry.create(tagName);
            if (handler == null) {
                // Pretend that there was no tag detection and continue from the same index to parse "false tag content" normally.
                index--;
                isTagEntered = false;
                this.eat(ParserUtils.OPEN_BRACKET);
                continue;
            }
            else if (this.tagPool.isGoodTag(handler)) {
                if (this.mode == ParserMode.STRIP) {
                    this.eat(isTagClosing ? TagWrapper.simple(tagName).closing() : TagWrapper.simple(bracketsContent).opening());
                }
                else if (this.mode == ParserMode.PARSE) {
                    String groupName = LowerCase.INTERNAL.apply(tagName);
                    if (isTagClosing) {
                        this.backTo(groupName); // Jump to the closing tag's group + Append all text comes before the closing tag.

                        ParserResolvers resolvers = this.currentGroup.getResolvers();
                        TagHandler resolver = resolvers.removeFor(tagName);

                        if (resolver != null && resolver.canBeClosed()) {
                            resolver.handleClose(this.currentGroup);
                            this.upward(); // Then jump upward from the tag's group to it's parent (up to the root group).
                        }
                    }
                    else {
                        this.downward(groupName); // Create new tag group inside current's one.

                        ParserResolvers resolvers = this.currentGroup.getResolvers();
                        TagHandler resolver = resolvers.createFor(tagName);
                        if (resolver != null) {
                            resolver.handleOpen(this.currentGroup, tagContent);

                            if (!resolver.canBeClosed()) {
                                this.upward(); // Jump back to the initial group.
                            }
                        }
                    }
                }
            }

            isTagEntered = false;
            isTagClosing = false;
            isLegacyChar = false;
            index = tagEndIndex; // Move caret to the tag's opening end.
        }

        // Jump to the root group to search nested groups properly.
        this.jumpToRoot();

        // Simulate tag closing to handle some tags properly (e.g. gradients that applies on tag end).
        this.currentGroup.getChildGroups().forEach(EntryGroup::closeResolvers);

        // Jump to the root group back to end parsing properly.
        this.jumpToRoot();

        this.component = this.currentGroup.toComponent();

        // Clean memory
        this.currentGroup = null;
    }

    @NotNull
    public String getString() {
        return this.string;
    }

    @NotNull
    public EntryGroup getCurrentGroup() {
        return this.currentGroup;
    }
}
