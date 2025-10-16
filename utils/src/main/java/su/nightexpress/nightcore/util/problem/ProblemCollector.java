package su.nightexpress.nightcore.util.problem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProblemCollector implements ProblemReporter {

    private static final boolean USE_COLORS = true;

    // ANSI color codes
    private static final String RESET          = "\u001B[0m";
    private static final String MAGENTA_BRIGHT = "\u001B[95m";
    private static final String YELLOW_BRIGHT  = "\u001B[93m";
    private static final String GRAY           = "\u001B[90m";
    private static final String WHITE_DIM      = "\u001B[37m";

    private final String                    subject;
    private final String                    path;
    private final List<Problem>             problems;
    private final List<Problem.ChildReport> children;

    public ProblemCollector(@NotNull String subject, @NotNull String path) {
        this.subject = subject;
        this.path = path;
        this.problems = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    @Override
    public int countProblems() {
        return this.problems.size() + this.children.stream().mapToInt(childReport -> childReport.reporter().countProblems()).sum();
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
    public void children(@NotNull String description, @NotNull ProblemReporter reporter) {
        this.children.add(new Problem.ChildReport(description, reporter));
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
        builder.append(color(YELLOW_BRIGHT, "*".repeat(10) + " PROBLEM REPORT " + "*".repeat(10))).append("\n");
        builder.append(color(WHITE_DIM, "> Subject: ")).append(this.subject).append("\n");
        builder.append(color(WHITE_DIM, "> Path: ")).append(this.path).append("\n");
        builder.append(color(WHITE_DIM, "> Found Problems (" + this.countProblems() + "):")).append("\n");

        appendTree(this, builder, "");
        builder.append(color(YELLOW_BRIGHT, "*".repeat(35))).append("\n");

        return builder.toString();
    }

    private static void appendTree(@NotNull ProblemReporter root, @NotNull StringBuilder builder, @NotNull String prefix) {
        List<String> entries = new ArrayList<>();
        List<Problem> problems = root.getProblems();
        List<Problem.ChildReport> children = root.getChildren();

        // Add local problems
        for (Problem problem : root.getProblems()) {
            entries.add(problem.description());
        }

        // Add child collectors as nested entries
        for (Problem.ChildReport child : root.getChildren()) {
            entries.add(child.description());
        }

        for (int index = 0; index < entries.size(); index++) {
            boolean last = (index == entries.size() - 1);
            String connector = last ? "└── " : "├── ";
            //builder.append(prefix).append(connector).append(entries.get(index)).append("\n");

            builder.append(color(GRAY, prefix + connector));
            builder.append(color(index < problems.size() ? YELLOW_BRIGHT : MAGENTA_BRIGHT, entries.get(index)));
            builder.append(RESET).append("\n");

            // If this is a child collector, recurse
            if (index >= problems.size()) {
                ProblemReporter child = children.get(index - problems.size()).reporter();
                String newPrefix = prefix + (last ? "    " : "│   ");
                appendTree(child, builder, newPrefix);
            }
        }
    }

    @NotNull
    private static String color(@NotNull String code, @NotNull String text) {
        return USE_COLORS ? code + text + RESET : text;
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

    @NotNull
    public List<Problem.ChildReport> getChildren() {
        return this.children;
    }
}
