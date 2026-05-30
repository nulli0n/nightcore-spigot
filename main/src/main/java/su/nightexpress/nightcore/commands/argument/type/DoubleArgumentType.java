package su.nightexpress.nightcore.commands.argument.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DoubleArgumentType implements ArgumentType<Double>, SuggestionsProvider {

    private static final List<String> EXAMPLES = Arrays.asList("0", "1.2", ".5", "-1", "-.5", "-1234.56");

    private final double  minimum;
    private final double  maximum;
    private final boolean allowCompacts;

    public DoubleArgumentType(double minimum, double maximum, boolean allowCompacts) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.allowCompacts = allowCompacts;
    }

    @Override
    @NonNull
    public Double parse(@NonNull CommandContextBuilder contextBuilder,
                        @NonNull String string) throws CommandSyntaxException {
        Optional<Double> parse;
        if (this.allowCompacts) {
            parse = NumberUtil.parseDecimalCompact(string);
        }
        else {
            parse = NumberUtil.parseDouble(string);
        }

        double result = parse.orElseThrow(() -> CommandSyntaxException.custom(
            CoreLang.COMMAND_SYNTAX_NUMBER_NOT_DECIMAL));
        if (result < this.minimum) {
            throw CommandSyntaxException.dynamic(CoreLang.COMMAND_SYNTAX_NUMBER_BELOW_MIN, NumberUtil.format(
                this.minimum));
        }
        if (result > this.maximum) {
            throw CommandSyntaxException.dynamic(CoreLang.COMMAND_SYNTAX_NUMBER_ABOVE_MAX, NumberUtil.format(
                this.maximum));
        }
        return result;
    }

    @Override
    @NonNull
    public List<String> suggest(@NonNull ArgumentReader reader, @NonNull CommandContext context) {
        return EXAMPLES;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }
}
