package su.nightexpress.nightcore.database.sql.column;

import org.jspecify.annotations.NonNull;

@Deprecated
public class ColumnType {

    public static final ColumnType INTEGER = new ColumnType(ColumnFormer.INTEGER);
    public static final ColumnType DOUBLE  = new ColumnType(ColumnFormer.DOUBLE);
    public static final ColumnType LONG    = new ColumnType(ColumnFormer.LONG);
    public static final ColumnType BOOLEAN = new ColumnType(ColumnFormer.BOOLEAN);
    public static final ColumnType STRING  = new ColumnType(ColumnFormer.STRING);

    private final ColumnFormer former;

    public ColumnType(@NonNull ColumnFormer former) {
        this.former = former;
    }

    @NonNull
    public ColumnFormer getFormer() {
        return former;
    }
}
