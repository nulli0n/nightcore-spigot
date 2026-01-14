package su.nightexpress.nightcore.db.statement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;
import su.nightexpress.nightcore.db.statement.type.QueryStatement;
import su.nightexpress.nightcore.db.table.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SQLStatements {

    public static boolean hasTable(@NotNull AbstractConnector connector, @NotNull String table) {
        try (Connection connection = connector.getConnection()) {

            boolean has;
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, table, null);
            has = tables.next();
            tables.close();
            return has;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean hasColumn(@NotNull AbstractConnector connector, @NotNull Table table, @NotNull Column<?> column) {
        return hasColumn(connector, table.getName(), column.getName());
    }

    public static boolean hasColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull Column<?> column) {
        return hasColumn(connector, table, column.getName());
    }

    public static boolean hasColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull String columnName) {
        String sql = "SELECT * FROM " + table;
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            for (int index = 1; index <= columns; index++) {
                if (columnName.equals(metaData.getColumnName(index))) {
                    return true;
                }
            }
            return false;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static void executeUpdate(@NotNull AbstractConnector connector, @NotNull String sql) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    
    @NotNull
    public static <R> List<R> select(@NotNull AbstractConnector connector,
                                     @NotNull String table,
                                     @NotNull QueryStatement<R> query,
                                     @Nullable Wheres<Object> where,
                                     @Nullable Integer limit) {
        ArrayList<R> results = new ArrayList<>();
        Object entity = new Object(); // dummy object to get WHERE values.

        String sql = query.toSql(table, where, limit);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (where != null) {
                List<PropertyAccessor<Object, Object>> params = where.getParameters();
                for (int index = 0; index < params.size(); index++) {
                    statement.setObject(index + 1, params.get(index).access(entity));
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    R result = query.map(resultSet);
                    if (result == null) continue;

                    results.add(result);
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        return results;
    }

    @NotNull
    public static <R> List<R> selectAny(@NotNull AbstractConnector connector, @NotNull String table, @NotNull QueryStatement<R> query) {
        return select(connector, table, query, null, null);
    }

    @NotNull
    public static <R> Optional<R> selectFirst(@NotNull AbstractConnector connector, @NotNull String table, @NotNull QueryStatement<R> query, @NotNull Wheres<Object> where) {
        return Optional.of(select(connector, table, query, where, 1)).filter(Predicate.not(List::isEmpty)).map(List::getFirst);
    }

    @NotNull
    public static <R> Optional<R> selectAnyFirst(@NotNull AbstractConnector connector, @NotNull String table, @NotNull QueryStatement<R> query) {
        return Optional.of(select(connector, table, query, null, 1)).filter(Predicate.not(List::isEmpty)).map(List::getFirst);
    }

    public static <T> void executeBatch(@NotNull AbstractConnector connector,
                                        @NotNull String table,
                                        @NotNull BatchStatement<T> query,
                                        @NotNull T entity,
                                        @Nullable Wheres<T> where) {
        executeBatch(connector, table, query, List.of(entity), where);
    }

    public static <T> void executeBatch(@NotNull AbstractConnector connector,
                                        @NotNull String table,
                                        @NotNull BatchStatement<T> query,
                                        @NotNull Collection<T> entities,
                                        @Nullable Wheres<T> where) {
        if (entities.isEmpty()) return;

        String sql = query.toSql(table, where);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int entityCount = 0;
            int batchSize = 500;

            for (T entity : entities) {
                query.prepare(statement, entity, where);
                statement.addBatch();
                statement.clearParameters();
                entityCount++;

                if (entityCount % batchSize == 0 || entityCount == entities.size()) {
                    statement.executeBatch();
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
