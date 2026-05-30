package su.nightexpress.nightcore.db.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jspecify.annotations.NonNull;

public record ColumnMapping<T, R>(@NonNull PropertyAccessor<T, R> accessor, @NonNull ParameterBinder<R> binder) {

    public void apply(@NonNull PreparedStatement statement, int index, @NonNull T entity) throws SQLException {
        R data = this.accessor.access(entity);
        if (data == null) {
            statement.setObject(index, null);
        }
        else {
            this.binder.bind(statement, index, data);
        }
    }
}