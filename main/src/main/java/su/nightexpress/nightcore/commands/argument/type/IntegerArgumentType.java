package su.nightexpress.nightcore.commands.argument.type;

import org.jetbrains.annotations.NotNull;
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

public class IntegerArgumentType implements ArgumentType<Integer> {

    private static final List<String> EXAMPLES = Arrays.asList("0", "123", "-123");

    private final int minimum;
    private final int maximum;
    private final boolean allowCompacts;

    public IntegerArgumentType(int minimum, int maximum, boolean allowCompacts) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.allowCompacts = allowCompacts;
    }

    @Override
    @NotNull
    public Integer parse(@NotNull CommandContextBuilder contextBuilder, @NotNull String string) throws CommandSyntaxException {
        Optional<Integer> parse;
        if (this.allowCompacts) {
            parse = NumberUtil.parseIntCompact(string);
        }
        else {
            parse = NumberUtil.parseInteger(string);
        }

        int result = parse.orElseThrow(() -> CommandSyntaxException.custom(CoreLang.COMMAND_SYNTAX_NUMBER_NOT_INTEGER));
        if (result < this.minimum) {
            throw CommandSyntaxException.dynamic(CoreLang.COMMAND_SYNTAX_NUMBER_BELOW_MIN, NumberUtil.format(this.minimum));
        }
        if (result > this.maximum) {
            throw CommandSyntaxException.dynamic(CoreLang.COMMAND_SYNTAX_NUMBER_ABOVE_MAX, NumberUtil.format(this.maximum));
        }
        return result;
    }

    @Override
    @NotNull
    public List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context) {
        return EXAMPLES;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }
}
