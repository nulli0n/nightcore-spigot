package su.nightexpress.nightcore.db.sql.query.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.type.ConditionalQuery;
import su.nightexpress.nightcore.db.sql.query.QueryValue;
import su.nightexpress.nightcore.db.sql.query.type.ValuedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class UpdateQuery<T> extends ConditionalQuery<UpdateQuery<T>, T> implements ValuedQuery<UpdateQuery<T>, T> {

    private final List<QueryValue<T>> dataColumns;

    public UpdateQuery() {
        this.dataColumns = new ArrayList<>();
    }

    @Override
    protected UpdateQuery<T> getThis() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.dataColumns.isEmpty();
    }

    @Override
    public void onExecute(@NonNull PreparedStatement statement, @NonNull T entity) throws SQLException {
        int paramCount = 1;

        for (int index = 0; index < this.countDataColumns(); index++) {
            statement.setString(paramCount++, this.getColumnValue(entity, index));
        }

        for (int index = 0; index < this.countWhereColumns(); index++) {
            statement.setString(paramCount++, this.getWhereValue(entity, index));
        }
    }

    @Override
    @NonNull
    protected String buildSQL(@NonNull String table) {
        String columns = this.dataColumns.stream().map(QueryValue::getSQLPart).collect(Collectors.joining(","));
        String wheres = this.buildWhereSQLPart();

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(table);
        builder.append(" SET ").append(columns);
        if (!wheres.isBlank()) {
            builder.append(" WHERE ").append(wheres);
        }
        return builder.toString();
    }

    @Override
    @NonNull
    public UpdateQuery<T> setValue(@NonNull Column column, @NonNull Function<T, String> function) {
        this.dataColumns.add(new QueryValue<>(column.getNameEscaped() + " = ?", function));
        return this;
    }

    public int countDataColumns() {
        return this.dataColumns.size();
    }

    @NonNull
    public String getColumnValue(@NonNull T entity, int index) {
        return this.dataColumns.get(index).getStatementPart(entity);
    }
}
