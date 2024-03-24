package su.nightexpress.nightcore.database.sql;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.DatabaseType;
import su.nightexpress.nightcore.database.sql.column.ColumnType;

public class SQLColumn {

    private final String     name;
    private final String     nameEscaped;
    private final ColumnType type;
    private final int        length;

    public SQLColumn(@NotNull String name, @NotNull ColumnType type, int length) {
        this(name, "`" + name + "`", type, length);
    }

    public SQLColumn(@NotNull String name, @NotNull String nameEscaped, @NotNull ColumnType type, int length) {
        this.name = name;
        this.nameEscaped = nameEscaped;
        this.type = type;
        this.length = length;
    }

    @NotNull
    public static SQLColumn of(@NotNull String name, @NotNull ColumnType type) {
        return SQLColumn.of(name, type, -1);
    }

    @NotNull
    public static SQLColumn of(@NotNull String name, @NotNull ColumnType type, int length) {
        return new SQLColumn(name, type, length);
    }

    @NotNull
    public SQLColumn asLowerCase() {
        String name = "LOWER(" + this.getName() + ")";

        return new SQLColumn(name, name, this.getType(), this.getLength());
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getNameEscaped() {
        return this.nameEscaped;
    }

    @NotNull
    public ColumnType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    @NotNull
    public String formatType(@NotNull DatabaseType databaseType) {
        return this.getType().getFormer().build(databaseType, this.getLength());
    }

    @NotNull
    public SQLValue toValue(@NotNull Object value) {
        return SQLValue.of(this, String.valueOf(value));
    }
}
