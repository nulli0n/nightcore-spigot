package su.nightexpress.nightcore.database.sql.executor;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLExecutor;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class InsertQueryExecutor extends SQLExecutor<Void> {

    private final List<SQLValue> values;

    private InsertQueryExecutor(@NotNull String table) {
        super(table);
        this.values = new ArrayList<>();
    }

    @NotNull
    public static InsertQueryExecutor builder(@NotNull String table) {
        return new InsertQueryExecutor(table);
    }

    @NotNull
    public InsertQueryExecutor values(@NotNull SQLValue... values) {
        return this.values(Arrays.asList(values));
    }

    @NotNull
    public InsertQueryExecutor values(@NotNull List<SQLValue> values) {
        this.values.clear();
        this.values.addAll(values);
        return this;
    }

    @Override
    @NotNull
    public Void execute(@NotNull AbstractConnector connector) {
        if (this.values.isEmpty()) return null;

        String columns = this.values.stream().map(SQLValue::getColumn).map(SQLColumn::getNameEscaped).collect(Collectors.joining(","));
        String values = this.values.stream().map(value -> "?").collect(Collectors.joining(","));
        String sql = "INSERT INTO " + this.getTable() + "(" + columns + ") VALUES(" + values + ")";
        List<String> values2 = this.values.stream().map(SQLValue::getValue).toList();

        SQLQueries.executeStatement(connector, sql, values2);
        return null;
    }
}
