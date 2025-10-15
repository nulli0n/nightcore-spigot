package su.nightexpress.nightcore.util.problem;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public interface ProblemReporter {

    @NotNull String getSubject();

    @NotNull String getPath();

    @NotNull List<Problem> getProblems();

    @NotNull String getReport();

    List<Problem.ChildReport> getChildren();

    boolean isEmpty();

    void print(@NotNull Logger logger);

    void report(@NotNull Problem problem);

    void report(@NotNull String problem);

    void children(@NotNull String description, @NotNull ProblemReporter reporter);

    int countProblems();

}
