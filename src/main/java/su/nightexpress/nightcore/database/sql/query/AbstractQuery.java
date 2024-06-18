package su.nightexpress.nightcore.database.sql.query;

import org.jetbrains.annotations.NotNull;

public class AbstractQuery {

    private final String sql;

    protected AbstractQuery(@NotNull String sql) {
        this.sql = sql;
    }

    @NotNull
    public String getSQL() {
        return sql;
    }
}
