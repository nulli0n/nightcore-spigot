package su.nightexpress.nightcore.util.format.adaptive;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class AdaptiveFormatter<T> {

    private static class ConditionRule<T> {

        final String              openTag;
        final Pattern             pattern;
        final DisplayCondition<T> condition;

        ConditionRule(@NonNull String tag, @NonNull DisplayCondition<T> condition) {
            this.openTag = "<if_" + tag + ">";

            this.pattern = Pattern.compile("<if_" + tag + ">(.*?)</if_" + tag + ">");
            this.condition = condition;
        }
    }

    private static class VariableRule<T> {

        final String              placeholder;
        final VariableReplacer<T> replacer;

        VariableRule(@NonNull String tag, @NonNull VariableReplacer<T> replacer) {
            this.placeholder = "{" + tag + "}";
            this.replacer = replacer;
        }
    }

    private final List<ConditionRule<T>> conditions = new ArrayList<>();
    private final List<VariableRule<T>>  variables  = new ArrayList<>();

    public void registerCondition(@NonNull String tag, @NonNull DisplayCondition<T> condition) {
        this.conditions.add(new ConditionRule<>(tag, condition));
    }

    public void registerVariable(@NonNull String tag, @NonNull VariableReplacer<T> replacer) {
        this.variables.add(new VariableRule<>(tag, replacer));
    }


    public @NonNull String formatLine(@NonNull String line, @NonNull T source, @NonNull Player player) {
        if (line.isEmpty()) return line;

        if (line.contains("<if_")) {
            line = this.validateConditions(line, source, player);
        }

        if (line.contains("{")) {
            line = this.replaceVariables(line, source, player);
        }

        return line.trim();
    }

    private @NonNull String validateConditions(@NonNull String line, @NonNull T source, @NonNull Player player) {
        for (ConditionRule<T> rule : this.conditions) {
            if (!line.contains(rule.openTag)) continue;

            boolean isConditionMet = rule.condition.check(source, player);
            Matcher matcher = rule.pattern.matcher(line);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                if (isConditionMet) {
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(1)));
                }
                else {
                    matcher.appendReplacement(sb, "");
                }
            }
            matcher.appendTail(sb);
            line = sb.toString();
        }

        return line;
    }

    private @NonNull String replaceVariables(@NonNull String line, @NonNull T source, @NonNull Player player) {
        for (VariableRule<T> rule : variables) {
            if (line.contains(rule.placeholder)) {
                String replacementValue = rule.replacer.replace(source, player);
                line = line.replace(rule.placeholder, replacementValue);
            }
        }

        return line;
    }
}