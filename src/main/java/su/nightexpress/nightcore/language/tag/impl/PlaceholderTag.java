package su.nightexpress.nightcore.language.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;

public class PlaceholderTag extends MessageTag {

    public PlaceholderTag() {
        super("papi");
    }

    @Override
    public void apply(@NotNull MessageOptions options, @Nullable String tagContent) {
        options.setUsePlaceholderAPI(true);
    }
}
