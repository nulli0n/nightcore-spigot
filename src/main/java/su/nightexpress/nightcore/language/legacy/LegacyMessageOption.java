package su.nightexpress.nightcore.language.legacy;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@Deprecated
public enum LegacyMessageOption {

    PREFIX("prefix"),
    SOUND("sound"),
    TYPE("type"),
    PAPI("papi"),
    ;

    private final Pattern pattern;

    LegacyMessageOption(@NotNull String name) {
        this.pattern = Pattern.compile(name + ":(?:'|\")(.*?)(?:'|\")(?=>|\\s|$)");
    }

    @NotNull
    public Pattern getPattern() {
        return pattern;
    }
}
