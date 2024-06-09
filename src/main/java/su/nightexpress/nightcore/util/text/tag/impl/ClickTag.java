package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.TextRoot;
import su.nightexpress.nightcore.util.text.tag.api.ComplexTag;
import su.nightexpress.nightcore.util.text.tag.decorator.ClickDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;

public class ClickTag extends ComplexTag implements ContentTag {

    public static final String NAME = "click";

    public ClickTag() {
        super(NAME);
    }

    @NotNull
    public String encloseRun(@NotNull String text, @NotNull String command) {
        return this.enclose(text, ClickEvent.Action.RUN_COMMAND, command);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull ClickEvent.Action action, @NotNull String text, @NotNull String content) {
        return this.enclose(text, action, content);
    }

    @NotNull
    public String enclose(@NotNull String text, @NotNull ClickEvent.Action action, @NotNull String content) {
        //content = content.replace("'", "\\'");

        //String tagOpen = brackets(NAME + ":" + action.name().toLowerCase() + ":'" + content + "'");
        //String tagClose = this.getClosingName();

        //return tagOpen + text + tagClose;

        String data = action.name().toLowerCase() + ":\"" + this.escapeQuotes(content) + "\"";

        return this.encloseContent(text, data);
    }

    @Override
    @Nullable
    public ClickDecorator parse(@NotNull String tagContent) {
        ClickEvent.Action action = null;
        for (ClickEvent.Action global : ClickEvent.Action.values()) {
            if (tagContent.startsWith(global.name().toLowerCase())) {
                action = global;
                break;
            }
        }
        if (action == null) return null;

        int prefixSize = action.name().toLowerCase().length() + 1; // 1 for ':', like "run_command:"
        tagContent = tagContent.substring(prefixSize);

        String value = TextRoot.stripQuotesSlash(tagContent);

        return new ClickDecorator(action, value);
    }
}
