package su.nightexpress.nightcore.db.query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.query.data.Values;
import su.nightexpress.nightcore.db.query.data.Wheres;
import su.nightexpress.nightcore.util.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

@Deprecated
public abstract class TypedQuery<T> implements Query {

    @Nullable
    protected abstract Values<T> statementValues();

    @Nullable
    protected abstract Wheres<T> statementWheres();

    public void execute(@NotNull AbstractConnector connector, @NotNull String table, @NotNull T entity) {
        this.execute(connector, table, Lists.newList(entity));
    }

    public void execute(@NotNull AbstractConnector connector, @NotNull String table, @NotNull Collection<T> entities) {
        if (this.isEmpty()) return;

        String sql = this.toSQL(table).trim();
        Values<T> values = this.statementValues();
        Wheres<T> wheres = this.statementWheres();

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int entityCount = 0;
            for (T entity : entities) {

                int paramCount = 1;

                if (values != null) {
                    for (int index = 0; index < values.count(); index++) {
                        statement.setString(paramCount++, values.getValue(entity, index));
                    }
                }

                if (wheres != null) {
                    for (int index = 0; index < wheres.count(); index++) {
                        statement.setString(paramCount++, wheres.getValue(entity, index));
                    }
                }

                //this.onExecute(statement, entity);
                //System.out.println("myQuery: " + statement);
                statement.addBatch();
                entityCount++;

                if (entityCount % 500 == 0 || entityCount == entities.size()) {
                    statement.executeBatch();
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
