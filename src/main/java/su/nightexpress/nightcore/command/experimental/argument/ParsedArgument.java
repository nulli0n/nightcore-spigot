package su.nightexpress.nightcore.command.experimental.argument;

public class ParsedArgument<T> {

    private final T result;

    public ParsedArgument(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
