package su.nightexpress.nightcore.util.problem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProblemCollector implements ProblemReporter {

    private final String                subject;
    private final String                path;
    private final List<Problem>         problems;
    private final List<ProblemReporter> children;

    public ProblemCollector(@NotNull String subject, @NotNull String path) {
        this.subject = subject;
        this.path = path;
        this.problems = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    @Override
    public int countProblems() {
        return this.problems.size() + this.children.stream().mapToInt(ProblemReporter::countProblems).sum();
    }

    @Override
    public boolean isEmpty() {
        return this.countProblems() <= 0;
    }

    @Override
    public void report(@NotNull String problem) {
        this.report(() -> problem);
    }

    @Override
    public void report(@NotNull Problem problem) {
        this.problems.add(problem);
    }

    @Override
    @NotNull
    public ProblemReporter children(@NotNull String subject, @NotNull String path) {
        ProblemReporter reporter = new ProblemCollector(subject, path);
        this.children(reporter);
        return reporter;
    }

    @Override
    public void children(@NotNull ProblemReporter reporter) {
        this.children.add(reporter);
    }

    @Override
    public void print(@NotNull Logger logger) {
        if (!this.isEmpty()) {
            logger.warning(this.getReport());
        }
    }

    @Override
    @NotNull
    public String getReport() {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        builder.append("*".repeat(10)).append(" PROBLEM REPORTER ").append("*".repeat(10)).append("\n");
        builder.append("> Subject: ").append(this.subject).append("\n");
        builder.append("> Path: ").append(this.path).append("\n");
        builder.append("> Found Problems (").append(this.problems.size() + this.children.size()).append("):").append("\n");

        this.getLines().forEach(line -> builder.append(line).append("\n"));
        builder.append("*".repeat(35));

        return builder.toString();
    }

    @Override
    @NotNull
    public List<String> getLines() {
        List<String> list = new ArrayList<>();

        this.problems.forEach(problem -> list.add("[*] " + problem.description()));
        this.children.forEach(reporter -> {
            list.add("> '" + reporter.getSubject() + "':");
            list.addAll(reporter.getLines());
        });
        list.replaceAll(line -> "  " + line);

        return list;
    }

    @Override
    @NotNull
    public String getSubject() {
        return this.subject;
    }

    @Override
    @NotNull
    public String getPath() {
        return this.path;
    }

    @Override
    @NotNull
    public List<Problem> getProblems() {
        return this.problems;
    }
}
