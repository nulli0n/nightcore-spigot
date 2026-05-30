package su.nightexpress.nightcore.database.sql;

import org.jspecify.annotations.NonNull;

@Deprecated
public class SQLValue {

    private final SQLColumn column;
    private final String    value;

    public SQLValue(@NonNull SQLColumn column, @NonNull String value) {
        this.column = column;
        this.value = value;
    }

    @NonNull
    public static SQLValue of(@NonNull SQLColumn column, @NonNull String value) {
        return new SQLValue(column, value);
    }

    @NonNull
    public SQLColumn getColumn() {
        return column;
    }

    @NonNull
    public String getValue() {
        return value;
    }
}
