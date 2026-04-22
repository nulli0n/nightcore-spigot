package su.nightexpress.nightcore.util.text.night.wrapper;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.ParserUtils;

public class SimpleTagWrapper implements TagWrapper {

    private final String   tag;
    private final String[] arguments;

    public SimpleTagWrapper(@NonNull String tag, String[] arguments) {
        this.tag = tag;
        this.arguments = arguments;
    }

    @NonNull
    public String opening() {
        return ParserUtils.OPEN_BRACKET + this.openingNoBrackets() + ParserUtils.CLOSE_BRACKET;
    }

    @NonNull
    public String openingNoBrackets() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.tag);
        if (this.arguments.length > 0) {
            builder.append(ParserUtils.DELIMITER);
            builder.append(String.join(String.valueOf(ParserUtils.DELIMITER), this.arguments));
        }
        return builder.toString();
    }

    @NonNull
    public String closing() {
        return String.valueOf(
            ParserUtils.OPEN_BRACKET) + ParserUtils.CLOSE_SLASH + this.tag + ParserUtils.CLOSE_BRACKET;
    }

    @Override
    @NonNull
    public String wrap(@NonNull String string) {
        return this.opening() + string + this.closing();
    }
}
