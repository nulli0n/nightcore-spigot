package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;

@Deprecated
public class CoreLogger {

    public static void info(@NotNull String text) {
        Engine.core().info(text);
    }

    public static void warn(@NotNull String text) {
        Engine.core().warn(text);
    }

    public static void error(@NotNull String text) {
        Engine.core().error(text);
    }
}
