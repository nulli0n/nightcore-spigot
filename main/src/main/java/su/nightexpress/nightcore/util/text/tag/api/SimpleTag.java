package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

@Deprecated
public class SimpleTag extends Tag {

    public SimpleTag(@NotNull String name) {
        super(name);
    }

    public SimpleTag(@NotNull String name, @NotNull String[] aliases) {
        super(name, aliases);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text) {
        return this.wrap(text);//this.getBracketsName() + text + this.getClosingName();
    }

    @NotNull
    public String wrap(@NotNull String text) {
        return this.getBracketsName() + text + this.getClosingName();
    }
}
