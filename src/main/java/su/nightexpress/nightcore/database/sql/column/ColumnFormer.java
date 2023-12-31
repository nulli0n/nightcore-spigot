package su.nightexpress.nightcore.database.sql.column;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.DatabaseType;

public interface ColumnFormer {

    ColumnFormer STRING = (storageType, length) -> {
        if (length < 1 || storageType == DatabaseType.SQLITE) {
            return storageType == DatabaseType.SQLITE ? "TEXT NOT NULL" : "MEDIUMTEXT NOT NULL";
        }
        return "varchar(" + length + ") CHARACTER SET utf8 NOT NULL";
    };

    ColumnFormer INTEGER = (storageType, length) -> {
        if (length < 1 || storageType == DatabaseType.SQLITE) {
            return "INTEGER NOT NULL";
        }
        return "int(" + length + ") NOT NULL";
    };

    ColumnFormer DOUBLE = (storageType, length) -> {
        return storageType == DatabaseType.SQLITE ? "REAL NOT NULL" : "double NOT NULL";
    };

    ColumnFormer LONG = (storageType, length) -> {
        return length < 1 || storageType == DatabaseType.SQLITE ? "BIGINT NOT NULL" : "bigint(" + length + ") NOT NULL";
    };

    ColumnFormer BOOLEAN = (storageType, length) -> {
        return storageType == DatabaseType.SQLITE ? "INTEGER NOT NULL" : "tinyint(1) NOT NULL";
    };

    @NotNull String build(@NotNull DatabaseType databaseType, int length);
}
