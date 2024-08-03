package su.nightexpress.nightcore.database.sql.column;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ColumnType {

    public static final ColumnType INTEGER = new ColumnType(ColumnFormer.INTEGER, Integer::parseInt);
    public static final ColumnType DOUBLE = new ColumnType(ColumnFormer.DOUBLE, Double::parseDouble);
    public static final ColumnType LONG = new ColumnType(ColumnFormer.LONG, Long::parseLong);
    public static final ColumnType BOOLEAN = new ColumnType(ColumnFormer.BOOLEAN, Boolean::parseBoolean);
    public static final ColumnType STRING = new ColumnType(ColumnFormer.STRING, s -> s);

    private final ColumnFormer former;
    private final Function<String, ?> converter;

    public ColumnType(@NotNull ColumnFormer former, @NotNull Function<String, ?> converter) {
        this.former = former;
        this.converter = converter;
    }

    @NotNull
    public ColumnFormer getFormer() {
        return former;
    }

    public Function<String, ?> getConverter() {
        return converter;
    }
}
