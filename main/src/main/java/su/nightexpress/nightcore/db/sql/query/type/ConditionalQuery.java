package su.nightexpress.nightcore.db.sql.query.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.QueryValue;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public abstract class ConditionalQuery<Q extends ConditionalQuery<Q, T>, T> extends AbstractQuery<T> {

    protected final List<QueryValue<T>> whereColumns;

    public ConditionalQuery() {
        this.whereColumns = new ArrayList<>();
    }

    protected abstract Q getThis();

    @NotNull
    protected String buildWhereSQLPart() {
        return this.whereColumns.stream().map(QueryValue::getSQLPart).collect(Collectors.joining(" AND "));
    }

    @NotNull
    public Q where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.whereColumns.add(new QueryValue<>(SQLUtils.forWhere(column, operator), function));
        return this.getThis();
    }

    @NotNull
    public Q whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.whereColumns.add(new QueryValue<>(SQLUtils.forWhereLowercase(column, operator), function.andThen(String::toLowerCase)));
        return this.getThis();
    }

    public int countWhereColumns() {
        return this.whereColumns.size();
    }

    @NotNull
    public String getWhereValue(@NotNull T entity, int index) {
        return this.whereColumns.get(index).getStatementPart(entity);
    }
}
