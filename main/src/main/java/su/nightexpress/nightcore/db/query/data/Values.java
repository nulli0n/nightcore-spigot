package su.nightexpress.nightcore.db.query.data;

import org.jetbrains.annotations.NotNull;
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

    Values(@NotNull Function<Column, String> columnMapper) {
        this.values = new ArrayList<>();
        this.columnMapper = columnMapper;
    }

    @NotNull
    public static <T> Values<T> forInsert() {
        return new Values<>(Column::getNameEscaped);
    }

    @NotNull
    public static <T> Values<T> forUpdate() {
        return new Values<>(column -> column.getNameEscaped() + " = ?");
    }

    @NotNull
    public String toSQL() {
        return this.values.stream().map(TypedValue::toSQL).collect(Collectors.joining(","));
    }

    public int count() {
        return this.values.size();
    }

    @NotNull
    public String getValue(@NotNull T entity, int index) {
        return this.values.get(index).extract(entity);
    }

    @NotNull
    public Values<T> setValue(@NotNull Column column, @NotNull Function<T, String> function) {
        this.values.add(new TypedValue<>(this.columnMapper.apply(column), function));
        return this;
    }
}
