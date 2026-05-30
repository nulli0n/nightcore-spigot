package su.nightexpress.nightcore.util.regex;

import org.jspecify.annotations.NonNull;

public class TimeoutCharSequence implements CharSequence {

    private final CharSequence chars;
    private final long         timeout;
    private final long         maxTime;

    public TimeoutCharSequence(@NonNull CharSequence chars, long timeout) {
        this.chars = chars;
        this.timeout = timeout;
        this.maxTime = (System.currentTimeMillis() + timeout);
    }

    @Override
    public char charAt(int index) {
        if (System.currentTimeMillis() > this.maxTime) {
            throw new MatcherTimeoutException(this.chars, this.timeout);
        }
        return this.chars.charAt(index);
    }

    @Override
    public int length() {
        return this.chars.length();
    }

    @Override
    @NonNull
    public CharSequence subSequence(int start, int end) {
        return new TimeoutCharSequence(this.chars.subSequence(start, end), this.timeout);
    }

    @Override
    @NonNull
    public String toString() {
        return this.chars.toString();
    }
}
