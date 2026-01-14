package su.nightexpress.nightcore.db.statement.condition;

import org.jetbrains.annotations.NotNull;

public enum Operator {

    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    SMALLER("<"),
    SMALLER_OR_EQUAL("<="),
    EQUALS("="),
    EQUALS_IGNORE_CASE("="),
    NOT_EQUALS("!=");

    private final String literal;

    Operator(@NotNull String literal) {
        this.literal = literal;
    }

    @NotNull
    public String getLiteral() {
        return literal;
    }
}
