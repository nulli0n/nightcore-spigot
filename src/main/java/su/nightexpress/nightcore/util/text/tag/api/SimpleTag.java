package su.nightexpress.nightcore.util.text.tag.api;

import org.jetbrains.annotations.NotNull;

public class SimpleTag extends Tag {

    public SimpleTag(@NotNull String name) {
        super(name);
    }

    public SimpleTag(@NotNull String name, @NotNull String[] aliases) {
        super(name, aliases);
    }

    @NotNull
    public String enclose(@NotNull String text) {
        return this.getBracketsName() + text + this.getClosingName();
    }
}
