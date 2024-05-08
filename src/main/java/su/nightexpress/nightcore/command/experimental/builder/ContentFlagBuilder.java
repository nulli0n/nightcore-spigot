package su.nightexpress.nightcore.command.experimental.builder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.flag.ContentFlag;

import java.util.function.Function;

public class ContentFlagBuilder<T> extends FlagBuilder<ContentFlag<T>, ContentFlagBuilder<T>> {

    private final Function<String, T> parser;

    private String sample;

    public ContentFlagBuilder(@NotNull String name, @NotNull Function<String , T> parser) {
        super(name);
        this.parser = parser;
        this.sample = "";
    }

    @Override
    @NotNull
    protected ContentFlagBuilder<T> getThis() {
        return this;
    }

    @NotNull
    public ContentFlagBuilder<T> sample(@NotNull String sample) {
        this.sample = sample;
        return getThis();
    }

    @Override
    public @NotNull ContentFlag<T> build() {
        return new ContentFlag<>(this.name, this.parser, this.sample, this.permission);
    }
}
