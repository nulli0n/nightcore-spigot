package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentParser;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.util.List;
import java.util.function.Function;

@Deprecated
public class ArgumentBuilder<T> {

    protected final String            name;
    protected final ArgumentParser<T> parser;

    protected boolean                            required;
    protected boolean                            complex;
    protected String                             localized;
    protected String                             permission;
    protected Function<TabContext, List<String>> samples;
    protected LangMessage                        failureMessage;

    @Deprecated
    public ArgumentBuilder(@NonNull String name, @NonNull Function<String, T> parser) {
        this(name, (string, context) -> parser.apply(string));
    }

    public ArgumentBuilder(@NonNull String name, @NonNull ArgumentParser<T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @NonNull
    public ArgumentBuilder<T> required() {
        return this.required(true);
    }

    @NonNull
    public ArgumentBuilder<T> required(boolean required) {
        this.required = required;
        return this;
    }

    @NonNull
    public ArgumentBuilder<T> complex() {
        this.complex = true;
        return this;
    }

    @NonNull
    public ArgumentBuilder<T> localized(@NonNull LangString localized) {
        return this.localized(localized.getString());
    }

    @NonNull
    public ArgumentBuilder<T> localized(@Nullable String localized) {
        this.localized = localized;
        return this;
    }

    @NonNull
    public ArgumentBuilder<T> permission(@NonNull UniPermission permission) {
        return this.permission(permission.getName());
    }

    @NonNull
    public ArgumentBuilder<T> permission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    @NonNull
    public ArgumentBuilder<T> customFailure(@NonNull LangText text) {
        return this.customFailure(text.getMessage());
    }

    @NonNull
    public ArgumentBuilder<T> customFailure(@Nullable LangMessage message) {
        this.failureMessage = message;
        return this;
    }

    @NonNull
    public ArgumentBuilder<T> withSamples(@NonNull Function<TabContext, List<String>> samples) {
        this.samples = samples;
        return this;
    }

    @NonNull
    public CommandArgument<T> build() {
        return new CommandArgument<>(this.name, this.parser, this.required, this.complex, this.localized, this.permission, this.failureMessage, this.samples);
    }
}
