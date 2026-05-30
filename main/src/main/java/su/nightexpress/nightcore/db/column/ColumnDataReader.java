package su.nightexpress.nightcore.db.column;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@FunctionalInterface
public interface ColumnDataReader<R> {

    ColumnDataReader<Boolean>   BOOLEAN   = ResultSet::getBoolean;
    ColumnDataReader<Integer>   INTEGER   = ResultSet::getInt;
    ColumnDataReader<Long>      LONG      = ResultSet::getLong;
    ColumnDataReader<Float>     FLOAT     = ResultSet::getFloat;
    ColumnDataReader<Double>    DOUBLE    = ResultSet::getDouble;
    ColumnDataReader<String>    STRING    = ResultSet::getString;
    ColumnDataReader<Timestamp> TIMESTAMP = ResultSet::getTimestamp;

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

    @NonNull
    static <V> ColumnDataReader<V> jsonObject(@NonNull Gson gson, @NonNull Class<V> type) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            return gson.fromJson(jsonString, type);
        };
    }

    @NonNull
    static <K, V> ColumnDataReader<Map<K, V>> jsonMap(@NonNull Gson gson, @NonNull Class<K> keyType,
                                                      @NonNull Class<V> valType) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            Type mapType = TypeToken.getParameterized(Map.class, keyType, valType).getType();
            return gson.fromJson(jsonString, mapType);
        };
    }

    @NonNull
    static <V> ColumnDataReader<List<V>> jsonList(@NonNull Gson gson, @NonNull Class<V> valType) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            Type listType = TypeToken.getParameterized(List.class, valType).getType();
            return gson.fromJson(jsonString, listType);
        };
    }

    @NonNull
    static <V> ColumnDataReader<Set<V>> jsonSet(@NonNull Gson gson, @NonNull Class<V> valType) {
        return (resultSet, column) -> {
            String jsonString = resultSet.getString(column);

            if (resultSet.wasNull() || jsonString == null) {
                return null;
            }

            Type setType = TypeToken.getParameterized(Set.class, valType).getType();
            return gson.fromJson(jsonString, setType);
        };
    }

    @Nullable
    R readPrimitive(@NonNull ResultSet resultSet, @NonNull String column) throws SQLException;

    @NonNull
    default Optional<R> read(@NonNull ResultSet resultSet, @NonNull String column) throws SQLException {
        R primitive = this.readPrimitive(resultSet, column);
        return primitive == null || resultSet.wasNull() ? Optional.empty() : Optional.of(primitive);
    }
}
