package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.api.OrphanTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class LineBreakTag extends Tag implements OrphanTag {

    public LineBreakTag() {
        super("br");
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    @NotNull
    public String getContent() {
        return "\n";
    }
}
