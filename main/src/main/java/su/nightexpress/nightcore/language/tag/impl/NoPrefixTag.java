package su.nightexpress.nightcore.language.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;

@Deprecated
public class NoPrefixTag extends MessageTag {

    public NoPrefixTag() {
        super("noprefix");
    }

    @Override
    public void apply(@NonNull MessageOptions options, @Nullable String tagContent) {
        options.setHasPrefix(false);
    }
}
