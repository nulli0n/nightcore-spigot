package su.nightexpress.nightcore.db.statement;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public record ColumnMapping<T, R>(@NotNull PropertyAccessor<T, R> accessor, @NotNull ParameterBinder<R> binder) {

    public void apply(@NotNull PreparedStatement statement, int index, @NotNull T entity) throws SQLException {
        this.binder.bind(statement, index, this.accessor.access(entity));
    }
}
