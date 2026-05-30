package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.PlaceholderTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

@Deprecated
public class LineBreakTag extends Tag implements PlaceholderTag {

    public LineBreakTag() {
        super("newline", new String[]{"br"});
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    @NonNull
    public String getValue() {
        return "\n";
    }

    @Deprecated
    public String[] split(@NonNull String string) {
        for (String alias : this.getAliases()) {
            string = string.replace(TagUtils.brackets(alias), "\n");
        }
        return string.split("\n");
    }
}
