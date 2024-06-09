package su.nightexpress.nightcore.util.text.tag.impl;

import su.nightexpress.nightcore.util.text.tag.api.SimpleTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class ResetTag extends SimpleTag { // TODO Must be just Tag, SimpleTag for compatibility

    public static final String NAME = "reset";

    public ResetTag() {
        super(NAME, new String[]{"r"});
    }
}
