package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TagUtils {

    @NotNull
    public static Color colorFromHexString(@NotNull String string) {
        return colorFromHexString(string, Color.WHITE);
    }

    @NotNull
    public static Color colorFromHexString(@NotNull String string, @NotNull Color fallback) {
        if (string.charAt(0) != '#') string = "#" + string;

        try {
            return Color.decode(string);
        }
        catch (NumberFormatException exception) {
            exception.printStackTrace();
            return fallback;
        }
    }

    @NotNull
    public static String colorToHexString(@NotNull Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }
}
