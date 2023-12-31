package su.nightexpress.nightcore.util.text.decoration;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.decoration.Decorator;

public class ParsedDecorator {

    private final Decorator tag;

    private int length;

    public ParsedDecorator(@NotNull Decorator tag, int length) {
        this.tag = tag;
        this.length = length;
    }

    @NotNull
    public Decorator getDecorator() {
        return tag;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
