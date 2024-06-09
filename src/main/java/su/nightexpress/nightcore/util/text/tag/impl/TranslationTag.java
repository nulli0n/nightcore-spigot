package su.nightexpress.nightcore.util.text.tag.impl;

import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class TranslationTag extends Tag {

    public static final String NAME = "translation";

    public TranslationTag() {
        super(NAME, new String[]{"tr", "lang"});
    }
}
