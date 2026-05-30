package su.nightexpress.nightcore.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.statement.SQLStatements;

public class DataSynchronizer {

    public static final Column<Timestamp> LAST_UPDATED_TIME_COLUMN = Column.timestamp("last_modified").build();
    public static final Column<String>    LAST_UPDATED_BY_COLUMN   = Column.stringType("last_updated_by", 36)
        .defaultValue(DatabaseConstants.UNKNOWN_INSTANCE)
        .build();

    private static final String TRIGGER_TEMPLATE = """
        CREATE TRIGGER IF NOT EXISTS %s_before_%s
        BEFORE %s ON %s
        FOR EACH ROW
        BEGIN
            SET NEW.last_updated_by = IFNULL(%s, '%s');
        END;
        """;

    private final String            serverInstanceId;
    private final AbstractConnector connector;

    private final Map<String, Consumer<ResultSet>> synchronics;
    private final Map<String, Timestamp>           lastSyncTimes;

    public DataSynchronizer(@NonNull String serverInstanceId, @NonNull AbstractConnector connector) {
        this.serverInstanceId = serverInstanceId;
        this.connector = connector;
        this.synchronics = new ConcurrentHashMap<>();
        this.lastSyncTimes = new ConcurrentHashMap<>();
    }

    public void addTable(@NonNull String tableName, @NonNull Consumer<ResultSet> consumer) {
        DatabaseType dataType = this.connector.getType();

        if (!SQLStatements.hasColumn(this.connector, tableName, LAST_UPDATED_TIME_COLUMN.getName())) {
            SQLStatements.executeUpdate(this.connector,
                "ALTER TABLE %s ADD COLUMN %s DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)"
                    .formatted(tableName, LAST_UPDATED_TIME_COLUMN.toSqlNameType(this.connector.getType()))
            );
        }

        if (!SQLStatements.hasColumn(this.connector, tableName, "last_updated_by")) {
            SQLStatements.executeUpdate(this.connector,
                "ALTER TABLE %s ADD COLUMN %s"
                    .formatted(tableName, LAST_UPDATED_BY_COLUMN.toSqlWithDefault(dataType))
            );
        }

        // Create BEFORE UPDATE & INSERT Triggers
        SQLStatements.executeUpdate(this.connector, TRIGGER_TEMPLATE
            .formatted(tableName, "insert", "INSERT", tableName,
                DatabaseConstants.SQL_VAR_SERVER_INSTANCE,
                DatabaseConstants.UNKNOWN_INSTANCE)
        );

        SQLStatements.executeUpdate(this.connector, TRIGGER_TEMPLATE
            .formatted(tableName, "update", "UPDATE", tableName,
                DatabaseConstants.SQL_VAR_SERVER_INSTANCE,
                DatabaseConstants.UNKNOWN_INSTANCE)
        );

        this.synchronics.put(tableName, consumer);

        this.lastSyncTimes.put(tableName, this.fetchMaxTimestamp(tableName));
    }

    public void syncAll() {
        this.synchronics.forEach(this::syncTable);
    }

    private @NonNull Timestamp fetchMaxTimestamp(@NonNull String table) {
        String sql = "SELECT MAX(%s) FROM %s"
            .formatted(LAST_UPDATED_TIME_COLUMN.getName(), table);

        try (Connection connection = this.connector.getConnection();

             Statement statement = connection.createStatement();

             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                Timestamp maxValue = resultSet.getTimestamp(1);
                if (maxValue != null) {
                    return maxValue;
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        return new Timestamp(0L);
    }

    public void syncTable(@NonNull String tableName, @NonNull Consumer<ResultSet> consumer) {
        Timestamp lastTimestamp = this.lastSyncTimes.getOrDefault(tableName, new Timestamp(0L));
        Timestamp highestTimestamp = lastTimestamp;

        String query = "SELECT * FROM %s WHERE %s > ? AND %s != ?"
            .formatted(tableName, LAST_UPDATED_TIME_COLUMN.getName(), LAST_UPDATED_BY_COLUMN.getName());

        try (Connection connection = this.connector.getConnection(); PreparedStatement statement = connection
            .prepareStatement(query)) {

            statement.setTimestamp(1, lastTimestamp);
            statement.setString(2, this.serverInstanceId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Timestamp timestamp = LAST_UPDATED_TIME_COLUMN.read(resultSet).orElse(lastTimestamp);
                if (timestamp.after(highestTimestamp)) {
                    highestTimestamp = timestamp;
                }

                consumer.accept(resultSet);
            }
            resultSet.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        this.lastSyncTimes.put(tableName, highestTimestamp);
    }
}
