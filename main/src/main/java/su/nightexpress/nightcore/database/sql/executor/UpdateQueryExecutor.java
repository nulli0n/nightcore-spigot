package su.nightexpress.nightcore.database.sql.executor;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    private UpdateQueryExecutor(@NonNull String table) {
        super(table);
        this.values = new ArrayList<>();
        this.wheres = new ArrayList<>();
    }

    @NonNull
    public static UpdateQueryExecutor builder(@NonNull String table) {
        return new UpdateQueryExecutor(table);
    }

    @NonNull
    public UpdateQueryExecutor values(@NonNull SQLValue... values) {
        return this.values(Arrays.asList(values));
    }

    @NonNull
    public UpdateQueryExecutor values(@NonNull List<SQLValue> values) {
        this.values.clear();
        this.values.addAll(values);
        return this;
    }

    @NonNull
    public UpdateQueryExecutor where(@NonNull SQLCondition... wheres) {
        return this.where(Arrays.asList(wheres));
    }

    @NonNull
    public UpdateQueryExecutor where(@NonNull List<SQLCondition> wheres) {
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
    @NonNull
    public Void execute(@NonNull AbstractConnector connector) {
        if (this.values.isEmpty()) return null;

        String values = this.values.stream().map(value -> value.getColumn().getNameEscaped() + " = ?")
            .collect(Collectors.joining(","));

        String wheres = this.wheres.stream().map(where -> where.getColumn().getNameEscaped() + " " + where.getType()
            .getOperator() + " ?")
            .collect(Collectors.joining(" AND "));

        String sql = "UPDATE " + this.getTable() + " SET " + values + (wheres.isEmpty() ? "" : " WHERE " + wheres);

        List<String> values2 = this.values.stream().map(SQLValue::getValue).toList();
        List<String> whers2 = this.wheres.stream().map(SQLCondition::getValue).map(SQLValue::getValue).toList();

        SQLQueries.executeStatement(connector, sql, values2, whers2);
        return null;
    }

}
