package su.nightexpress.nightcore.database.sql.query;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.sql.SQLCondition;
import su.nightexpress.nightcore.database.sql.SQLValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
public class UpdateEntity {

    private final List<String> columnNames;
    private final List<String> values;
    private final List<String> whereColumns;
    private final List<String> wheres;

    private UpdateEntity(@NonNull List<String> columnNames,
                         @NonNull List<String> values,
                         @NonNull List<String> whereColumns,
                         @NonNull List<String> wheres) {
        this.columnNames = columnNames;
        this.values = values;
        this.whereColumns = whereColumns;
        this.wheres = wheres;
    }

    @NonNull
    public static UpdateEntity create(@NonNull List<SQLValue> values) {
        return create(values, Collections.emptyList());
    }

    @NonNull
    public static UpdateEntity create(@NonNull List<SQLValue> values, @NonNull List<SQLCondition> conditions) {
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

    @NonNull
    public String createSQL(@NonNull String table) {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(table);
        builder.append(" SET ").append(String.join(",", this.columnNames));
        if (!this.whereColumns.isEmpty()) {
            builder.append(" WHERE ").append(String.join(" AND ", this.whereColumns));
        }
        return builder.toString();
    }

    @NonNull
    public List<String> getColumnNames() {
        return columnNames;
    }

    @NonNull
    public List<String> getValues() {
        return values;
    }

    @NonNull
    public List<String> getWhereColumns() {
        return whereColumns;
    }

    @NonNull
    public List<String> getWheres() {
        return wheres;
    }
}
