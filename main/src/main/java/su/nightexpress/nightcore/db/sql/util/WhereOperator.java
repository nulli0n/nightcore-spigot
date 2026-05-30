package su.nightexpress.nightcore.db.sql.util;

import org.jspecify.annotations.NonNull;

@Deprecated
public enum WhereOperator {

    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    SMALLER("<"),
    SMALLER_OR_EQUAL("<="),
    EQUAL("="),
    NOT_EQUAL("!=");

    private final String literal;

    WhereOperator(@NonNull String literal) {
        this.literal = literal;
    }

    @NonNull
    public String getLiteral() {
        return literal;
    }
}
