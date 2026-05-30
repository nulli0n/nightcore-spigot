package su.nightexpress.nightcore.db.statement.condition;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.PropertyAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wheres<T> {

    private final StringBuilder                     buffer;
    private final List<PropertyAccessor<T, Object>> parameters;

    private Wheres() {
        this.buffer = new StringBuilder();
        this.parameters = new ArrayList<>();
    }

    @NonNull
    public static <T> Wheres<T> where(@NonNull Column<?> column, @NonNull Operator operator,
                                      @NonNull PropertyAccessor<T, Object> value) {
        return where(column.getQuotedName(), operator, value);
    }

    @NonNull
    public static <T> Wheres<T> where(@NonNull String column, @NonNull Operator operator,
                                      @NonNull PropertyAccessor<T, Object> value) {
        return new Wheres<T>().append(column, operator, value);
    }

    @NonNull
    public static <T> Wheres<T> whereUUID(@NonNull Column<?> column, @NonNull PropertyAccessor<T, UUID> value) {
        return whereUUID(column.getQuotedName(), value);
    }

    @NonNull
    public static <T> Wheres<T> whereUUID(@NonNull String column, @NonNull PropertyAccessor<T, UUID> value) {
        return where(column, Operator.EQUALS, o -> value.access(o).toString());
    }

    @NonNull
    public Wheres<T> and(@NonNull Column<?> column, @NonNull Operator operator,
                         @NonNull PropertyAccessor<T, Object> value) {
        return this.and(column.getQuotedName(), operator, value);
    }

    @NonNull
    public Wheres<T> and(@NonNull String column, @NonNull Operator operator,
                         @NonNull PropertyAccessor<T, Object> value) {
        return this.append("AND", column, operator, value);
    }

    @NonNull
    public Wheres<T> or(@NonNull Column<?> column, @NonNull Operator operator,
                        @NonNull PropertyAccessor<T, Object> value) {
        return this.or(column.getQuotedName(), operator, value);
    }

    @NonNull
    public Wheres<T> or(@NonNull String column, @NonNull Operator operator,
                        @NonNull PropertyAccessor<T, Object> value) {
        return this.append("OR", column, operator, value);
    }

    @NonNull
    private Wheres<T> append(@NonNull String logic, @NonNull String column, @NonNull Operator operator,
                             @NonNull PropertyAccessor<T, Object> value) {
        if (this.buffer.isEmpty()) throw new IllegalStateException("'%s' must be preceded by initial where clause"
            .formatted(logic));

        this.buffer.append(" ").append(logic).append(" ");

        return this.append(column, operator, value);
    }

    @NonNull
    private Wheres<T> append(@NonNull String column, @NonNull Operator operator,
                             @NonNull PropertyAccessor<T, Object> value) {
        if (operator == Operator.EQUALS_IGNORE_CASE) {
            this.buffer.append("LOWER(").append(column).append(") = LOWER(?)");
        }
        else {
            this.buffer.append(column).append(" ").append(operator.getLiteral()).append(" ?");
        }
        this.parameters.add(value);
        return this;
    }

    public boolean isEmpty() {
        return this.buffer.isEmpty();
    }

    public int size() {
        return this.parameters.size();
    }

    @NonNull
    public String toSql() {
        return this.buffer.toString();
    }

    @NonNull
    public List<PropertyAccessor<T, Object>> getParameters() {
        return this.parameters;
    }
}
