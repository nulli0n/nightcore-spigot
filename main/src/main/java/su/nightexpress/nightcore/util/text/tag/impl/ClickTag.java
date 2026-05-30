package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.ClickDecorator;

@Deprecated
public class ClickTag extends Tag implements ContentTag {

    public static final String NAME = "click";

    public ClickTag() {
        super(NAME);
    }

    @NonNull
    @Deprecated
    public String encloseRun(@NonNull String text, @NonNull String command) {
        return this.wrapRunCommand(text, command);
        //return this.enclose(text, ClickEventType.RUN_COMMAND, command);
    }

    //    @NonNull
    //    @Deprecated
    //    public String enclose(@NonNull ClickEventType action, @NonNull String text, @NonNull String content) {
    //        return this.enclose(text, action, content);
    //    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull String text, @NonNull ClickEventType action, @NonNull String content) {
        //        String data = action.name().toLowerCase() + TagUtils.SEMICOLON + TagUtils.quotedContent(content);
        //
        //        return TagUtils.wrapContent(this, text, data);
        return this.wrap(text, action, content);
    }

    @NonNull
    public String wrapRunCommand(@NonNull String string, @NonNull String command) {
        return this.enclose(string, ClickEventType.RUN_COMMAND, command);
    }

    @NonNull
    public String wrapSuggestCommand(@NonNull String string, @NonNull String command) {
        return this.enclose(string, ClickEventType.SUGGEST_COMMAND, command);
    }

    @NonNull
    public String wrapOpenRUL(@NonNull String string, @NonNull String url) {
        return this.enclose(string, ClickEventType.OPEN_URL, url);
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull ClickEventType type, @NonNull String content) {
        String data = type.name().toLowerCase() + ParserUtils.DELIMITER + ParserUtils.quoted(content);

        return TagUtils.wrapContent(this, string, data);
    }

    @Override
    @Nullable
    public ClickDecorator parse(@NonNull String tagContent) {
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

        String value = ParserUtils.unquoted(tagContent);

        return new ClickDecorator(action, value);
    }
}
