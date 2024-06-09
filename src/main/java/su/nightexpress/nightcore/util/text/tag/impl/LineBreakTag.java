package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.api.PlaceholderTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class LineBreakTag extends Tag implements PlaceholderTag {

    public LineBreakTag() {
        super("newline", new String[]{"br"});
    }

    @Override
    @NotNull
    public String getValue() {
        return "\n";
    }
}
