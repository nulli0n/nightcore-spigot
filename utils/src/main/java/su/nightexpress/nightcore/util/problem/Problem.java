package su.nightexpress.nightcore.util.problem;

import org.jetbrains.annotations.NotNull;

public interface Problem {

    @NotNull String description();

    record ChildReport(@NotNull String description, @NotNull ProblemReporter reporter) implements Problem {}
}
