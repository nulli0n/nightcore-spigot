package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;

public class AbstractSQLQuery {

    private final String sql;

    protected AbstractSQLQuery(@NotNull String sql) {
        this.sql = sql;
    }

    @NotNull
    public String getSQL() {
        return sql;
    }
}
