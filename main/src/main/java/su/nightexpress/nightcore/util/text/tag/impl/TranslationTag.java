package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

@Deprecated
public class TranslationTag extends Tag {

    public static final String NAME = "lang";

    public TranslationTag() {
        super(NAME, new String[]{"lang_or", "tr", "tr_or", "translation", "translation_or", "translate", "translate_or"});
    }

    @NonNull
    public String wrap(@NonNull String translationKey) {
        return this.wrap(translationKey, null);
    }

    @NonNull
    public String wrap(@NonNull String translationKey, @Nullable String fallback) {
        String full = this.getName() + ParserUtils.DELIMITER + ParserUtils.quoted(translationKey);
        if (fallback != null) {
            full += ParserUtils.DELIMITER + ParserUtils.quoted(fallback);
        }

        return TagUtils.brackets(full);
    }
}
