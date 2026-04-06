package su.nightexpress.nightcore.db.column;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.config.DatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Column<R> {

    private final String              name;
    private final ColumnDataType      dataType;
    private final ColumnDataReader<R> dataReader;
    private final NullOption          nullOption;
    private final String              defaultValue;

    private final boolean primaryKey;
    private final boolean autoIncrement;
    private final boolean unique;

    public Column(@NonNull String name,
                  @NonNull ColumnDataType dataType,
                  @NonNull ColumnDataReader<R> dataReader,
                  @NonNull NullOption nullOption,
                  @Nullable String  defaultValue,
                  boolean primaryKey,
                  boolean autoIncrement,
                  boolean unique) {
        this.name = name;
        this.dataType = dataType;
        this.dataReader = dataReader;
        this.nullOption = nullOption;
        this.defaultValue = defaultValue;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
        this.unique = unique;
    }

    @NonNull
    public static <T> Builder<T> builder(@NonNull String name, @NonNull ColumnDataType dataType, @NonNull ColumnDataReader<T> dataReader) {
        return new Builder<>(name, dataType, dataReader);
    }

    @NonNull
    public static Builder<Boolean> booleanType(@NonNull String name) {
        return builder(name, ColumnDataType.BOOLEAN, ColumnDataReader.BOOLEAN);
    }

    @NonNull
    public static Builder<Integer> intType(@NonNull String name) {
        return builder(name, ColumnDataType.INTEGER, ColumnDataReader.INTEGER);
    }

    @NonNull
    public static Builder<Long> longType(@NonNull String name) {
        return builder(name, ColumnDataType.LONG, ColumnDataReader.LONG);
    }

    @NonNull
    public static Builder<Float> floatType(@NonNull String name) {
        return builder(name, ColumnDataType.FLOAT, ColumnDataReader.FLOAT);
    }

    @NonNull
    public static Builder<Double> doubleType(@NonNull String name) {
        return builder(name, ColumnDataType.DOUBLE, ColumnDataReader.DOUBLE);
    }

    @NonNull
    public static Builder<String> stringType(@NonNull String name, int length) {
        return builder(name, ColumnDataType.string(length), ColumnDataReader.STRING);
    }

    @NonNull
    public static Builder<UUID> uuidType(@NonNull String name) {
        return builder(name, ColumnDataType.UUID, ColumnDataReader.UUID);
    }

    @NonNull
    public static Builder<String> tinyText(@NonNull String name) {
        return builder(name, ColumnDataType.TINY_TEXT, ColumnDataReader.STRING);
    }

    @NonNull
    public static Builder<String> mediumText(@NonNull String name) {
        return builder(name, ColumnDataType.MEDIUM_TEXT, ColumnDataReader.STRING);
    }

    @NonNull
    public static Builder<String> longText(@NonNull String name) {
        return builder(name, ColumnDataType.LONG_TEXT, ColumnDataReader.STRING);
    }

    @NonNull
    public static <R> Builder<R> json(@NonNull String name, @NonNull ColumnDataReader<R> dataReader) {
        return builder(name, ColumnDataType.JSON, dataReader);
    }

    @NonNull
    public static <K, V> Builder<Map<K, V>> jsonMap(@NonNull String name, @NonNull Gson gson, @NonNull Class<K> keyType, @NonNull Class<V> valType) {
        return json(name, ColumnDataReader.jsonMap(gson, keyType, valType));
    }

    @NonNull
    public static <V> Builder<List<V>> jsonList(@NonNull String name, @NonNull Gson gson, @NonNull Class<V> valType) {
        return json(name, ColumnDataReader.jsonList(gson, valType));
    }

    @NonNull
    public static <V> Builder<Set<V>> jsonSet(@NonNull String name, @NonNull Gson gson, @NonNull Class<V> valType) {
        return json(name, ColumnDataReader.jsonSet(gson, valType));
    }



    @NonNull
    public String toSqlNameType(@NonNull DatabaseType type) {
        return this.name + " " + this.dataType.toSql(type, this.nullOption);
    }

    @NonNull
    public String toSqlWithKey(@NonNull DatabaseType type) {
        StringBuilder builder = new StringBuilder(this.toSqlNameType(type));

        if (this.unique && !this.primaryKey) {
            builder.append(" UNIQUE");
        }

        if (this.defaultValue != null) {
            builder.append(" DEFAULT ").append(this.defaultValue);
        }

        if (this.autoIncrement) {
            if (!this.primaryKey) {
                throw new IllegalStateException("Column '" + this.name + "' cannot be AUTO_INCREMENT unless it is also the PRIMARY KEY.");
            }

            // SQLite requires the primary key to be defined inline if it auto-increments
            if (type == DatabaseType.SQLITE) {
                builder.append(" PRIMARY KEY AUTOINCREMENT");
            }
            // MySQL only needs the auto-increment flag inline
            else if (type == DatabaseType.MYSQL) {
                builder.append(" AUTO_INCREMENT");
            }
        }

        return builder.toString();
    }

    @NonNull
    public String toSqlWithDefault(@NonNull DatabaseType type) {
        StringBuilder builder = new StringBuilder(this.toSqlNameType(type));

        if (this.unique && !this.primaryKey) {
            builder.append(" UNIQUE");
        }

        // Don't throw the missing default exception if the column is an auto-incrementing PK
        boolean requiresDefault = this.defaultValue == null
            && this.nullOption == NullOption.NOT_NULL
            && !this.autoIncrement;

        if (requiresDefault) {
            throw new IllegalStateException("NOT NULL columns ('%s') must have a DEFAULT value".formatted(this.name));
        }
        if (this.defaultValue != null) {
            builder.append(" DEFAULT ").append(this.defaultValue);
        }

        return builder.toString();
    }

    @NonNull
    public Optional<R> read(@NonNull ResultSet resultSet) throws SQLException {
        return this.dataReader.read(resultSet, this.name);
    }

    @NonNull
    public R readOrThrow(@NonNull ResultSet resultSet) throws SQLException {
        return this.read(resultSet).orElseThrow();
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getQuotedName() {
        return "`" + this.name + "`";
    }

    @NonNull
    public ColumnDataType getDataType() {
        return this.dataType;
    }

    @NonNull
    public ColumnDataReader<R> getDataReader() {
        return this.dataReader;
    }

    @NonNull
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

    public boolean isUnique() {
        return this.unique;
    }

    public static class Builder<R> {

        private final String              name;
        private final ColumnDataType      dataType;
        private final ColumnDataReader<R> dataReader;

        private NullOption nullOption;
        private String defaultValue;
        private boolean    primaryKey;
        private boolean    autoIncrement;
        private boolean unqiue;

        public Builder(@NonNull String name, @NonNull ColumnDataType dataType, @NonNull ColumnDataReader<R> dataReader) {
            this.name = name;
            this.dataType = dataType;
            this.dataReader = dataReader;
            this.nullOption = NullOption.NOT_NULL;
            this.primaryKey = false;
            this.autoIncrement = false;
            this.unqiue = false;
        }

        @NonNull
        public Column<R> build() {
            return new Column<>(this.name, this.dataType, this.dataReader, this.nullOption, this.defaultValue, this.primaryKey, this.autoIncrement, this.unqiue);
        }

        @NonNull
        public Builder<R> defaultValue(@NonNull String string) {
            this.defaultValue = "'" + string + "'";
            return this;
        }

        @NonNull
        public Builder<R> defaultValue(@NonNull Number number) {
            this.defaultValue = String.valueOf(number);
            return this;
        }

        @NonNull
        public Builder<R> defaultValue(@NonNull Boolean b) {
            this.defaultValue = b ? "1" : "0";
            return this;
        }

        @NonNull
        public Builder<R> primaryKey() {
            this.primaryKey = true;
            return this;
        }

        @NonNull
        public Builder<R> unique() {
            this.unqiue = true;
            return this;
        }

        @NonNull
        public Builder<R> autoIncrement() {
            if (this.dataType != ColumnDataType.INTEGER) throw new IllegalStateException("Auto increment is for int only");

            this.autoIncrement = true;
            return this;
        }

        @NonNull
        public Builder<R> nullable() {
            this.nullOption = NullOption.NULLABLE;
            return this;
        }
    }
}
