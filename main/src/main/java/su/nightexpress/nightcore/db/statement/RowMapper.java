package su.nightexpress.nightcore.db.statement;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<R> {

    @Nullable
    R map(@NonNull ResultSet resultSet) throws SQLException;
}
