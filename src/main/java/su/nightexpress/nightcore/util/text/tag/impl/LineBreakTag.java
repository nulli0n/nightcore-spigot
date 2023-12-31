package su.nightexpress.nightcore.util.text.tag.impl;

import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class LineBreakTag extends Tag {

    public LineBreakTag() {
        super("br");
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
