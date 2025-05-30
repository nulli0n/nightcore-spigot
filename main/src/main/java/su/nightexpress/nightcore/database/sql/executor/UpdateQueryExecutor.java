package su.nightexpress.nightcore.database.sql.executor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLExecutor;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.query.UpdateQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public final class UpdateQueryExecutor extends SQLExecutor<Void> {

    private final List<SQLValue>     values;
    private final List<SQLCondition> wheres;

    private UpdateQueryExecutor(@NotNull String table) {
        super(table);
        this.values = new ArrayList<>();
        this.wheres = new ArrayList<>();
    }

    @NotNull
    public static UpdateQueryExecutor builder(@NotNull String table) {
        return new UpdateQueryExecutor(table);
    }

    @NotNull
    public UpdateQueryExecutor values(@NotNull SQLValue... values) {
        return this.values(Arrays.asList(values));
    }

    @NotNull
    public UpdateQueryExecutor values(@NotNull List<SQLValue> values) {
        this.values.clear();
        this.values.addAll(values);
        return this;
    }

    @NotNull
    public UpdateQueryExecutor where(@NotNull SQLCondition... wheres) {
        return this.where(Arrays.asList(wheres));
    }

    @NotNull
    public UpdateQueryExecutor where(@NotNull List<SQLCondition> wheres) {
        this.wheres.clear();
        this.wheres.addAll(wheres);
        return this;
    }

    /*@Nullable
    public UpdateQuery createObject() {
        if (this.values.isEmpty()) return null;

        String values = this.values.stream().map(value -> value.getColumn().getNameEscaped() + " = ?")
            .collect(Collectors.joining(","));

        String wheres = this.wheres.stream().map(where -> where.getColumn().getNameEscaped() + " " + where.getType().getOperator() + " ?")
            .collect(Collectors.joining(" AND "));

        String sql = "UPDATE " + this.getTable() + " SET " + values + (wheres.isEmpty() ? "" : " WHERE " + wheres);

        List<String> values2 = this.values.stream().map(SQLValue::getValue).toList();
        List<String> whers2 = this.wheres.stream().map(SQLCondition::getValue).map(SQLValue::getValue).toList();

        return new UpdateQuery(sql, values2, whers2);
    }*/

    @Override
    @NotNull
    public Void execute(@NotNull AbstractConnector connector) {
        if (this.values.isEmpty()) return null;

        String values = this.values.stream().map(value -> value.getColumn().getNameEscaped() + " = ?")
            .collect(Collectors.joining(","));

        String wheres = this.wheres.stream().map(where -> where.getColumn().getNameEscaped() + " " + where.getType().getOperator() + " ?")
            .collect(Collectors.joining(" AND "));

        String sql = "UPDATE " + this.getTable() + " SET " + values + (wheres.isEmpty() ? "" : " WHERE " + wheres);

        List<String> values2 = this.values.stream().map(SQLValue::getValue).toList();
        List<String> whers2 = this.wheres.stream().map(SQLCondition::getValue).map(SQLValue::getValue).toList();

        SQLQueries.executeStatement(connector, sql, values2, whers2);
        return null;
    }

}
