package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.builder.ArgumentBuilder;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.List;
import java.util.function.Function;

@Deprecated
public class CommandArgument<T> {

    private final String                             name;
    private final ArgumentParser<T>                  parser;
    private final boolean                            required;
    private final boolean                            complex;
    private final String                             localized;
    private final String                             permission;
    private final Function<TabContext, List<String>> samples;
    private final LangMessage                        failureMessage;

    public CommandArgument(@NonNull String name,
                           @NonNull ArgumentParser<T> parser,
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

    @NonNull
    @Deprecated
    public static <T> ArgumentBuilder<T> builder(@NonNull String name, @NonNull Function<String, T> parser) {
        return builder(name, (string, context) -> parser.apply(string));
    }

    @NonNull
    public static <T> ArgumentBuilder<T> builder(@NonNull String name, @NonNull ArgumentParser<T> parser) {
        return new ArgumentBuilder<>(name, parser);
    }

    @Nullable
    public ParsedArgument<T> parse(@NonNull String str, @NonNull CommandContext context) {
        T result = this.parser.parse(str, context);
        return result == null ? null : new ParsedArgument<>(result);
    }

    public boolean hasPermission(@NonNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NonNull
    public List<String> getSamples(@NonNull TabContext context) {
        return this.samples == null ? Lists.newList(NightMessage.stripTags(this.getLocalized())) : this.samples.apply(
            context);
    }

    @NonNull
    private String getLocalized(@Nullable String localized) {
        if (localized == null) {
            localized = this.name;
        }

        String format = (this
            .isRequired() ? CoreLang.COMMAND_ARGUMENT_FORMAT_REQUIRED : CoreLang.COMMAND_ARGUMENT_FORMAT_OPTIONAL)
                .getString();

        return format.replace(Placeholders.GENERIC_NAME, localized);
    }

    @NonNull
    @Deprecated
    public LangMessage getFailureMessage() {
        return this.failureMessage == null ? CoreLang.ERROR_COMMAND_PARSE_ARGUMENT.getMessage() : this.failureMessage;
    }

    @NonNull
    public LangMessage getFailureMessage(@NonNull NightCorePlugin plugin) {
        return this.failureMessage == null ? CoreLang.ERROR_COMMAND_PARSE_ARGUMENT.getMessage(
            plugin) : this.failureMessage.setPrefix(plugin);
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public ArgumentParser<T> getParser() {
        return this.parser;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isComplex() {
        return this.complex;
    }

    @NonNull
    public String getLocalized() {
        return this.localized == null ? this.name : this.localized;
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    @NonNull
    public Function<TabContext, List<String>> getSamples() {
        return this.samples;
    }
}
