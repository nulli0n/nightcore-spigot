package su.nightexpress.nightcore.db.sql.column;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;

@Deprecated
public class Column {

    private final String     name;
    private final ColumnType type;
    private final int        length;

    public Column(@NonNull String name, @NonNull ColumnType type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    @NonNull
    public String formatType(@NonNull DatabaseType databaseType) {
        return this.type.build(databaseType, this.length);
    }

    @NonNull
    public static Column of(@NonNull String name, @NonNull ColumnType type) {
        return Column.of(name, type, -1);
    }

    @NonNull
    public static Column of(@NonNull String name, @NonNull ColumnType type, int length) {
        return new Column(name, type, length);
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getNameEscaped() {
        return SQLUtils.escape(this.name);
    }

    @NonNull
    public String getNameLowercase() {
        return "LOWER(" + this.name + ")";
    }

    @NonNull
    public ColumnType getType() {
        return this.type;
    }

    public int getLength() {
        return this.length;
    }
}
