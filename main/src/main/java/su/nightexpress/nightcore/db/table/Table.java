package su.nightexpress.nightcore.db.table;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    private final String           name;
    private final List<Column<?>>  columns;
    private final List<ForeignKey> foreignKeys;

    public Table(@NonNull String name, @NonNull List<Column<?>> columns, @NonNull List<ForeignKey> foreignKeys) {
        this.name = name;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
    }

    @NonNull
    public static Builder builder(@NonNull String name) {
        return new Builder(name);
    }

    @NonNull
    public String toSqlCreate(@NonNull DatabaseType type) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(this.name).append(" (\n");

        List<String> definitions = new ArrayList<>();
        List<String> pkColumns = new ArrayList<>();
        boolean hasAutoIncrementPk = false;
        boolean hasInlineSQLitePk = false;

        for (Column<?> column : this.columns) {
            definitions.add(column.toSqlWithKey(type));

            if (column.isPrimaryKey()) {
                pkColumns.add(column.getQuotedName());

                if (column.isAutoIncrement()) {
                    hasAutoIncrementPk = true;
                    if (type == DatabaseType.SQLITE) {
                        hasInlineSQLitePk = true;
                    }
                }
            }
        }

        // Prevent auto-increment on composite primary keys
        if (pkColumns.size() > 1 && hasAutoIncrementPk) {
            throw new IllegalStateException("Table '" + this.name + "' cannot have an auto-increment column as part of a composite primary key.");
        }

        for (ForeignKey foreignKey : this.foreignKeys) {
            definitions.add(foreignKey.toSql());
        }

        // Append the table-level PRIMARY KEY if we have keys and didn't use the SQLite inline workaround
        if (!pkColumns.isEmpty() && !hasInlineSQLitePk) {
            String pkDefinition = "PRIMARY KEY (" + String.join(", ", pkColumns) + ")";
            definitions.add(pkDefinition);
        }

        sql.append(String.join(",\n", definitions));
        sql.append("\n)");

        return sql.toString();
    }

    @NonNull
    public String toSqlAddColumn(@NonNull DatabaseType type, @NonNull Column<?> column) {
        return "ALTER TABLE " + this.name + " ADD " + column.toSqlWithDefault(type) + ";";
    }

    @NonNull
    public String toSqlRenameColumn(@NonNull DatabaseType type, @NonNull String sourceName, @NonNull String targetName) {
        return "ALTER TABLE " + this.name + " RENAME COLUMN " + sourceName + " TO " + targetName;
    }

    @NonNull
    public String toSqlDropColumn(@NonNull DatabaseType type, @NonNull String columnName) {
        return "ALTER TABLE " + this.name + " DROP COLUMN " + columnName;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public List<Column<?>> getColumns() {
        return this.columns;
    }

    public static class Builder {

        private final String           name;
        private final List<Column<?>>  columns;
        private final List<ForeignKey> foreignKeys;

        public Builder(@NonNull String name) {
            this.name = name;
            this.columns = new ArrayList<>();
            this.foreignKeys = new ArrayList<>();
        }

        @NonNull
        public Table build() {
            return new Table(this.name, this.columns, this.foreignKeys);
        }

        @NonNull
        public Builder withColumn(@NonNull Column<?>... columns) {
            this.columns.addAll(Arrays.asList(columns));
            return this;
        }

        @NonNull
        public Builder foreignKey(@NonNull Column<?> source, @NonNull Table targetTable, @NonNull Column<?> targetColumn) {
            this.foreignKeys.add(ForeignKey.of(source, targetTable, targetColumn));
            return this;
        }
    }
}
