package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.PropertyAccessor;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DeleteStatement<T> implements BatchStatement<T> {

    @Override
    @NotNull
    public String toSql(@NotNull String table, @Nullable Wheres<T> where) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(table);

        if (where != null && !where.isEmpty()) {
            builder.append(" WHERE ").append(where.toSql());
        }
        return builder.toString();
    }

    @Override
    public void prepare(@NotNull PreparedStatement statement, @NotNull T entity, @Nullable Wheres<T> where) throws SQLException {
        int paramCount = 1;

        if (where != null && !where.isEmpty()) {
            for (PropertyAccessor<T, Object> parameter : where.getParameters()) {
                statement.setObject(paramCount++, parameter.access(entity));
            }
        }
    }
}
