package su.nightexpress.nightcore.core;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.Engine;

@Deprecated
public class CoreLogger {

    public static void info(@NonNull String text) {
        Engine.core().info(text);
    }

    public static void warn(@NonNull String text) {
        Engine.core().warn(text);
    }

    public static void error(@NonNull String text) {
        Engine.core().error(text);
    }
}
