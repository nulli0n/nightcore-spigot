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

    public String[] split(@NotNull String string) {
        for (String alias : this.getAliases()) {
            string = string.replace(brackets(alias), "\n");
        }
        return string.split("\n");
    }
}
