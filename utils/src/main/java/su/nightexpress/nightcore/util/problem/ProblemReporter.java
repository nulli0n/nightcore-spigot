package su.nightexpress.nightcore.util.problem;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public interface ProblemReporter {

    @NotNull String getSubject();

    @NotNull String getPath();

    @NotNull List<Problem> getProblems();

    @NotNull String getReport();

    @NotNull List<String> getLines();

    boolean isEmpty();

    void print(@NotNull Logger logger);

    void report(@NotNull Problem problem);

    void report(@NotNull String problem);

    void children(@NotNull ProblemReporter reporter);

    @NotNull ProblemReporter children(@NotNull String subject, @NotNull String path);

    int countProblems();

}
