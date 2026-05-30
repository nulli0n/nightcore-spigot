package su.nightexpress.nightcore.db.statement.condition;

import org.jspecify.annotations.NonNull;

public enum Operator {

    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    SMALLER("<"),
    SMALLER_OR_EQUAL("<="),
    EQUALS("="),
    EQUALS_IGNORE_CASE("="),
    NOT_EQUALS("!=");

    private final String literal;

    Operator(@NonNull String literal) {
        this.literal = literal;
    }

    @NonNull
    public String getLiteral() {
        return literal;
    }
}
