package su.nightexpress.nightcore.language.tag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageDecorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class PlaceholderTag extends Tag implements MessageDecorator {

    public PlaceholderTag() {
        super("papi");
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void apply(@NotNull MessageOptions options, @NotNull String value) {
        options.setUsePlaceholderAPI(true);
    }
}
