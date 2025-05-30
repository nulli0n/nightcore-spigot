package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.ClickDecorator;

public class ClickTag extends Tag implements ContentTag {

    public static final String NAME = "click";

    public ClickTag() {
        super(NAME);
    }

    @NotNull
    @Deprecated
    public String encloseRun(@NotNull String text, @NotNull String command) {
        return this.wrapRunCommand(text, command);
        //return this.enclose(text, ClickEventType.RUN_COMMAND, command);
    }

//    @NotNull
//    @Deprecated
//    public String enclose(@NotNull ClickEventType action, @NotNull String text, @NotNull String content) {
//        return this.enclose(text, action, content);
//    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text, @NotNull ClickEventType action, @NotNull String content) {
//        String data = action.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quotedContent(content);
//
//        return TagUtils.wrapContent(this, text, data);
        return this.wrap(text, action, content);
    }

    @NotNull
    public String wrapRunCommand(@NotNull String string, @NotNull String command) {
        return this.enclose(string, ClickEventType.RUN_COMMAND, command);
    }

    @NotNull
    public String wrapSuggestCommand(@NotNull String string, @NotNull String command) {
        return this.enclose(string, ClickEventType.SUGGEST_COMMAND, command);
    }

    @NotNull
    public String wrapOpenRUL(@NotNull String string, @NotNull String url) {
        return this.enclose(string, ClickEventType.OPEN_URL, url);
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull ClickEventType type, @NotNull String content) {
        String data = type.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quoted(content);

        return TagUtils.wrapContent(this, string, data);
    }

    @Override
    @Nullable
    public ClickDecorator parse(@NotNull String tagContent) {
        ClickEventType action = null;
        for (ClickEventType global : ClickEventType.values()) {
            if (tagContent.startsWith(global.name().toLowerCase())) {
                action = global;
                break;
            }
        }
        if (action == null) return null;

        int prefixSize = action.name().toLowerCase().length() + 1; // 1 for ':', like "run_command:"
        tagContent = tagContent.substring(prefixSize);

        String value = TagUtils.unquoted(tagContent);

        return new ClickDecorator(action, value);
    }
}
