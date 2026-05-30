package su.nightexpress.nightcore.util.problem;

import org.jspecify.annotations.NonNull;

public interface Problem {

    @NonNull
    String description();

    record ChildReport(@NonNull String description, @NonNull ProblemReporter reporter) implements Problem {
    }
}
