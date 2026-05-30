package su.nightexpress.nightcore.util.regex;

import org.jspecify.annotations.NonNull;

public class MatcherTimeoutException extends RuntimeException {

    private final String chars;
    private final long   timeout;

    MatcherTimeoutException(@NonNull CharSequence chars, long timeout) {
        this.chars = chars.toString();
        this.timeout = timeout;
    }

    @NonNull
    public String getString() {
        return this.chars;
    }

    public long getTimeout() {
        return this.timeout;
    }
}
