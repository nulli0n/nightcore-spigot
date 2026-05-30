package su.nightexpress.nightcore.db.statement.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryStatement<R> {

    @NonNull
    String toSql(@NonNull String table, @Nullable Wheres where, @Nullable Integer limit);

    @Nullable
    R map(@NonNull ResultSet resultSet) throws SQLException;
}
