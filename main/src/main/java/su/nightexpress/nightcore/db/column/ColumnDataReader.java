package su.nightexpress.nightcore.db.column;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface ColumnDataReader<R> {

    ColumnDataReader<Boolean> BOOLEAN = ResultSet::getBoolean;
    ColumnDataReader<Integer> INTEGER = ResultSet::getInt;
    ColumnDataReader<Long>    LONG    = ResultSet::getLong;
    ColumnDataReader<Float>   FLOAT   = ResultSet::getFloat;
    ColumnDataReader<Double>  DOUBLE  = ResultSet::getDouble;
    ColumnDataReader<String>  STRING  = ResultSet::getString;

    ColumnDataReader<UUID> UUID = (resultSet, column) -> {
        String raw = resultSet.getString(column);
        if (resultSet.wasNull()) return null;

        try {
            return java.util.UUID.fromString(raw);
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    };

    @NotNull
    static <V> ColumnDataReader<V> jsonObject(@NotNull Gson gson, @NotNull Class<V> type) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            return gson.fromJson(jsonString, type);
        };
    }

    @NotNull
    static <K, V> ColumnDataReader<Map<K, V>> jsonMap(@NotNull Gson gson, @NotNull Class<K> keyType, @NotNull Class<V> valType) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            Type mapType = TypeToken.getParameterized(Map.class, keyType, valType).getType();
            return gson.fromJson(jsonString, mapType);
        };
    }

    @NotNull
    static <V> ColumnDataReader<List<V>> jsonList(@NotNull Gson gson, @NotNull Class<V> valType) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            Type mapType = TypeToken.getParameterized(List.class, valType).getType();
            return gson.fromJson(jsonString, mapType);
        };
    }

    @Nullable R readPrimitive(@NotNull ResultSet resultSet, @NotNull String column) throws SQLException;

    @NotNull default Optional<R> read(@NotNull ResultSet resultSet, @NotNull String column) throws SQLException {
        R primitive = this.readPrimitive(resultSet, column);
        return primitive == null || resultSet.wasNull() ? Optional.empty() : Optional.of(primitive);
    }
}
