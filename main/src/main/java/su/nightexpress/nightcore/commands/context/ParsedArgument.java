package su.nightexpress.nightcore.commands.context;

import org.jetbrains.annotations.NotNull;

public class ParsedArgument<T> {

    private final T   result;
    private final int cursor;

    public ParsedArgument(@NotNull T result, int cursor) {
        this.result = result;
        this.cursor = cursor;
    }

    public T getResult() {
        return result;
    }

    public int getCursor() {
        return this.cursor;
    }
}
