package su.nightexpress.nightcore.util.regex;

import org.jetbrains.annotations.NotNull;

public class MatcherTimeoutException extends RuntimeException {

    private final String chars;
    private final long   timeout;

    MatcherTimeoutException(@NotNull CharSequence chars, long timeout) {
        this.chars = chars.toString();
        this.timeout = timeout;
    }

    @NotNull
    public String getString() {
        return this.chars;
    }

    public long getTimeout() {
        return this.timeout;
    }
}
