package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.builder.ArgumentBuilder;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.List;
import java.util.function.Function;

public class CommandArgument<T> {

    private final String                             name;
    private final ArgumentParser<T>                  parser;
    private final boolean                            required;
    private final boolean                            complex;
    private final String                             localized;
    private final String                             permission;
    private final Function<TabContext, List<String>> samples;
    private final LangMessage                        failureMessage;

//    @Deprecated
//    public CommandArgument(@NotNull String name,
//                           @NotNull Function<String, T> parser,
//                           boolean required,
//                           boolean complex,
//                           @Nullable String localized,
//                           @Nullable String permission,
//                           @Nullable LangMessage failureMessage,
//                           @Nullable Function<TabContext, List<String>> samples) {
//        this(name, ((string, context) -> parser.apply(string)), required, complex, localized, permission, failureMessage, samples);
//    }

    public CommandArgument(@NotNull String name,
                           @NotNull ArgumentParser<T> parser,
                           boolean required,
                           boolean complex,
                           @Nullable String localized,
                           @Nullable String permission,
                           @Nullable LangMessage failureMessage,
                           @Nullable Function<TabContext, List<String>> samples) {
        this.name = name.toLowerCase();
        this.parser = parser;
        this.required = required;
        this.complex = complex;
        this.samples = samples;
        this.localized = this.getLocalized(localized);
        this.permission = permission;
        this.failureMessage = failureMessage;
    }

    @NotNull
    @Deprecated
    public static <T> ArgumentBuilder<T> builder(@NotNull String name, @NotNull Function<String, T> parser) {
//        return new ArgumentBuilder<>(name, parser);
        return builder(name, (string, context) -> parser.apply(string));
    }

    @NotNull
    public static <T> ArgumentBuilder<T> builder(@NotNull String name, @NotNull ArgumentParser<T> parser) {
        return new ArgumentBuilder<>(name, parser);
    }

    @Nullable
    public ParsedArgument<T> parse(@NotNull String str, @NotNull CommandContext context) {
        T result = this.parser.parse(str, context);
        return result == null ? null : new ParsedArgument<>(result);
    }

    public boolean hasPermission(@NotNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NotNull
    public List<String> getSamples(@NotNull TabContext context) {
        return this.samples == null ? Lists.newList(NightMessage.asLegacy(this.getLocalized())) : this.samples.apply(context);
    }

    @NotNull
    private String getLocalized(@Nullable String localized) {
        if (localized == null) {
            localized = this.name;
        }

        String format = (this.isRequired() ? CoreLang.COMMAND_ARGUMENT_FORMAT_REQUIRED : CoreLang.COMMAND_ARGUMENT_FORMAT_OPTIONAL).getString();

        return format.replace(Placeholders.GENERIC_NAME, localized);
    }

    @NotNull
    public LangMessage getFailureMessage() {
        return this.failureMessage == null ? CoreLang.ERROR_COMMAND_PARSE_ARGUMENT.getMessage() : this.failureMessage;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ArgumentParser<T> getParser() {
        return parser;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isComplex() {
        return complex;
    }

    @NotNull
    public String getLocalized() {
        return this.localized == null ? this.name : this.localized;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @NotNull
    public Function<TabContext, List<String>> getSamples() {
        return samples;
    }
}
