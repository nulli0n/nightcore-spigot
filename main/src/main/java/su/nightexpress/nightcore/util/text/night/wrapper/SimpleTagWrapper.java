package su.nightexpress.nightcore.util.text.night.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.night.ParserUtils;

public class SimpleTagWrapper implements TagWrapper {

    private final String tag;
    private final String[] arguments;

    public SimpleTagWrapper(@NotNull String tag, String[] arguments) {
        this.tag = tag;
        this.arguments = arguments;
    }

    @NotNull
    public String opening() {
        return ParserUtils.OPEN_BRACKET + this.openingNoBrackets() + ParserUtils.CLOSE_BRACKET;
    }

    @NotNull
    public String openingNoBrackets() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.tag);
        if (this.arguments.length > 0) {
            builder.append(ParserUtils.DELIMITER);
            builder.append(String.join(String.valueOf(ParserUtils.DELIMITER), this.arguments));
        }
        return builder.toString();
    }

    @NotNull
    public String closing() {
        return String.valueOf(ParserUtils.OPEN_BRACKET) + ParserUtils.CLOSE_SLASH + this.tag + ParserUtils.CLOSE_BRACKET;
    }

    @Override
    @NotNull
    public String wrap(@NotNull String string) {
        return this.opening() + string + this.closing();
    }
}
