package su.nightexpress.nightcore.util.text.tag.api;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;

@Deprecated
public class ComplexTag extends Tag {

    public ComplexTag(@NonNull String name) {
        super(name);
    }

    public ComplexTag(@NonNull String name, @NonNull String[] aliases) {
        super(name, aliases);
    }

    @NonNull
    protected String escapeQuotes(@NonNull String content) {
        return ParserUtils.escapeQuotes(content);
        //return content.replace("'", "\\'").replace("\"", "\\\"");
    }

    @NonNull
    public String encloseContent(@NonNull String text, @NonNull String content) {
        return TagUtils.wrapContent(this, text, content);
        //        String tagOpen = brackets(this.getName() + ":" + content);
        //        String tagClose = this.getClosingName();
        //
        //        return tagOpen + text + tagClose;
    }
}
