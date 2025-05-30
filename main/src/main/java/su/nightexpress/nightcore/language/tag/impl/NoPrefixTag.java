package su.nightexpress.nightcore.language.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;

public class NoPrefixTag extends MessageTag {

    public NoPrefixTag() {
        super("noprefix");
    }

    @Override
    public void apply(@NotNull MessageOptions options, @Nullable String tagContent) {
        options.setHasPrefix(false);
    }
}
