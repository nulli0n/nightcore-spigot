package su.nightexpress.nightcore.db.sql.util;

import org.jetbrains.annotations.NotNull;

@Deprecated
public enum WhereOperator {

    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    SMALLER("<"),
    SMALLER_OR_EQUAL("<="),
    EQUAL("="),
    NOT_EQUAL("!=");

    private final String literal;

    WhereOperator(@NotNull String literal) {
        this.literal = literal;
    }

    @NotNull
    public String getLiteral() {
        return literal;
    }
}
