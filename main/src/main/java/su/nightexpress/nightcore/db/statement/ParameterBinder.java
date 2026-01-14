package su.nightexpress.nightcore.db.statement;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterBinder<R> {

    ParameterBinder<Integer>   INT       = PreparedStatement::setInt;
    ParameterBinder<Long>      LONG      = PreparedStatement::setLong;
    ParameterBinder<Float>     FLOAT     = PreparedStatement::setFloat;
    ParameterBinder<Double>    DOUBLE    = PreparedStatement::setDouble;
    ParameterBinder<Boolean>   BOOLEAN   = PreparedStatement::setBoolean;
    ParameterBinder<String>    STRING    = PreparedStatement::setString;
    ParameterBinder<Object>    GENERIC   = PreparedStatement::setObject;

    void bind(@NotNull PreparedStatement statement, int index, @NotNull R data) throws SQLException;
}
