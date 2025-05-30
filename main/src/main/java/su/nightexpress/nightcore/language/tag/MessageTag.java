package su.nightexpress.nightcore.language.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public abstract class MessageTag extends Tag {

    public MessageTag(@NotNull String name) {
        super(name);
    }

    public MessageTag(@NotNull String name, @NotNull String[] aliases) {
        super(name, aliases);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String content) {
        return this.wrap(content);//brackets(this.getName() + ":\"" + content + "\"");
    }

    @NotNull
    public String wrap(@NotNull String content) {
        return TagUtils.brackets(this.getName() + TagUtils.SEMICOLON + TagUtils.quoted(content));// ":\"" + content + "\"");
    }

    public abstract void apply(@NotNull MessageOptions options, @Nullable String tagContent);
}
