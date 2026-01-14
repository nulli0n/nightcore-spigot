package su.nightexpress.nightcore.db.sql.column;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.sql.util.SQLUtils;

@Deprecated
public class Column {

    private final String     name;
    private final ColumnType type;
    private final int        length;

    public Column(@NotNull String name, @NotNull ColumnType type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    @NotNull
    public String formatType(@NotNull DatabaseType databaseType) {
        return this.type.build(databaseType, this.length);
    }

    @NotNull
    public static Column of(@NotNull String name, @NotNull ColumnType type) {
        return Column.of(name, type, -1);
    }

    @NotNull
    public static Column of(@NotNull String name, @NotNull ColumnType type, int length) {
        return new Column(name, type, length);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getNameEscaped() {
        return SQLUtils.escape(this.name);
    }

    @NotNull
    public String getNameLowercase() {
        return "LOWER(" + this.name + ")";
    }

    @NotNull
    public ColumnType getType() {
        return this.type;
    }

    public int getLength() {
        return this.length;
    }
}
