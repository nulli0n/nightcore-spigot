package su.nightexpress.nightcore.util.regex;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimedMatcher {

    private final Matcher matcher;
    private boolean debug;

    public TimedMatcher(@NotNull Matcher matcher) {
        this.matcher = matcher;
    }

    @NotNull
    public static TimedMatcher create(@NotNull String pattern, @NotNull String str) {
        return create(pattern, str, 200);
    }

    @NotNull
    public static TimedMatcher create(@NotNull Pattern pattern, @NotNull String str) {
        return new TimedMatcher(getMatcher(pattern, str, 200));
    }

    @NotNull
    public static TimedMatcher create(@NotNull String rawPattern, @NotNull String str, long timeout) {
        return create(Pattern.compile(rawPattern), str, timeout);
    }

    @NotNull
    public static TimedMatcher create(@NotNull Pattern pattern, @NotNull String str, long timeout) {
        return new TimedMatcher(getMatcher(pattern, str, timeout));
    }

    @NotNull
    private static Matcher getMatcher(@NotNull Pattern pattern, @NotNull String text, long timeout) {
        if (timeout <= 0) {
            return pattern.matcher(text);
        }
        return pattern.matcher(new TimeoutCharSequence(text, timeout));
    }

    @NotNull
    public Matcher getMatcher() {
        return matcher;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @NotNull
    public String replaceAll(@NotNull String with) {
        try {
            return this.matcher.replaceAll(with);
        }
        catch (MatcherTimeoutException exception) {
            if (this.isDebug()) {
                Plugins.CORE.warn("Matcher " + exception.getTimeout() + "ms timeout error for replaceAll: '" + matcher.pattern().pattern() + "'.");
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
                Plugins.CORE.warn("Matcher " + exception.getTimeout() + "ms timeout error for: '" + matcher.pattern().pattern() + "'.");
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
                Plugins.CORE.warn("Matcher " + exception.getTimeout() + "ms timeout error for: '" + matcher.pattern().pattern() + "'.");
            }
            return false;
        }
    }
}
