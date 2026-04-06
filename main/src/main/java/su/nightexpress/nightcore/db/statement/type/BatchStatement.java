package su.nightexpress.nightcore.db.statement.type;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BatchStatement<T> {

    @NonNull String toSql(@NonNull DatabaseType type, @NonNull String table, @Nullable Wheres<T> where);

    void prepare(@NonNull PreparedStatement statement, @NonNull T entity, @Nullable Wheres<T> where) throws SQLException;
}
