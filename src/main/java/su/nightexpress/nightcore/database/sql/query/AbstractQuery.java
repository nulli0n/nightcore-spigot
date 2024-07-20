package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.sql.SQLUtils;

public class AbstractQuery {

    protected final String table;
    protected final String sql;

    protected AbstractQuery(@NotNull String table, @NotNull String sql) {
        this.table = SQLUtils.escape(table);
        this.sql = sql;
    }

    @NotNull
    public String getTable() {
        return table;
    }

    @NotNull
    public String getSQL() {
        return sql;
    }
}
