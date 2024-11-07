package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Plugins;

public class CoreLogger {

    public static void info(@NotNull String text) {
        Plugins.getCore().info(text);
    }

    public static void warn(@NotNull String text) {
        Plugins.getCore().warn(text);
    }

    public static void error(@NotNull String text) {
        Plugins.getCore().error(text);
    }
}
