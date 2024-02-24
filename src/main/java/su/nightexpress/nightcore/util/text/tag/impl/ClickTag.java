package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.decoration.ClickEventDecorator;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.decoration.ParsedDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;

public class ClickTag extends ContentTag {

    public static final String NAME = "click";

    public ClickTag() {
        super(NAME);
    }

    @Override
    public int getWeight() {
        return 50;
    }

    @NotNull
    public String enclose(@NotNull ClickEvent.Action action, @NotNull String text, @NotNull String command) {
        String actionName = action.name().toLowerCase();
        return this.enclose(actionName, command, text);
    }

    @Override
    @Nullable
    public ParsedDecorator onParse(@NotNull String sub) {
        ClickEvent.Action action = null;
        for (ClickEvent.Action global : ClickEvent.Action.values()) {
            if (sub.startsWith(global.name().toLowerCase())) {
                action = global;
                break;
            }
        }
        if (action == null) return null;

        int prefixSize = action.name().toLowerCase().length() + 1; // 1 for ':', like "run_command:"
        sub = sub.substring(prefixSize);

        String content = StringUtil.parseQuotedContent(sub);
        if (content == null) return null;

        int length = prefixSize + content.length();// + 2; // 2 for quotes

        Decorator decorator = new ClickEventDecorator(action, content);

        return new ParsedDecorator(decorator, length);
    }
}
