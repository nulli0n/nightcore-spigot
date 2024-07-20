package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdateEntity {

    private final List<String> columnNames;
    private final List<String> values;
    private final List<String> whereColumns;
    private final List<String> wheres;

    private UpdateEntity(@NotNull List<String> columnNames,
                         @NotNull List<String> values,
                         @NotNull List<String> whereColumns,
                         @NotNull List<String> wheres) {
        this.columnNames = columnNames;
        this.values = values;
        this.whereColumns = whereColumns;
        this.wheres = wheres;
    }

    @NotNull
    public static UpdateEntity create(@NotNull List<SQLValue> values) {
        return create(values, Collections.emptyList());
    }

    @NotNull
    public static UpdateEntity create(@NotNull List<SQLValue> values, @NotNull List<SQLCondition> conditions) {
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

        return new UpdateEntity(columnNames, columnValues, whereColumns, whereValues);
    }

    @NotNull
    public String createSQL(@NotNull String table) {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(table);
        builder.append(" SET ").append(String.join(",", this.columnNames));
        if (!this.whereColumns.isEmpty()) {
            builder.append(" WHERE ").append(String.join(" AND ", this.whereColumns));
        }
        return builder.toString();
    }

    @NotNull
    public List<String> getColumnNames() {
        return columnNames;
    }

    @NotNull
    public List<String> getValues() {
        return values;
    }

    @NotNull
    public List<String> getWhereColumns() {
        return whereColumns;
    }

    @NotNull
    public List<String> getWheres() {
        return wheres;
    }
}
