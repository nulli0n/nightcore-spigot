package su.nightexpress.nightcore.userdata;

import java.util.UUID;

import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public class UserDataQueries {

    public static final Column<UUID>   USER_ID_COLUMN   = Column.uuidType("player_uuid").primaryKey().build();
    public static final Column<String> USER_NAME_COLUMN = Column.stringType("player_name", 32).build();
    public static final Column<String> LAST_SKIN_COLUMN = Column.stringType("last_skin_url", 128).nullable().build();
    public static final Column<Long>   LAST_SEEN_COLUMN = Column.longType("last_seen").defaultValue(0L).build();

    private UserDataQueries() {
    }

    public static final RowMapper<UserData> USER_ROW_MAPPER = resultSet -> {
        UUID userId = USER_ID_COLUMN.readOrThrow(resultSet);
        String userName = USER_NAME_COLUMN.readOrThrow(resultSet);
        String lastSkinUrl = LAST_SKIN_COLUMN.readOrThrow(resultSet);
        long lastSeen = LAST_SEEN_COLUMN.readOrThrow(resultSet);

        UserData data = new UserData(userId, userName);
        data.setLastSkinUrl(lastSkinUrl);
        data.setLastSeen(lastSeen);
        data.refreshProfile();
        return data;
    };

    public static final SelectStatement<UserData> USER_SELECT = SelectStatement
        .builder(UserDataQueries.USER_ROW_MAPPER)
        .build();

    public static final InsertStatement<UserData> USER_INSERT = InsertStatement.builder(
        UserData.class)
        .updateOnConflict()
        .setUUID(USER_ID_COLUMN, UserData::getId)
        .setString(USER_NAME_COLUMN, UserData::getName)
        .setString(LAST_SKIN_COLUMN, UserData::getLastSkinUrl)
        .build();
}
