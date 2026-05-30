package su.nightexpress.nightcore.database.sql;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.database.DatabaseType;
import su.nightexpress.nightcore.database.sql.column.ColumnType;

@Deprecated
public class SQLColumn {

    private final String     name;
    private final String     nameEscaped;
    private final ColumnType type;
    private final int        length;

    public SQLColumn(@NonNull String name, @NonNull ColumnType type, int length) {
        this(name, "`" + name + "`", type, length);
    }

    public SQLColumn(@NonNull String name, @NonNull String nameEscaped, @NonNull ColumnType type, int length) {
        this.name = name;
        this.nameEscaped = nameEscaped;
        this.type = type;
        this.length = length;
    }

    @NonNull
    public static SQLColumn of(@NonNull String name, @NonNull ColumnType type) {
        return SQLColumn.of(name, type, -1);
    }

    @NonNull
    public static SQLColumn of(@NonNull String name, @NonNull ColumnType type, int length) {
        return new SQLColumn(name, type, length);
    }

    @NonNull
    @Deprecated
    public SQLColumn asLowerCase() {
        String name = "LOWER(" + this.getName() + ")";

        return new SQLColumn(name, name, this.getType(), this.getLength());
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getNameEscaped() {
        return this.nameEscaped;
    }

    @NonNull
    public String getNameLowercase() {
        return "LOWER(" + this.getName() + ")";
    }


    @NonNull
    public ColumnType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    @NonNull
    public String formatType(@NonNull DatabaseType databaseType) {
        return this.getType().getFormer().build(databaseType, this.getLength());
    }

    @NonNull
    public SQLValue toValue(@NonNull Object value) {
        return SQLValue.of(this, String.valueOf(value));
    }
}
