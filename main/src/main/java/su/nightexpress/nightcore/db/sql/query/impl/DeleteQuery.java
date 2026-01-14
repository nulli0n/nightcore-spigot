package su.nightexpress.nightcore.db.sql.query.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.sql.query.type.ConditionalQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public class DeleteQuery<T> extends ConditionalQuery<DeleteQuery<T>, T> {

    public DeleteQuery() {

    }

    @Override
    protected DeleteQuery<T> getThis() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @NotNull
    protected String buildSQL(@NotNull String table) {
        StringBuilder builder = new StringBuilder().append("DELETE FROM ").append(table);

        String wheres = this.buildWhereSQLPart();
        if (!wheres.isBlank()) {
            builder.append(" WHERE ").append(wheres);
        }

        return builder.toString();
    }

    @Override
    public void onExecute(@NotNull PreparedStatement statement, @NotNull T entity) throws SQLException {
        int paramCount = 1;

        for (int index = 0; index < this.countWhereColumns(); index++) {
            statement.setString(paramCount++, this.getWhereValue(entity, index));
        }
    }
}
