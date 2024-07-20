package su.nightexpress.nightcore.command.experimental.flag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentParser;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArgument;
import su.nightexpress.nightcore.command.experimental.builder.ContentFlagBuilder;

import java.util.function.Function;

public class ContentFlag<T> extends CommandFlag {

    private final ArgumentParser<T> parser;
    private final String            sample;

    public ContentFlag(@NotNull String name, @NotNull ArgumentParser<T> parser, @Nullable String sample, @Nullable String permission) {
        super(name, permission);
        this.parser = parser;
        this.sample = sample == null ? "" : sample;
    }

    @NotNull
    @Deprecated
    public static <T> ContentFlagBuilder<T> builder(@NotNull String name, @NotNull Function<String , T> parser) {
//        return new ContentFlagBuilder<>(name, parser);
        return builder(name, (string, context) -> parser.apply(string));
    }

    @NotNull
    public static <T> ContentFlagBuilder<T> builder(@NotNull String name, @NotNull ArgumentParser<T> parser) {
        return new ContentFlagBuilder<>(name, parser);
    }

    @Nullable
    public ParsedArgument<T> parse(@NotNull String str, @NotNull CommandContext context) {
        T result = this.parser.parse(str, context);
        return result == null ? null : new ParsedArgument<>(result);
    }

    @NotNull
    public String getSampled() {
        return this.getPrefixed() + DELIMITER + this.getSample();
    }

    @NotNull
    public ArgumentParser<T> getParser() {
        return parser;
    }

    @NotNull
    public String getSample() {
        return sample;
    }
}
