package su.nightexpress.nightcore.db.query.data;

import org.jspecify.annotations.NonNull;
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

    @NonNull
    public String toSQL() {
        if (this.values.isEmpty()) return "";

        return "WHERE " + this.values.stream().map(TypedValue::toSQL).collect(Collectors.joining(" AND "));
    }

    public int count() {
        return this.values.size();
    }

    @NonNull
    public String getValue(@NonNull T entity, int index) {
        return this.values.get(index).extract(entity);
    }

    @NonNull
    public Wheres<T> where(@NonNull Column column, @NonNull WhereOperator operator, @NonNull String string) {
        return this.where(column, operator, o -> string);
    }

    @NonNull
    public Wheres<T> where(@NonNull Column column, @NonNull WhereOperator operator,
                           @NonNull Function<T, String> function) {
        this.values.add(new TypedValue<>(mapNormal(column, operator), function));
        return this;
    }

    @NonNull
    public Wheres<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator, @NonNull String string) {
        return this.whereIgnoreCase(column, operator, o -> string);
    }

    @NonNull
    public Wheres<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                     @NonNull Function<T, String> function) {
        this.values.add(new TypedValue<>(mapLowerCase(column, operator), function.andThen(String::toLowerCase)));
        return this;
    }

    @NonNull
    private static String mapNormal(@NonNull Column column, @NonNull WhereOperator operator) {
        return column.getNameEscaped() + " " + operator.getLiteral() + " ?";
    }

    @NonNull
    private static String mapLowerCase(@NonNull Column column, @NonNull WhereOperator operator) {
        return column.getNameLowercase() + " " + operator.getLiteral() + " ?";
    }
}
