package su.nightexpress.nightcore.util.text.tag.api;

import org.jspecify.annotations.NonNull;

@Deprecated
public class SimpleTag extends Tag {

    public SimpleTag(@NonNull String name) {
        super(name);
    }

    public SimpleTag(@NonNull String name, @NonNull String[] aliases) {
        super(name, aliases);
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull String text) {
        return this.wrap(text);//this.getBracketsName() + text + this.getClosingName();
    }

    @NonNull
    public String wrap(@NonNull String text) {
        return this.getBracketsName() + text + this.getClosingName();
    }
}
