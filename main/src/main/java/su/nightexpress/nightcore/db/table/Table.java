package su.nightexpress.nightcore.db.table;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    private final String           name;
    private final List<Column<?>>  columns;
    private final List<ForeignKey> foreignKeys;

    public Table(@NotNull String name, @NotNull List<Column<?>> columns, @NotNull List<ForeignKey> foreignKeys) {
        this.name = name;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
    }

    @NotNull
    public static Builder builder(@NotNull String name) {
        return new Builder(name);
    }

    @NotNull
    public String toSqlCreate(@NotNull DatabaseType type) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(this.name).append(" (\n");

        List<String> definitions = new ArrayList<>();

        for (Column<?> column : this.columns) {
            definitions.add(column.toSqlWithKey(type));
        }
        for (ForeignKey foreignKey : this.foreignKeys) {
            definitions.add(foreignKey.toSql());
        }

        sql.append(String.join(",\n", definitions));
        sql.append("\n)");

        return sql.toString();
    }

    @NotNull
    public String toSqlAddColumn(@NotNull DatabaseType type, @NotNull Column<?> column) {
        return "ALTER TABLE " + this.name + " ADD " + column.toSqlWithDefault(type) + ";";
    }

    @NotNull
    public String toSqlRenameColumn(@NotNull DatabaseType type, @NotNull String sourceName, @NotNull String targetName) {
        return "ALTER TABLE " + this.name + " RENAME COLUMN " + sourceName + " TO " + targetName;
    }

    @NotNull
    public String toSqlDropColumn(@NotNull DatabaseType type, @NotNull String columnName) {
        return "ALTER TABLE " + this.name + " DROP COLUMN " + columnName;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public List<Column<?>> getColumns() {
        return this.columns;
    }

    public static class Builder {

        private final String           name;
        private final List<Column<?>>  columns;
        private final List<ForeignKey> foreignKeys;

        public Builder(@NotNull String name) {
            this.name = name;
            this.columns = new ArrayList<>();
            this.foreignKeys = new ArrayList<>();
        }

        @NotNull
        public Table build() {
            return new Table(this.name, this.columns, this.foreignKeys);
        }

        @NotNull
        public Builder withColumn(@NotNull Column<?>... columns) {
            this.columns.addAll(Arrays.asList(columns));
            return this;
        }

        @NotNull
        public Builder foreignKey(@NotNull Column<?> source, @NotNull Table targetTable, @NotNull Column<?> targetColumn) {
            this.foreignKeys.add(ForeignKey.of(source, targetTable, targetColumn));
            return this;
        }
    }
}
