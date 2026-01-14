package su.nightexpress.nightcore.db.table;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.column.Column;

public record ForeignKey(@NotNull String columnName, @NotNull String refTable, @NotNull String refColumn) {

    @NotNull
    public static ForeignKey of(@NotNull Column<?> column, @NotNull Table table, @NotNull Column<?> ref) {
        return new ForeignKey(column.getQuotedName(), table.getName(), ref.getQuotedName());
    }

    @NotNull
    public String toSql() {
        return "FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE".formatted(this.columnName, this.refTable, this.refColumn);
    }
}
