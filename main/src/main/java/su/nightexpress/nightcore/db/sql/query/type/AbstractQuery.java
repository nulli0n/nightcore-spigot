package su.nightexpress.nightcore.db.sql.query.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public abstract class AbstractQuery<T> {

    @NotNull
    public String createSQL(@NotNull String table) {
        return this.buildSQL(SQLUtils.escape(table));
    }

    public abstract boolean isEmpty();

    public abstract void onExecute(@NotNull PreparedStatement statement, @NotNull T entity) throws SQLException;

    @NotNull
    protected abstract String buildSQL(@NotNull String table);
}
