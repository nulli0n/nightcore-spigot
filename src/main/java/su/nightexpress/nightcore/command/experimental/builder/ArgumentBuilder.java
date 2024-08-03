package su.nightexpress.nightcore.command.experimental.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.TabContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentParser;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.util.List;
import java.util.function.Function;

public class ArgumentBuilder<T> {

    protected final String         name;
    protected final ArgumentParser<T> parser;

    protected boolean                            required;
    protected boolean complex;
    protected String                             localized;
    protected String                             permission;
    protected Function<TabContext, List<String>> samples;
    protected LangMessage                        failureMessage;

    @Deprecated
    public ArgumentBuilder(@NotNull String name, @NotNull Function<String, T> parser) {
        this(name, (string, context) -> parser.apply(string));
    }

    public ArgumentBuilder(@NotNull String name, @NotNull ArgumentParser<T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @NotNull
    public ArgumentBuilder<T> required() {
        return this.required(true);
    }

    @NotNull
    public ArgumentBuilder<T> required(boolean required) {
        this.required = required;
        return this;
    }

    @NotNull
    public ArgumentBuilder<T> complex() {
        this.complex = true;
        return this;
    }

    @NotNull
    public ArgumentBuilder<T> localized(@NotNull LangString localized) {
        return this.localized(localized.getString());
    }

    @NotNull
    public ArgumentBuilder<T> localized(@Nullable String localized) {
        this.localized = localized;
        return this;
    }

    @NotNull
    public ArgumentBuilder<T> permission(@NotNull UniPermission permission) {
        return this.permission(permission.getName());
    }

    @NotNull
    public ArgumentBuilder<T> permission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    @NotNull
    public ArgumentBuilder<T> customFailure(@NotNull LangText text) {
        return this.customFailure(text.getMessage());
    }

    @NotNull
    public ArgumentBuilder<T> customFailure(@Nullable LangMessage message) {
        this.failureMessage = message;
        return this;
    }

    @NotNull
    public ArgumentBuilder<T> withSamples(@NotNull Function<TabContext, List<String>> samples) {
        this.samples = samples;
        return this;
    }

    @NotNull
    public CommandArgument<T> build() {
        return new CommandArgument<>(this.name, this.parser, this.required, this.complex, this.localized, this.permission, this.failureMessage, this.samples);
    }
}
