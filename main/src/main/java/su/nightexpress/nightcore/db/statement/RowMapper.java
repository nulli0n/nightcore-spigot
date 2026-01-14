package su.nightexpress.nightcore.db.statement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<R> {

    @Nullable R map(@NotNull ResultSet resultSet) throws SQLException;
}
