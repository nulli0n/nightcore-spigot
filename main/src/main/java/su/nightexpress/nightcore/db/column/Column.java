package su.nightexpress.nightcore.db.column;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.config.DatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Column<R> {

    private final String              name;
    private final ColumnDataType      dataType;
    private final ColumnDataReader<R> dataReader;
    private final NullOption          nullOption;
    private final String              defaultValue;

    private final boolean primaryKey;
    private final boolean autoIncrement;

    public Column(@NotNull String name,
                  @NotNull ColumnDataType dataType,
                  @NotNull ColumnDataReader<R> dataReader,
                  @NotNull NullOption nullOption,
                  @Nullable String  defaultValue,
                  boolean primaryKey,
                  boolean autoIncrement) {
        this.name = name;
        this.dataType = dataType;
        this.dataReader = dataReader;
        this.nullOption = nullOption;
        this.defaultValue = defaultValue;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    @NotNull
    public static <T> Builder<T> builder(@NotNull String name, @NotNull ColumnDataType dataType, @NotNull ColumnDataReader<T> dataReader) {
        return new Builder<>(name, dataType, dataReader);
    }

    @NotNull
    public static Builder<Boolean> booleanType(@NotNull String name) {
        return builder(name, ColumnDataType.BOOLEAN, ColumnDataReader.BOOLEAN);
    }

    @NotNull
    public static Builder<Integer> intType(@NotNull String name) {
        return builder(name, ColumnDataType.INTEGER, ColumnDataReader.INTEGER);
    }

    @NotNull
    public static Builder<Long> longType(@NotNull String name) {
        return builder(name, ColumnDataType.LONG, ColumnDataReader.LONG);
    }

    @NotNull
    public static Builder<Float> floatType(@NotNull String name) {
        return builder(name, ColumnDataType.FLOAT, ColumnDataReader.FLOAT);
    }

    @NotNull
    public static Builder<Double> doubleType(@NotNull String name) {
        return builder(name, ColumnDataType.DOUBLE, ColumnDataReader.DOUBLE);
    }

    @NotNull
    public static Builder<String> stringType(@NotNull String name, int length) {
        return builder(name, ColumnDataType.string(length), ColumnDataReader.STRING);
    }

    @NotNull
    public static Builder<UUID> uuidType(@NotNull String name) {
        return builder(name, ColumnDataType.UUID, ColumnDataReader.UUID);
    }

    @NotNull
    public static Builder<String> tinyText(@NotNull String name) {
        return builder(name, ColumnDataType.TINY_TEXT, ColumnDataReader.STRING);
    }

    @NotNull
    public static Builder<String> mediumText(@NotNull String name) {
        return builder(name, ColumnDataType.MEDIUM_TEXT, ColumnDataReader.STRING);
    }

    @NotNull
    public static Builder<String> longText(@NotNull String name) {
        return builder(name, ColumnDataType.LONG_TEXT, ColumnDataReader.STRING);
    }

    @NotNull
    public static <R> Builder<R> json(@NotNull String name, @NotNull ColumnDataReader<R> dataReader) {
        return builder(name, ColumnDataType.JSON, dataReader);
    }



    @NotNull
    public String toSql(@NotNull DatabaseType type) {
        return this.name + " " + this.dataType.toSql(type, this.nullOption);
    }

    @NotNull
    private String toSql(@NotNull DatabaseType type, boolean withKey, boolean withDefault) {
        StringBuilder builder = new StringBuilder(this.toSql(type));

        if (withKey) {
            if (this.primaryKey) {
                builder.append(" PRIMARY KEY");
                if (this.autoIncrement) {
                    builder.append(type == DatabaseType.SQLITE ? " AUTOINCREMENT" : " AUTO_INCREMENT");
                }
            }
        }
        else if (withDefault) {
            if (this.defaultValue == null && this.nullOption == NullOption.NOT_NULL) {
                throw new IllegalStateException("NOT NULL columns must have a DEFAULT value");
            }
            builder.append(" DEFAULT").append(this.defaultValue);
        }

        return builder.toString();
    }

    @NotNull
    public String toSqlWithKey(@NotNull DatabaseType type) {
        return this.toSql(type, true, false);
    }

    @NotNull
    public String toSqlWithDefault(@NotNull DatabaseType type) {
        return this.toSql(type, false, true);
    }

    @NotNull
    public Optional<R> read(@NotNull ResultSet resultSet) throws SQLException {
        return this.dataReader.read(resultSet, this.name);
    }

    @NotNull
    public R readOrThrow(@NotNull ResultSet resultSet) throws SQLException {
        return this.read(resultSet).orElseThrow();
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getQuotedName() {
        return "`" + this.name + "`";
    }

    @NotNull
    public ColumnDataType getDataType() {
        return this.dataType;
    }

    @NotNull
    public ColumnDataReader<R> getDataReader() {
        return this.dataReader;
    }

    @NotNull
    public NullOption getNullOption() {
        return this.nullOption;
    }

    @Nullable
    public String getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    public boolean isAutoIncrement() {
        return this.autoIncrement;
    }

    public static class Builder<R> {

        private final String              name;
        private final ColumnDataType      dataType;
        private final ColumnDataReader<R> dataReader;

        private NullOption nullOption;
        private String defaultValue;
        private boolean    primaryKey;
        private boolean    autoIncrement;

        public Builder(@NotNull String name, @NotNull ColumnDataType dataType, @NotNull ColumnDataReader<R> dataReader) {
            this.name = name;
            this.dataType = dataType;
            this.dataReader = dataReader;
            this.nullOption = NullOption.NOT_NULL;
            this.primaryKey = false;
            this.autoIncrement = false;
        }

        @NotNull
        public Column<R> build() {
            return new Column<>(this.name, this.dataType, this.dataReader, this.nullOption, this.defaultValue, this.primaryKey, this.autoIncrement);
        }

        @NotNull
        public Builder<R> defaultValue(@NotNull String string) {
            this.defaultValue = "'" + string + "'";
            return this;
        }

        @NotNull
        public Builder<R> defaultValue(@NotNull Number number) {
            this.defaultValue = String.valueOf(number);
            return this;
        }

        @NotNull
        public Builder<R> defaultValue(@NotNull Boolean b) {
            this.defaultValue = b ? "1" : "0";
            return this;
        }

        @NotNull
        public Builder<R> primaryKey() {
            this.primaryKey = true;
            return this;
        }

        @NotNull
        public Builder<R> autoIncrement() {
            if (this.dataType != ColumnDataType.INTEGER) throw new IllegalStateException("Auto increment is for int only");

            this.autoIncrement = true;
            return this;
        }

        @NotNull
        public Builder<R> nullable() {
            this.nullOption = NullOption.NULLABLE;
            return this;
        }
    }
}
