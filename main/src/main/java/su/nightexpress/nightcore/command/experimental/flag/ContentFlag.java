package su.nightexpress.nightcore.command.experimental.flag;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentParser;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArgument;
import su.nightexpress.nightcore.command.experimental.builder.ContentFlagBuilder;

import java.util.function.Function;

@Deprecated
public class ContentFlag<T> extends CommandFlag {

    private final ArgumentParser<T> parser;
    private final String            sample;

    public ContentFlag(@NonNull String name, @NonNull ArgumentParser<T> parser, @Nullable String sample,
                       @Nullable String permission) {
        super(name, permission);
        this.parser = parser;
        this.sample = sample == null ? "" : sample;
    }

    @NonNull
    @Deprecated
    public static <T> ContentFlagBuilder<T> builder(@NonNull String name, @NonNull Function<String, T> parser) {
        //        return new ContentFlagBuilder<>(name, parser);
        return builder(name, (string, context) -> parser.apply(string));
    }

    @NonNull
    public static <T> ContentFlagBuilder<T> builder(@NonNull String name, @NonNull ArgumentParser<T> parser) {
        return new ContentFlagBuilder<>(name, parser);
    }

    @Nullable
    public ParsedArgument<T> parse(@NonNull String str, @NonNull CommandContext context) {
        T result = this.parser.parse(str, context);
        return result == null ? null : new ParsedArgument<>(result);
    }

    @NonNull
    public String getSampled() {
        return this.getPrefixed() + DELIMITER + this.getSample();
    }

    @NonNull
    public ArgumentParser<T> getParser() {
        return parser;
    }

    @NonNull
    public String getSample() {
        return sample;
    }
}
