package su.nightexpress.nightcore.util.text.tag.impl;

import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class ResetTag extends Tag {

    public static final String NAME = "reset";

    public ResetTag() {
        super(NAME, new String[]{"r"});
    }

    @Override
    public boolean isCloseable() {
        return false;
    }
}
