package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

@Deprecated
public class TranslationTag extends Tag {

    public static final String NAME = "lang";

    public TranslationTag() {
        super(NAME, new String[]{"lang_or", "tr", "tr_or", "translation", "translation_or", "translate", "translate_or"});
    }

    @NotNull
    public String wrap(@NotNull String translationKey) {
        return this.wrap(translationKey, null);
    }

    @NotNull
    public String wrap(@NotNull String translationKey, @Nullable String fallback) {
        String full = this.getName() + ParserUtils.DELIMITER + ParserUtils.quoted(translationKey);
        if (fallback != null) {
            full += ParserUtils.DELIMITER + ParserUtils.quoted(fallback);
        }

        return TagUtils.brackets(full);
    }
}
