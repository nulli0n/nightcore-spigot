package su.nightexpress.nightcore.db.sql.query.type;

import org.jspecify.annotations.NonNull;
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

    @NonNull
    protected String buildWhereSQLPart() {
        return this.whereColumns.stream().map(QueryValue::getSQLPart).collect(Collectors.joining(" AND "));
    }

    @NonNull
    public Q where(@NonNull Column column, @NonNull WhereOperator operator, @NonNull Function<T, String> function) {
        this.whereColumns.add(new QueryValue<>(SQLUtils.forWhere(column, operator), function));
        return this.getThis();
    }

    @NonNull
    public Q whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                             @NonNull Function<T, String> function) {
        this.whereColumns.add(new QueryValue<>(SQLUtils.forWhereLowercase(column, operator), function.andThen(
            String::toLowerCase)));
        return this.getThis();
    }

    public int countWhereColumns() {
        return this.whereColumns.size();
    }

    @NonNull
    public String getWhereValue(@NonNull T entity, int index) {
        return this.whereColumns.get(index).getStatementPart(entity);
    }
}
