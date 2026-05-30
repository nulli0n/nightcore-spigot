package su.nightexpress.nightcore.language.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;

@Deprecated
public class PlaceholderTag extends MessageTag {

    public PlaceholderTag() {
        super("papi");
    }

    @Override
    public void apply(@NonNull MessageOptions options, @Nullable String tagContent) {
        options.setUsePlaceholderAPI(true);
    }
}
