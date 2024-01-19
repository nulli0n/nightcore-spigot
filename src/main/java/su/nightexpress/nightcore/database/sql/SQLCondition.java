package su.nightexpress.nightcore.database.sql;

import org.jetbrains.annotations.NotNull;

public class SQLCondition {

    private final SQLValue value;
    private final Type      type;

    public SQLCondition(@NotNull SQLValue value, @NotNull Type type) {
        this.value = value;
        this.type = type;
    }

    @NotNull
    public static SQLCondition of(@NotNull SQLValue value, @NotNull Type type) {
        return new SQLCondition(value, type);
    }

    @NotNull
    public static SQLCondition equal(@NotNull SQLValue value) {
        return of(value, Type.EQUAL);
    }

    @NotNull
    public static SQLCondition not(@NotNull SQLValue value) {
        return of(value, Type.NOT_EQUAL);
    }

    @NotNull
    public static SQLCondition smaller(@NotNull SQLValue value) {
        return of(value, Type.SMALLER);
    }

    @NotNull
    public static SQLCondition greater(@NotNull SQLValue value) {
        return of(value, Type.GREATER);
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @NotNull
    public SQLValue getValue() {
        return value;
    }

    @NotNull
    public SQLColumn getColumn() {
        return this.getValue().getColumn();
    }

    public enum Type {
        GREATER(">"),
        SMALLER("<"),
        EQUAL("="),
        NOT_EQUAL("!=");

        private final String operator;

        Type(@NotNull String operator) {
            this.operator = operator;
        }

        @NotNull
        public String getOperator() {
            return operator;
        }
    }
}
