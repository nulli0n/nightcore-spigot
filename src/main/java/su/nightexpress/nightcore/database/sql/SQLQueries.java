package su.nightexpress.nightcore.database.sql;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.AbstractConnector;
import su.nightexpress.nightcore.database.sql.query.UpdateEntity;
import su.nightexpress.nightcore.database.sql.query.UpdateQuery;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class SQLQueries {

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

    public static boolean hasColumn(@NotNull AbstractConnector connector,
                                    @NotNull String table,
                                    @NotNull SQLColumn column) {
        String sql = "SELECT * FROM " + table;
        String columnName = column.getName();
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

    public static void executeStatement(@NotNull AbstractConnector connector,
                                        @NotNull String sql) {
        executeStatement(connector, sql, Collections.emptySet());
    }

    public static void executeStatement(@NotNull AbstractConnector connector,
                                        @NotNull String sql,
                                        @NotNull Collection<String> values1) {
        executeStatement(connector, sql, values1, Collections.emptySet());
    }

    public static void executeStatement(@NotNull AbstractConnector connector,
                                        @NotNull String sql,
                                        @NotNull Collection<String> values1,
                                        @NotNull Collection<String> values2) {

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int count = 1;
            for (String columnName : values1) {
                statement.setString(count++, columnName);
            }
            for (String columnValue : values2) {
                statement.setString(count++, columnValue);
            }

            statement.executeUpdate();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void executeUpdate(@NotNull AbstractConnector connector, @NotNull UpdateQuery query) {
        executeUpdates(connector, query);
    }

    public static void executeUpdates(@NotNull AbstractConnector connector, @NotNull UpdateQuery query) {
        if (query.isEmpty()) return;

        List<UpdateEntity> entities = query.getEntities();
        //if (entities.isEmpty()) return;

        //if (queries.isEmpty()) return;

//        try (Connection connection = connector.getConnection()) {
//            for (UpdateQuery query : queries) {
//                try (PreparedStatement statement = connection.prepareStatement(query.getSQL())) {
//
//                    int count = 1;
//                    for (String columnValue : query.getValues()) {
//                        statement.setString(count++, columnValue);
//                    }
//                    for (String conditionValue : query.getWheres()) {
//                        statement.setString(count++, conditionValue);
//                    }
//
//                    statement.executeUpdate();
//                }
//                catch (SQLException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }
//        catch (SQLException exception) {
//            exception.printStackTrace();
//        }

        String sql = query.getSQL();//queries.get(0).getSQL();

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int entityCount = 0;
            for (UpdateEntity entity : entities) {
                int paramCount = 1;

                for (String columnValue : entity.getValues()) {
                    statement.setString(paramCount++, columnValue);
                }
                for (String conditionValue : entity.getWheres()) {
                    statement.setString(paramCount++, conditionValue);
                }
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

    @NotNull
    public static <T> List<@NotNull T> executeQuery(@NotNull AbstractConnector connector,
                                                    @NotNull String sql,
                                                    @NotNull Collection<String> values,
                                                    @NotNull Function<ResultSet, T> dataFunction,
                                                    int amount) {

        List<T> list = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int count = 1;
            for (String wValue : values) {
                statement.setString(count++, wValue);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next() && (amount < 0 || list.size() < amount)) {
                list.add(dataFunction.apply(resultSet));
            }
            resultSet.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        list.removeIf(Objects::isNull);

        return list;
    }
}
