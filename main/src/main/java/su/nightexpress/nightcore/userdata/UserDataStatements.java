package su.nightexpress.nightcore.userdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import su.nightexpress.nightcore.db.AbstractDatabaseManager;
import su.nightexpress.nightcore.util.LowerCase;

public class UserDataStatements {

    // Just a temp trash class with some statements

    public static List<UserData> selectByIds(AbstractDatabaseManager<?> database, String table, Set<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) {
            return Collections.emptyList();
        }

        // Dynamically create placeholders (?, ?, ?)
        String placeholders = String.join(",", Collections.nCopies(uuids.size(), "?"));
        String sql = "SELECT * FROM " + table + " WHERE " + UserDataQueries.USER_ID_COLUMN.getName() + " IN (" +
            placeholders + ")";

        List<UserData> users = new ArrayList<>();

        try (
            Connection connection = database.getConnector().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (UUID uuid : uuids) {
                statement.setString(index, uuid.toString());
                index++;
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(UserDataQueries.USER_ROW_MAPPER.map(resultSet));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<UserData> selectByNames(AbstractDatabaseManager<?> database, String table, Set<String> names) {
        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }

        // Dynamically create placeholders (?, ?, ?)
        String placeholders = String.join(",", Collections.nCopies(names.size(), "?"));
        String sql = "SELECT * FROM " + table + " WHERE LOWER(" + UserDataQueries.USER_NAME_COLUMN.getName() +
            ") IN (" + placeholders + ")";

        List<UserData> users = new ArrayList<>();

        try (
            Connection connection = database.getConnector().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (String name : names) {
                statement.setString(index, LowerCase.internal(name));
                index++;
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(UserDataQueries.USER_ROW_MAPPER.map(resultSet));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
