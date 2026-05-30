package su.nightexpress.nightcore.db.query.data;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.query.TypedValue;
import su.nightexpress.nightcore.db.sql.column.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class Values<T> {

    private final List<TypedValue<T>>      values;
    private final Function<Column, String> columnMapper;

    Values(@NonNull Function<Column, String> columnMapper) {
        this.values = new ArrayList<>();
        this.columnMapper = columnMapper;
    }

    @NonNull
    public static <T> Values<T> forInsert() {
        return new Values<>(Column::getNameEscaped);
    }

    @NonNull
    public static <T> Values<T> forUpdate() {
        return new Values<>(column -> column.getNameEscaped() + " = ?");
    }

    @NonNull
    public String toSQL() {
        return this.values.stream().map(TypedValue::toSQL).collect(Collectors.joining(","));
    }

    public int count() {
        return this.values.size();
    }

    @NonNull
    public String getValue(@NonNull T entity, int index) {
        return this.values.get(index).extract(entity);
    }

    @NonNull
    public Values<T> setValue(@NonNull Column column, @NonNull Function<T, String> function) {
        this.values.add(new TypedValue<>(this.columnMapper.apply(column), function));
        return this;
    }
}
