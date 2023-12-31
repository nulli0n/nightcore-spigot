package su.nightexpress.nightcore.database.sql;

import org.jetbrains.annotations.NotNull;

public class SQLValue {

    private final SQLColumn column;
    private final String    value;

    public SQLValue(@NotNull SQLColumn column, @NotNull String value) {
        this.column = column;
        this.value = value;
    }

    @NotNull
    public static SQLValue of(@NotNull SQLColumn column, @NotNull String value) {
        return new SQLValue(column, value);
    }

    @NotNull
    public SQLColumn getColumn() {
        return column;
    }

    @NotNull
    public String getValue() {
        return value;
    }
}
