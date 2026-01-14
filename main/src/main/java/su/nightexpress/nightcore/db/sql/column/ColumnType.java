package su.nightexpress.nightcore.db.sql.column;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.config.DatabaseType;

@Deprecated
public interface ColumnType {

    ColumnType STRING = (type, length) -> {
        if (length < 1 || type == DatabaseType.SQLITE) {
            return type == DatabaseType.SQLITE ? "TEXT NOT NULL" : "MEDIUMTEXT NOT NULL";
        }
        return "varchar(" + length + ") CHARACTER SET utf8 NOT NULL";
    };

    ColumnType INTEGER = (type, length) -> {
        if (length < 1 || type == DatabaseType.SQLITE) {
            return "INTEGER NOT NULL";
        }
        return "int(" + length + ") NOT NULL";
    };

    ColumnType DOUBLE = (type, length) -> {
        return type == DatabaseType.SQLITE ? "REAL NOT NULL" : "double NOT NULL";
    };

    ColumnType LONG = (type, length) -> {
        return length < 1 || type == DatabaseType.SQLITE ? "BIGINT NOT NULL" : "bigint(" + length + ") NOT NULL";
    };

    ColumnType TIMESTAMP = (type, length) -> {
        return "TIMESTAMP NOT NULL";
    };

    ColumnType BOOLEAN = (type, length) -> {
        return type == DatabaseType.SQLITE ? "INTEGER NOT NULL" : "tinyint(1) NOT NULL";
    };

    @NotNull String build(@NotNull DatabaseType type, int length);
}
