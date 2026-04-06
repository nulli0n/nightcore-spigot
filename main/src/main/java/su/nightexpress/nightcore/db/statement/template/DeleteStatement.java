package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.statement.PropertyAccessor;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DeleteStatement<T> implements BatchStatement<T> {

    @Override
    @NonNull
    public String toSql(@NonNull DatabaseType type, @NonNull String table, @Nullable Wheres<T> where) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(table);

        if (where != null && !where.isEmpty()) {
            builder.append(" WHERE ").append(where.toSql());
        }
        return builder.toString();
    }

    @Override
    public void prepare(@NonNull PreparedStatement statement, @NonNull T entity, @Nullable Wheres<T> where) throws SQLException {
        int paramCount = 1;

        if (where != null && !where.isEmpty()) {
            for (PropertyAccessor<T, Object> parameter : where.getParameters()) {
                statement.setObject(paramCount++, parameter.access(entity));
            }
        }
    }
}
