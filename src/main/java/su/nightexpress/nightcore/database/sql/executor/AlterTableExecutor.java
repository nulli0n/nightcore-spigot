package su.nightexpress.nightcore.database.sql.executor;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.DatabaseType;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.connection.SQLiteConnector;
import su.nightexpress.nightcore.database.sql.SQLColumn;
import su.nightexpress.nightcore.database.sql.SQLExecutor;
import su.nightexpress.nightcore.database.sql.SQLQueries;
import su.nightexpress.nightcore.database.sql.SQLValue;
import su.nightexpress.nightcore.database.sql.column.ColumnType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AlterTableExecutor extends SQLExecutor<Void> {

    private final DatabaseType   databaseType;
    private final List<SQLValue> columns;
    private Type type;

    private AlterTableExecutor(@NotNull String table, @NotNull DatabaseType databaseType) {
        super(table);
        this.databaseType = databaseType;
        this.columns = new ArrayList<>();
    }

    private enum Type {
        ADD_COLUMN, RENAME_COLUMN, DROP_COLUMN
    }

    @NotNull
    public static AlterTableExecutor builder(@NotNull String table, @NotNull DatabaseType databaseType) {
        return new AlterTableExecutor(table, databaseType);
    }

    @NotNull
    public AlterTableExecutor addColumn(@NotNull SQLValue... columns) {
        return this.addColumn(Arrays.asList(columns));
    }

    @NotNull
    public AlterTableExecutor addColumn(@NotNull List<SQLValue> columns) {
        return this.columns(columns, Type.ADD_COLUMN);
    }

    @NotNull
    public AlterTableExecutor renameColumn(@NotNull SQLValue... columns) {
        return this.addColumn(Arrays.asList(columns));
    }

    @NotNull
    public AlterTableExecutor renameColumn(@NotNull List<SQLValue> columns) {
        return this.columns(columns, Type.RENAME_COLUMN);
    }

    @NotNull
    public AlterTableExecutor dropColumn(@NotNull SQLColumn... columns) {
        return this.dropColumn(Arrays.asList(columns));
    }

    @NotNull
    public AlterTableExecutor dropColumn(@NotNull List<SQLColumn> columns) {
        return this.columns(columns.stream().map(column -> column.toValue("dummy")).toList(), Type.DROP_COLUMN);
    }

    private AlterTableExecutor columns(@NotNull List<SQLValue> values, @NotNull Type type) {
        this.columns.clear();
        this.columns.addAll(values);
        this.type = type;
        return this;
    }

    @Override
    @NotNull
    public Void execute(@NotNull AbstractConnector connector) {
        if (this.columns.isEmpty()) return null;

        if (this.type == Type.ADD_COLUMN) {
            this.columns.forEach(value -> {
                if (SQLQueries.hasColumn(connector, this.getTable(), value.getColumn())) return;

                String sql = "ALTER TABLE " + this.getTable() + " ADD "
                    + value.getColumn().getName() + " " + value.getColumn().formatType(this.databaseType);

                if (connector instanceof SQLiteConnector || value.getColumn().getType() != ColumnType.STRING) {
                    sql = sql + " DEFAULT '" + value.getValue() + "'";
                }

                SQLQueries.executeStatement(connector, sql);
            });
        }
        else if (this.type == Type.RENAME_COLUMN) {
            this.columns.forEach(value -> {
                if (!SQLQueries.hasColumn(connector, this.getTable(), value.getColumn())) return;

                String sql = "ALTER TABLE " + this.getTable() + " RENAME COLUMN " + value.getColumn().getName() + " TO " + value.getValue();
                SQLQueries.executeStatement(connector, sql);
            });
        }
        else if (this.type == Type.DROP_COLUMN) {
            this.columns.forEach(value -> {
                if (!SQLQueries.hasColumn(connector, this.getTable(), value.getColumn())) return;

                String sql = "ALTER TABLE " + this.getTable() + " DROP COLUMN " + value.getColumn().getName();
                SQLQueries.executeStatement(connector, sql);
            });
        }
        return null;
    }
}
