package su.nightexpress.nightcore.db.sql.query;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.connection.impl.SQLiteConnector;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.column.ColumnType;
import su.nightexpress.nightcore.db.sql.query.type.AbstractQuery;
import su.nightexpress.nightcore.db.sql.query.impl.SelectQuery;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;
import su.nightexpress.nightcore.util.Lists;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
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

    public static boolean hasColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull Column column) {
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

    public static void addColumn(@NotNull AbstractConnector connector,
                                 @NotNull DatabaseType type,
                                 @NotNull String table,
                                 @NotNull Column column,
                                 @NotNull String defVal) {
        if (SQLQueries.hasColumn(connector, table, column)) return;

        StringBuilder builder = new StringBuilder()
            .append("ALTER TABLE ").append(table).append(" ADD ")
            .append(column.getName()).append(" ").append(column.formatType(type));

        if (connector instanceof SQLiteConnector || column.getType() != ColumnType.STRING) {
            builder.append(" DEFAULT ").append("'").append(defVal).append("'");
        }

        SQLQueries.executeSimpleQuery(connector, builder.toString());
    }

    public static void renameColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull Column column, @NotNull String toName) {
        renameColumn(connector, table, column.getName(), toName);
    }

    public static void renameColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull String columnName, @NotNull String toName) {
        if (!SQLQueries.hasColumn(connector, table, columnName)) return;

        String sql = "ALTER TABLE " + table + " RENAME COLUMN " + columnName + " TO " + toName;

        SQLQueries.executeSimpleQuery(connector, sql);
    }

    public static void dropColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull Column column) {
        dropColumn(connector, table, column.getName());
    }

    public static void dropColumn(@NotNull AbstractConnector connector, @NotNull String table, @NotNull String columnName) {
        if (!SQLQueries.hasColumn(connector, table, columnName)) return;

        String sql = "ALTER TABLE " + table + " DROP COLUMN " + columnName;

        SQLQueries.executeSimpleQuery(connector, sql);
    }

    public static void createTable(@NotNull AbstractConnector connector, @NotNull DatabaseType type, @NotNull String table, @NotNull List<Column> columns) {
        if (columns.isEmpty()) return;

        StringBuilder idBuilder = new StringBuilder(SQLUtils.escape("id")).append(" ").append(ColumnType.INTEGER.build(type, 11));

        if (type == DatabaseType.SQLITE) {
            idBuilder.append(" PRIMARY KEY AUTOINCREMENT");
        }
        else {
            idBuilder.append(" PRIMARY KEY AUTO_INCREMENT");
        }

        String columnNames = columns.stream()
            .map(column -> column.getNameEscaped() + " " + column.formatType(type)).collect(Collectors.joining(", "));

        String allColumns = idBuilder + ", " + columnNames;

        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(" + allColumns + ");";

        SQLQueries.executeSimpleQuery(connector, sql);
    }
    
    public static void renameTable(@NotNull AbstractConnector connector, @NotNull DatabaseType type, @NotNull String table, @NotNull String toName) {
        if (!SQLQueries.hasTable(connector, table)) return;

        StringBuilder sql = new StringBuilder();
        if (type == DatabaseType.MYSQL) {
            sql.append("RENAME TABLE ").append(table).append(" TO ").append(toName).append(";");
        }
        else {
            sql.append("ALTER TABLE ").append(table).append(" RENAME TO ").append(toName);
        }
        SQLQueries.executeSimpleQuery(connector, sql.toString());
    }

    public static void executeSimpleQuery(@NotNull AbstractConnector connector, @NotNull String sql) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            //System.out.println("simpleQuery: " + statement.toString());

            statement.executeUpdate();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static <T> void executeQuery(@NotNull AbstractConnector connector, @NotNull String table, @NotNull AbstractQuery<T> query, @NotNull T entity) {
        executeQuery(connector, table, query, Lists.newList(entity));
    }

    public static <T> void executeQuery(@NotNull AbstractConnector connector, @NotNull String table, @NotNull AbstractQuery<T> query, @NotNull Collection<T> entities) {
        if (query.isEmpty()) return;

        String sql = query.createSQL(table);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int entityCount = 0;
            for (T entity : entities) {
                query.onExecute(statement, entity);
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

    @NotNull
    public static <T> List<T> executeSelect(@NotNull AbstractConnector connector, @NotNull String table, @NotNull SelectQuery<T> query) {
        ArrayList<T> list = new ArrayList<>();

        if (query.isEmpty()) return list;

        String sql = query.createSQL(table);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            query.onExecute(statement, list);
            //System.out.println("selectQuery: " + statement);
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        return list;
    }
}
