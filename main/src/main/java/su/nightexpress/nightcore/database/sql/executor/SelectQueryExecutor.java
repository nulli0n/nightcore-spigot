package su.nightexpress.nightcore.database.sql.executor;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
public final class SelectQueryExecutor<T> extends SQLExecutor<List<T>> {

    private final List<SQLColumn>        columns;
    private final List<SQLCondition>     wheres;
    private final Function<ResultSet, T> dataFunction;
    private int                          amount;

    private SelectQueryExecutor(@NonNull String table, @NonNull Function<ResultSet, T> dataFunction) {
        super(table);
        this.columns = new ArrayList<>();
        this.wheres = new ArrayList<>();
        this.dataFunction = dataFunction;
        this.amount = -1;
    }

    @NonNull
    public static <T> SelectQueryExecutor<T> builder(@NonNull String table,
                                                     @NonNull Function<ResultSet, T> dataFunction) {
        return new SelectQueryExecutor<>(table, dataFunction);
    }

    /*@NonNull
    public SelectQueryExecutor<T> all() {
        return this.columns(new SQLColumn("*", ColumnType.STRING, -1));
    }*/

    @NonNull
    public SelectQueryExecutor<T> columns(@NonNull SQLColumn... columns) {
        return this.columns(Arrays.asList(columns));
    }

    @NonNull
    public SelectQueryExecutor<T> columns(@NonNull List<SQLColumn> columns) {
        //if (columns.isEmpty()) return this.all();
        this.columns.clear();
        this.columns.addAll(columns);
        return this;
    }

    @NonNull
    public SelectQueryExecutor<T> where(@NonNull SQLCondition... wheres) {
        return this.where(Arrays.asList(wheres));
    }

    @NonNull
    public SelectQueryExecutor<T> where(@NonNull List<SQLCondition> wheres) {
        this.wheres.clear();
        this.wheres.addAll(wheres);
        return this;
    }

    @NonNull
    public SelectQueryExecutor<T> amount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    @NonNull
    public List<T> execute(@NonNull AbstractConnector connector) {
        String columns = this.columns.stream().map(SQLColumn::getNameEscaped).collect(Collectors.joining(","));
        if (columns.isEmpty()) columns = "*";

        String wheres = this.wheres.stream().map(where -> where.getColumn().getNameEscaped() + " " + where.getType()
            .getOperator() + " ?")
            .collect(Collectors.joining(" AND "));

        String sql = "SELECT " + columns + " FROM " + this.getTable() + (wheres.isEmpty() ? "" : " WHERE " + wheres);

        List<String> values = this.wheres.stream().map(SQLCondition::getValue).map(SQLValue::getValue).toList();

        return SQLQueries.executeQuery(connector, sql, values, this.dataFunction, this.amount);
    }
}
