package su.nightexpress.nightcore.database.sql;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

@Deprecated
public class SQLUtils {

    @NonNull
    public static String escape(@NonNull String string) {
        if (string.isBlank()) return string;
        if (string.charAt(0) == '`') return string;

        return "`" + string + "`";
    }

    @NonNull
    public static String forWhere(@NonNull SQLColumn column, @NonNull WhereOperator operator) {
        return column.getNameEscaped() + " " + operator.getLiteral() + " ?";
    }

    @NonNull
    public static String forWhereLowercase(@NonNull SQLColumn column, @NonNull WhereOperator operator) {
        return column.getNameLowercase() + " " + operator.getLiteral() + " ?";
    }
}
