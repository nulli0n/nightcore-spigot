package su.nightexpress.nightcore.commands.argument;

import org.jetbrains.annotations.NotNull;

public class ArgumentReader {

    private static final String ARGUMENT_SEPARATOR = " ";

    private final String[] args;
    private final int length;
    private final String string;

    private int cursor;

    public ArgumentReader(@NotNull String[] args) {
        this.args = args;
        this.length = args.length;
        this.cursor = 0;
        this.string = String.join(ARGUMENT_SEPARATOR, this.args);
    }

    @NotNull
    public static ArgumentReader forArgumentsWithLabel(@NotNull String label, @NotNull String[] args) {
        return new ArgumentReader(argumentsWithLabel(label, args));
    }

    private static String[] argumentsWithLabel(@NotNull String label, String[] args) {
        String[] withLabel = new String[args.length + 1];
        withLabel[0] = label;
        System.arraycopy(args, 0, withLabel, 1, args.length);
        return withLabel;
    }

    @NotNull
    public String getString() {
        return this.string;
    }

    public boolean canMoveForward() {
        return this.cursor < this.length;
    }

    public boolean isEnd() {
        return this.cursor == this.length - 1;
    }

    public void moveForward() {
        this.cursor++;
    }

    public void resetCursor() {
        this.setCursor(0);
    }

    @NotNull
    public String getArgument(int cursor) {
        return this.args[cursor];
    }

    @NotNull
    public String getCursorArgument() {
        return this.getArgument(this.cursor);
    }

    public String[] getArgs() {
        return this.args;
    }

    public int getLength() {
        return this.length;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return this.cursor;
    }
}
