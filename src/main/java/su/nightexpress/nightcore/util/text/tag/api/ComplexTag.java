package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

public class ComplexTag extends Tag {

    public ComplexTag(@NotNull String name) {
        super(name);
    }

    public ComplexTag(@NotNull String name, @NotNull String[] aliases) {
        super(name, aliases);
    }

    @NotNull
    protected String escapeQuotes(@NotNull String content) {
        return content.replace("'", "\\'").replace("\"", "\\\"");
    }

    @NotNull
    public String encloseContent(@NotNull String text, @NotNull String content) {
        String tagOpen = brackets(this.getName() + ":" + content);
        String tagClose = this.getClosingName();

        return tagOpen + text + tagClose;
    }
}
