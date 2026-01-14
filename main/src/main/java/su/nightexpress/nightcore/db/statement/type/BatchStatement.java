package su.nightexpress.nightcore.db.statement.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BatchStatement<T> {

    @NotNull String toSql(@NotNull String table, @Nullable Wheres<T> where);

    void prepare(@NotNull PreparedStatement statement, @NotNull T entity, @Nullable Wheres<T> where) throws SQLException;
}
