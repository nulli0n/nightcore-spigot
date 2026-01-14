package su.nightexpress.nightcore.db;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.column.ColumnType;
import su.nightexpress.nightcore.db.statement.SQLStatements;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DataSynchronizer {

    public static final Column COLUMN_LAST_MODIFIED = Column.of("last_modified", ColumnType.TIMESTAMP);

    private final AbstractConnector connector;

    private final Map<String, Consumer<ResultSet>> synchronics;
    private final Map<String, Timestamp>           lastSyncTimes;

    public DataSynchronizer(@NotNull AbstractConnector connector) {
        this.connector = connector;
        this.synchronics = new HashMap<>();
        this.lastSyncTimes = new HashMap<>();
    }

    public void addTable(@NotNull String tableName, @NotNull Consumer<ResultSet> consumer) {
        if (!SQLStatements.hasColumn(this.connector, tableName, COLUMN_LAST_MODIFIED.getName())) {
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + COLUMN_LAST_MODIFIED.getName() + " TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP";
            SQLStatements.executeUpdate(this.connector, sql);
        }

        this.synchronics.put(tableName, consumer);
    }

    public void syncAll() {
        this.synchronics.forEach(this::syncTable);
    }

    public void syncTable(@NotNull String tableName, @NotNull Consumer<ResultSet> consumer) {
        Timestamp lastSync = this.lastSyncTimes.getOrDefault(tableName, Timestamp.from(Instant.now()));

        String query = "SELECT * FROM " + tableName + " WHERE " + COLUMN_LAST_MODIFIED.getName() + " > ?";
        try (Connection connection = this.connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                consumer.accept(resultSet);
            }
            resultSet.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        this.lastSyncTimes.put(tableName, Timestamp.from(Instant.now()));
    }
}
