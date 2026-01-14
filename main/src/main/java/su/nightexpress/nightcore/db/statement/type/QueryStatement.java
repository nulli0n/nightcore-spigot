package su.nightexpress.nightcore.db.statement.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryStatement<R> {

    @NotNull String toSql(@NotNull String table, @Nullable Wheres where, @Nullable Integer limit);

    @Nullable R map(@NotNull ResultSet resultSet) throws SQLException;
}
