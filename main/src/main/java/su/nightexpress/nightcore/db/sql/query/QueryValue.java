package su.nightexpress.nightcore.db.sql.query;

import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@Deprecated
public class QueryValue<T> {

    private final String              sqlPart;
    private final Function<T, String> statementPart;

    public QueryValue(String sqlPart, Function<T, String> statementPart) {
        this.sqlPart = sqlPart;
        this.statementPart = statementPart;
    }

    @NonNull
    public String getSQLPart() {
        return this.sqlPart;
    }

    @NonNull
    public String getStatementPart(T entity) {
        return this.statementPart.apply(entity);
    }

    @NonNull
    public Function<T, String> getStatementPart() {
        return this.statementPart;
    }
}
