package su.nightexpress.nightcore.db.sql.query.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.query.type.ConditionalQuery;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Deprecated
public class SelectQuery<T> extends ConditionalQuery<SelectQuery<T>, ArrayList<T>> {

    private final List<String>           columns;
    private final Function<ResultSet, T> dataFunction;

    private int amount;

    public SelectQuery(@NonNull Function<ResultSet, T> dataFunction) {
        this.columns = new ArrayList<>();
        this.dataFunction = dataFunction;
        this.amount = -1;
    }

    @Override
    protected SelectQuery<T> getThis() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.columns.isEmpty();
    }

    @Override
    @NonNull
    protected String buildSQL(@NonNull String table) {
        String columns = String.join(",", this.columns);
        String wheres = this.buildWhereSQLPart();

        return "SELECT " + columns + " FROM " + table + (wheres.isEmpty() ? "" : " WHERE " + wheres);
    }

    @Override
    public void onExecute(@NonNull PreparedStatement statement, @NonNull ArrayList<T> list) throws SQLException {
        int paramCount = 1;

        for (int index = 0; index < this.countWhereColumns(); index++) {
            statement.setString(paramCount++, this.getWhereValue(index));
        }

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next() && (this.amount < 0 || list.size() < this.amount)) {
            T object = this.dataFunction.apply(resultSet);
            if (object == null) continue;

            list.add(object);
        }

        resultSet.close();
    }

    @NonNull
    public SelectQuery<T> limit(int amount) {
        this.amount = Math.abs(amount);
        return this;
    }

    @NonNull
    public SelectQuery<T> all() {
        this.columns.clear();
        this.columns.add("*");
        return this;
    }

    @NonNull
    public SelectQuery<T> column(@NonNull Column column) {
        this.columns.add(column.getNameEscaped());
        return this;
    }

    @NonNull
    public SelectQuery<T> where(@NonNull Column column, @NonNull WhereOperator operator, @NonNull String value) {
        return super.where(column, operator, obj -> value);
    }

    @NonNull
    public SelectQuery<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                          @NonNull String value) {
        return super.whereIgnoreCase(column, operator, obj -> value);
    }

    @NonNull
    public String getWhereValue(int index) {
        return this.whereColumns.get(index).getStatementPart(null);
    }
}
