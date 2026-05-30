package su.nightexpress.nightcore.database.sql;

import org.jspecify.annotations.NonNull;

@Deprecated
public class SQLCondition {

    private final SQLValue value;
    private final Type     type;

    public SQLCondition(@NonNull SQLValue value, @NonNull Type type) {
        this.value = value;
        this.type = type;
    }

    @NonNull
    public static SQLCondition of(@NonNull SQLValue value, @NonNull Type type) {
        return new SQLCondition(value, type);
    }

    @NonNull
    public static SQLCondition equal(@NonNull SQLValue value) {
        return of(value, Type.EQUAL);
    }

    @NonNull
    public static SQLCondition not(@NonNull SQLValue value) {
        return of(value, Type.NOT_EQUAL);
    }

    @NonNull
    public static SQLCondition smaller(@NonNull SQLValue value) {
        return of(value, Type.SMALLER);
    }

    @NonNull
    public static SQLCondition greater(@NonNull SQLValue value) {
        return of(value, Type.GREATER);
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @NonNull
    public SQLValue getValue() {
        return value;
    }

    @NonNull
    public SQLColumn getColumn() {
        return this.getValue().getColumn();
    }

    public enum Type {

        GREATER(">"),
        SMALLER("<"),
        EQUAL("="),
        NOT_EQUAL("!=");

        private final String operator;

        Type(@NonNull String operator) {
            this.operator = operator;
        }

        @NonNull
        public String getOperator() {
            return operator;
        }
    }
}
