package su.nightexpress.nightcore.db.table;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.column.Column;

public record ForeignKey(@NonNull String columnName, @NonNull String refTable, @NonNull String refColumn) {

    @NonNull
    public static ForeignKey of(@NonNull Column<?> column, @NonNull Table table, @NonNull Column<?> ref) {
        return new ForeignKey(column.getQuotedName(), table.getName(), ref.getQuotedName());
    }

    @NonNull
    public String toSql() {
        return "FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE".formatted(this.columnName, this.refTable,
            this.refColumn);
    }
}
