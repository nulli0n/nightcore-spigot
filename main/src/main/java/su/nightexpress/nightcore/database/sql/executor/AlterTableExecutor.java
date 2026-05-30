package su.nightexpress.nightcore.database.sql.executor;

import org.jspecify.annotations.NonNull;
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

@Deprecated
public final class AlterTableExecutor extends SQLExecutor<Void> {

    private final DatabaseType   databaseType;
    private final List<SQLValue> columns;
    private Type                 type;

    private AlterTableExecutor(@NonNull String table, @NonNull DatabaseType databaseType) {
        super(table);
        this.databaseType = databaseType;
        this.columns = new ArrayList<>();
    }

    private enum Type {
        ADD_COLUMN,
        RENAME_COLUMN,
        DROP_COLUMN
    }

    @NonNull
    public static AlterTableExecutor builder(@NonNull String table, @NonNull DatabaseType databaseType) {
        return new AlterTableExecutor(table, databaseType);
    }

    @NonNull
    public AlterTableExecutor addColumn(@NonNull SQLValue... columns) {
        return this.addColumn(Arrays.asList(columns));
    }

    @NonNull
    public AlterTableExecutor addColumn(@NonNull List<SQLValue> columns) {
        return this.columns(columns, Type.ADD_COLUMN);
    }

    @NonNull
    public AlterTableExecutor renameColumn(@NonNull SQLValue... columns) {
        return this.addColumn(Arrays.asList(columns));
    }

    @NonNull
    public AlterTableExecutor renameColumn(@NonNull List<SQLValue> columns) {
        return this.columns(columns, Type.RENAME_COLUMN);
    }

    @NonNull
    public AlterTableExecutor dropColumn(@NonNull SQLColumn... columns) {
        return this.dropColumn(Arrays.asList(columns));
    }

    @NonNull
    public AlterTableExecutor dropColumn(@NonNull List<SQLColumn> columns) {
        return this.columns(columns.stream().map(column -> column.toValue("dummy")).toList(), Type.DROP_COLUMN);
    }

    private AlterTableExecutor columns(@NonNull List<SQLValue> values, @NonNull Type type) {
        this.columns.clear();
        this.columns.addAll(values);
        this.type = type;
        return this;
    }

    @Override
    @NonNull
    public Void execute(@NonNull AbstractConnector connector) {
        if (this.columns.isEmpty()) return null;

        if (this.type == Type.ADD_COLUMN) {
            this.columns.forEach(value -> {
                if (SQLQueries.hasColumn(connector, this.getTable(), value.getColumn())) return;

                String sql = "ALTER TABLE " + this.getTable() + " ADD " + value.getColumn().getName() + " " + value
                    .getColumn().formatType(this.databaseType);

                if (connector instanceof SQLiteConnector || value.getColumn().getType() != ColumnType.STRING) {
                    sql = sql + " DEFAULT '" + value.getValue() + "'";
                }

                SQLQueries.executeStatement(connector, sql);
            });
        }
        else if (this.type == Type.RENAME_COLUMN) {
            this.columns.forEach(value -> {
                if (!SQLQueries.hasColumn(connector, this.getTable(), value.getColumn())) return;

                String sql = "ALTER TABLE " + this.getTable() + " RENAME COLUMN " + value.getColumn().getName() +
                    " TO " + value.getValue();
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
