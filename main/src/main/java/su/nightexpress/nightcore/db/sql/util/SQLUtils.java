package su.nightexpress.nightcore.db.sql.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.sql.column.Column;

@Deprecated
public class SQLUtils {

    @NotNull
    public static String escape(@NotNull String string) {
        if (string.isBlank()) return string;
        if (string.charAt(0) == '`') return string;

        return "`" + string + "`";
    }

    @NotNull
    public static String forWhere(@NotNull Column column, @NotNull WhereOperator operator) {
        return column.getNameEscaped() + " " + operator.getLiteral() + " ?";
    }

    @NotNull
    public static String forWhereLowercase(@NotNull Column column, @NotNull WhereOperator operator) {
        return column.getNameLowercase() + " " + operator.getLiteral() + " ?";
    }
}
