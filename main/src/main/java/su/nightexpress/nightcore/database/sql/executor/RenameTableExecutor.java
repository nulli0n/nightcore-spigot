package su.nightexpress.nightcore.database.sql.executor;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.DatabaseType;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.SQLExecutor;
import su.nightexpress.nightcore.database.sql.SQLQueries;

@Deprecated
public final class RenameTableExecutor extends SQLExecutor<Void> {

    private final DatabaseType databaseType;
    private String             renameTo;

    private RenameTableExecutor(@NonNull String table, @NonNull DatabaseType databaseType) {
        super(table);
        this.databaseType = databaseType;
    }

    @NonNull
    public static RenameTableExecutor builder(@NonNull String table, @NonNull DatabaseType databaseType) {
        return new RenameTableExecutor(table, databaseType);
    }

    @NonNull
    public RenameTableExecutor renameTo(@NonNull String renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    @Override
    @NonNull
    public Void execute(@NonNull AbstractConnector connector) {
        if (this.renameTo == null || this.renameTo.isEmpty()) return null;
        if (!SQLQueries.hasTable(connector, this.getTable())) return null;

        StringBuilder sql = new StringBuilder();
        if (this.databaseType == DatabaseType.MYSQL) {
            sql.append("RENAME TABLE ").append(this.getTable()).append(" TO ").append(this.renameTo).append(";");
        }
        else {
            sql.append("ALTER TABLE ").append(this.getTable()).append(" RENAME TO ").append(this.renameTo);
        }
        SQLQueries.executeStatement(connector, sql.toString());
        return null;
    }
}
