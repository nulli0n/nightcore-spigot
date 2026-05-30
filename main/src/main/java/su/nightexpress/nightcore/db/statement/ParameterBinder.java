package su.nightexpress.nightcore.db.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface ParameterBinder<R> {

    ParameterBinder<Integer>   INT       = PreparedStatement::setInt;
    ParameterBinder<Long>      LONG      = PreparedStatement::setLong;
    ParameterBinder<Float>     FLOAT     = PreparedStatement::setFloat;
    ParameterBinder<Double>    DOUBLE    = PreparedStatement::setDouble;
    ParameterBinder<Boolean>   BOOLEAN   = PreparedStatement::setBoolean;
    ParameterBinder<String>    STRING    = PreparedStatement::setString;
    ParameterBinder<UUID>      UUID      = (statement, index, data) -> statement.setString(index, data.toString());
    ParameterBinder<Timestamp> TIMESTAMP = PreparedStatement::setTimestamp;
    ParameterBinder<Object>    GENERIC   = PreparedStatement::setObject;

    void bind(@NonNull PreparedStatement statement, int index, @NonNull R data) throws SQLException;
}
