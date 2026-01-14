package su.nightexpress.nightcore.db.statement.condition;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.PropertyAccessor;

import java.util.ArrayList;
import java.util.List;

public class Wheres<T> {

    private final StringBuilder                     buffer;
    private final List<PropertyAccessor<T, Object>> parameters;

    private Wheres() {
        this.buffer = new StringBuilder();
        this.parameters = new ArrayList<>();
    }

    @NotNull
    public static <T> Wheres<T> where(@NotNull Column<?> column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return where(column.getQuotedName(), operator, value);
    }

    @NotNull
    public static <T> Wheres<T> where(@NotNull String column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return new Wheres<T>().append(column, operator, value);
    }

    @NotNull
    public Wheres<T> and(@NotNull Column<?> column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return this.and(column.getQuotedName(), operator, value);
    }

    @NotNull
    public Wheres<T> and(@NotNull String column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return this.append("AND", column, operator, value);
    }

    @NotNull
    public Wheres<T> or(@NotNull Column<?> column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return this.or(column.getQuotedName(), operator, value);
    }

    @NotNull
    public Wheres<T> or(@NotNull String column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        return this.append("OR", column, operator, value);
    }

    @NotNull
    private Wheres<T> append(@NotNull String logic, @NotNull String column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
        if (this.buffer.isEmpty()) throw new IllegalStateException("'%s' must be preceded by initial where clause".formatted(logic));

        this.buffer.append(" ").append(logic).append(" ");

        return this.append(column, operator, value);
    }

    @NotNull
    private Wheres<T> append(@NotNull String column, @NotNull Operator operator, @NotNull PropertyAccessor<T, Object> value) {
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

    @NotNull
    public String toSql() {
        return this.buffer.toString();
    }

    @NotNull
    public List<PropertyAccessor<T, Object>> getParameters() {
        return this.parameters;
    }
}
