package su.nightexpress.nightcore.database.sql.column;

import org.jetbrains.annotations.NotNull;

public class ColumnType {

    public static final ColumnType INTEGER = new ColumnType(ColumnFormer.INTEGER);
    public static final ColumnType DOUBLE = new ColumnType(ColumnFormer.DOUBLE);
    public static final ColumnType LONG = new ColumnType(ColumnFormer.LONG);
    public static final ColumnType BOOLEAN = new ColumnType(ColumnFormer.BOOLEAN);
    public static final ColumnType STRING = new ColumnType(ColumnFormer.STRING);

    private final ColumnFormer former;

    public ColumnType(@NotNull ColumnFormer former) {
        this.former = former;
    }

    @NotNull
    public ColumnFormer getFormer() {
        return former;
    }
}
