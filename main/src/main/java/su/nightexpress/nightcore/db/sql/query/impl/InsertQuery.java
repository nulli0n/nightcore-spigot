package su.nightexpress.nightcore.db.sql.query.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.type.AbstractQuery;
import su.nightexpress.nightcore.db.sql.query.QueryValue;
import su.nightexpress.nightcore.db.sql.query.type.ValuedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public class InsertQuery<T> extends AbstractQuery<T> implements ValuedQuery<InsertQuery<T>, T> {

    private final List<QueryValue<T>> columns;

    public InsertQuery() {
        this.columns = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        return this.columns.isEmpty();
    }

    @Override
    public void onExecute(@NonNull PreparedStatement statement, @NonNull T entity) throws SQLException {
        int paramCount = 1;

        for (int index = 0; index < this.countValues(); index++) {
            statement.setString(paramCount++, this.getValue(entity, index));
        }
    }

    @Override
    @NonNull
    protected String buildSQL(@NonNull String table) {
        String columns = this.columns.stream().map(QueryValue::getSQLPart).collect(Collectors.joining(","));
        String values = this.columns.stream().map(value -> "?").collect(Collectors.joining(","));

        return "INSERT INTO " + table + " (" + columns + ")" + " VALUES(" + values + ")";
    }

    @Override
    @NonNull
    public InsertQuery<T> setValue(@NonNull Column column, @NonNull Function<T, String> function) {
        this.columns.add(new QueryValue<>(column.getNameEscaped(), function));
        return this;
    }

    public int countValues() {
        return this.columns.size();
    }

    @NonNull
    public String getValue(@NonNull T entity, int index) {
        return this.columns.get(index).getStatementPart(entity);
    }
}
