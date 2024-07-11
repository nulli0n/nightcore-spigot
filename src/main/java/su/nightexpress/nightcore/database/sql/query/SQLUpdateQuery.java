package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLUtils;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLUpdateQuery extends AbstractSQLQuery implements IUpdateQuery {

    private final List<String> values;
    private final List<String> wheres;

    private SQLUpdateQuery(@NotNull String sql, @NotNull List<String> values, @NotNull List<String> wheres) {
        super(sql);
        this.values = values;
        this.wheres = wheres;
    }

    @NotNull
    public static SQLUpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values) {
        return create(table, values, Collections.emptyList());
    }

    @NotNull
    public static SQLUpdateQuery create(@NotNull String table, @NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        List<String> whereColumns = new ArrayList<>();
        List<String> whereValues = new ArrayList<>();

        values.forEach(value -> {
            columnNames.add(value.getColumn().getNameEscaped() + " = ?");
            columnValues.add(value.getValue());
        });

        conditions.forEach(condition -> {
            whereColumns.add(condition.getColumn().getNameEscaped() + " " + condition.getType().getOperator() + " ?");
            whereValues.add(condition.getValue().getValue());
        });

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(SQLUtils.escape(table));
        builder.append(" SET ").append(String.join(",", columnNames));
        if (!whereColumns.isEmpty()) {
            builder.append(" WHERE ").append(String.join(" AND ", whereColumns));
        }

        String sql = builder.toString();

        return new SQLUpdateQuery(sql, columnValues, whereValues);
    }

    @NotNull
    public List<String> getValues() {
        return values;
    }

    @NotNull
    public List<String> getWheres() {
        return wheres;
    }
}
