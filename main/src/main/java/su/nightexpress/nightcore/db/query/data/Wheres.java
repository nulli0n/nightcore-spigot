package su.nightexpress.nightcore.db.query.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.query.TypedValue;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class Wheres<T> {

    private final List<TypedValue<T>> values;

    public Wheres() {
        this.values = new ArrayList<>();
    }

    @NotNull
    public String toSQL() {
        if (this.values.isEmpty()) return "";

        return "WHERE " + this.values.stream().map(TypedValue::toSQL).collect(Collectors.joining(" AND "));
    }

    public int count() {
        return this.values.size();
    }

    @NotNull
    public String getValue(@NotNull T entity, int index) {
        return this.values.get(index).extract(entity);
    }

    @NotNull
    public Wheres<T> where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        return this.where(column, operator, o -> string);
    }

    @NotNull
    public Wheres<T> where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.values.add(new TypedValue<>(mapNormal(column, operator), function));
        return this;
    }

    @NotNull
    public Wheres<T> whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        return this.whereIgnoreCase(column, operator, o -> string);
    }

    @NotNull
    public Wheres<T> whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.values.add(new TypedValue<>(mapLowerCase(column, operator), function.andThen(String::toLowerCase)));
        return this;
    }

    @NotNull
    private static String mapNormal(@NotNull Column column, @NotNull WhereOperator operator) {
        return column.getNameEscaped() + " " + operator.getLiteral() + " ?";
    }

    @NotNull
    private static String mapLowerCase(@NotNull Column column, @NotNull WhereOperator operator) {
        return column.getNameLowercase() + " " + operator.getLiteral() + " ?";
    }
}
