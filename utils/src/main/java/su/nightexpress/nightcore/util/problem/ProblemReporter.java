package su.nightexpress.nightcore.util.problem;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

public interface ProblemReporter {

    @NonNull
    String getSubject();

    @NonNull
    String getPath();

    @NonNull
    List<Problem> getProblems();

    @NonNull
    String getReport();

    List<Problem.ChildReport> getChildren();

    boolean isEmpty();

    void print(@NonNull Logger logger);

    void report(@NonNull Problem problem);

    void report(@NonNull String problem);

    void children(@NonNull String description, @NonNull ProblemReporter reporter);

    int countProblems();

}
