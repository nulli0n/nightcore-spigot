package su.nightexpress.nightcore.language.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public abstract class MessageTag extends Tag {

    public MessageTag(@NotNull String name) {
        super(name);
    }

    public MessageTag(@NotNull String name, @NotNull String[] aliases) {
        super(name, aliases);
    }

    @NotNull
    public String enclose(@NotNull String content) {
        return brackets(this.getName() + ":\"" + content + "\"");
    }

    public abstract void apply(@NotNull MessageOptions options, @Nullable String tagContent);
}
