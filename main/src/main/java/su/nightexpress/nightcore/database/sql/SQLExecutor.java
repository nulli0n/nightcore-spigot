package su.nightexpress.nightcore.database.sql;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.AbstractConnector;

@Deprecated
public abstract class SQLExecutor<T> {

    protected final String table;

    protected SQLExecutor(@NonNull String table) {
        this.table = table;
    }

    @NonNull
    public String getTable() {
        return "`" + this.table + "`";
    }

    @NonNull
    public abstract T execute(@NonNull AbstractConnector connector);
}
