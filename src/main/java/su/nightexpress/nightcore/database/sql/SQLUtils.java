package su.nightexpress.nightcore.database.sql;

import org.jetbrains.annotations.NotNull;

public class SQLUtils {

    @NotNull
    public static String escape(@NotNull String string) {
        if (string.isBlank()) return string;
        if (string.charAt(0) == '`') return string;

        return "`" + string + "`";
    }
}
