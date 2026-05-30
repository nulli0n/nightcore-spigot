package su.nightexpress.nightcore.db.sql.query.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public abstract class AbstractQuery<T> {

    @NonNull
    public String createSQL(@NonNull String table) {
        return this.buildSQL(SQLUtils.escape(table));
    }

    public abstract boolean isEmpty();

    public abstract void onExecute(@NonNull PreparedStatement statement, @NonNull T entity) throws SQLException;

    @NonNull
    protected abstract String buildSQL(@NonNull String table);
}
