package su.nightexpress.nightcore.database.sql.executor;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLExecutor;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class DeleteQueryExecutor extends SQLExecutor<Void> {

    private final List<SQLCondition> wheres;

    private DeleteQueryExecutor(@NotNull String table) {
        super(table);
        this.wheres = new ArrayList<>();
    }

    @NotNull
    public static DeleteQueryExecutor builder(@NotNull String table) {
        return new DeleteQueryExecutor(table);
    }

    @NotNull
    public DeleteQueryExecutor where(@NotNull SQLCondition... wheres) {
        return this.where(Arrays.asList(wheres));
    }

    @NotNull
    public DeleteQueryExecutor where(@NotNull List<SQLCondition> wheres) {
        this.wheres.clear();
        this.wheres.addAll(wheres);
        return this;
    }

    @Override
    @NotNull
    public Void execute(@NotNull AbstractConnector connector) {
        //if (this.wheres.isEmpty()) return null;

        String whereCols = this.wheres.stream()
            .map(where -> where.getValue().getColumn().getNameEscaped() + " " + where.getType().getOperator() + " ?")
            .collect(Collectors.joining(" AND "));
        String sql = "DELETE FROM " + this.getTable() + (whereCols.isEmpty() ? "" : " WHERE " + whereCols);

        List<String> whereVals = this.wheres.stream().map(SQLCondition::getValue).map(SQLValue::getValue).toList();

        SQLQueries.executeStatement(connector, sql, whereVals);
        return null;
    }
}
