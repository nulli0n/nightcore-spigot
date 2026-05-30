package su.nightexpress.nightcore.db.column;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.db.config.DatabaseType;

@FunctionalInterface
public interface ColumnDataType {

    ColumnDataType BOOLEAN     = type -> type == DatabaseType.SQLITE ? "INTEGER" : "TINYINT(1)";
    ColumnDataType INTEGER     = type -> type == DatabaseType.SQLITE ? "INTEGER" : "INT";
    ColumnDataType LONG        = type -> type == DatabaseType.SQLITE ? "INTEGER" : "BIGINT";
    ColumnDataType FLOAT       = type -> type == DatabaseType.SQLITE ? "REAL" : "FLOAT";
    ColumnDataType DOUBLE      = type -> type == DatabaseType.SQLITE ? "REAL" : "DOUBLE";
    ColumnDataType TINY_TEXT   = type -> type == DatabaseType.SQLITE ? "TEXT" : "TINYTEXT";
    ColumnDataType MEDIUM_TEXT = type -> type == DatabaseType.SQLITE ? "TEXT" : "MEDIUMTEXT";
    ColumnDataType LONG_TEXT   = type -> type == DatabaseType.SQLITE ? "TEXT" : "LONGTEXT";
    ColumnDataType JSON        = type -> type == DatabaseType.SQLITE ? "TEXT" : "JSON";
    ColumnDataType UUID        = type -> type == DatabaseType.SQLITE ? "TEXT" : "VARCHAR(36)";
    ColumnDataType TIMESTAMP   = type -> "TIMESTAMP(6) NOT NULL";

    @NonNull
    static ColumnDataType string(int length) {
        if (length <= 0) throw new IllegalArgumentException("Length must be > 0");

        return type -> type == DatabaseType.SQLITE ? "TEXT" : "VARCHAR(" + length + ") CHARACTER SET utf8mb4";
    }

    @NonNull
    String toSql(@NonNull DatabaseType type);

    @NonNull
    default String toSql(@NonNull DatabaseType type, @NonNull NullOption nullOption) {
        String sql = this.toSql(type);
        return nullOption == NullOption.NULLABLE ? sql : sql + " NOT NULL";
    }
}
