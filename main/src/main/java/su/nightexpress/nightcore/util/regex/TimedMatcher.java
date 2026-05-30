package su.nightexpress.nightcore.util.regex;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.Engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimedMatcher {

    private final Matcher matcher;
    private boolean       debug;

    public TimedMatcher(@NonNull Matcher matcher) {
        this.matcher = matcher;
    }

    @NonNull
    public static TimedMatcher create(@NonNull String pattern, @NonNull String str) {
        return create(pattern, str, 200);
    }

    @NonNull
    public static TimedMatcher create(@NonNull Pattern pattern, @NonNull String str) {
        return new TimedMatcher(getMatcher(pattern, str, 200));
    }

    @NonNull
    public static TimedMatcher create(@NonNull String rawPattern, @NonNull String str, long timeout) {
        return create(Pattern.compile(rawPattern), str, timeout);
    }

    @NonNull
    public static TimedMatcher create(@NonNull Pattern pattern, @NonNull String str, long timeout) {
        return new TimedMatcher(getMatcher(pattern, str, timeout));
    }

    @NonNull
    private static Matcher getMatcher(@NonNull Pattern pattern, @NonNull String text, long timeout) {
        if (timeout <= 0) {
            return pattern.matcher(text);
        }
        return pattern.matcher(new TimeoutCharSequence(text, timeout));
    }

    @NonNull
    public Matcher getMatcher() {
        return matcher;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @NonNull
    public String replaceAll(@NonNull String with) {
        try {
            return this.matcher.replaceAll(with);
        }
        catch (MatcherTimeoutException exception) {
            if (this.isDebug()) {
                Engine.core().warn("Matcher " + exception.getTimeout() + "ms timeout error for replaceAll: '" + matcher
                    .pattern().pattern() + "'.");
            }
            return "";
        }
    }

    public boolean matches() {
        try {
            return this.matcher.matches();
        }
        catch (MatcherTimeoutException exception) {
            if (this.isDebug()) {
                Engine.core().warn("Matcher " + exception.getTimeout() + "ms timeout error for: '" + matcher.pattern()
                    .pattern() + "'.");
            }
            return false;
        }
    }

    public boolean find() {
        try {
            return this.matcher.find();
        }
        catch (MatcherTimeoutException exception) {
            if (this.isDebug()) {
                Engine.core().warn("Matcher " + exception.getTimeout() + "ms timeout error for: '" + matcher.pattern()
                    .pattern() + "'.");
            }
            return false;
        }
    }
}
