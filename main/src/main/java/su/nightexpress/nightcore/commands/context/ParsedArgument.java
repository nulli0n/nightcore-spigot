package su.nightexpress.nightcore.commands.context;

import org.jspecify.annotations.NonNull;

public class ParsedArgument<T> {

    private final T   result;
    private final int cursor;

    public ParsedArgument(@NonNull T result, int cursor) {
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
