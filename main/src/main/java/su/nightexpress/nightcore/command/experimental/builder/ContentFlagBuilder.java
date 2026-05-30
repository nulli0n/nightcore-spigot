package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentParser;
import su.nightexpress.nightcore.command.experimental.flag.ContentFlag;

@Deprecated
public class ContentFlagBuilder<T> extends FlagBuilder<ContentFlag<T>, ContentFlagBuilder<T>> {

    private final ArgumentParser<T> parser;

    private String sample;

    //    public ContentFlagBuilder(@NonNull String name, @NonNull Function<String , T> parser) {
    //        super(name);
    //        this.parser = parser;
    //        this.sample = "";
    //    }

    public ContentFlagBuilder(@NonNull String name, @NonNull ArgumentParser<T> parser) {
        super(name);
        this.parser = parser;
        this.sample = "";
    }

    @Override
    @NonNull
    protected ContentFlagBuilder<T> getThis() {
        return this;
    }

    @NonNull
    public ContentFlagBuilder<T> sample(@NonNull String sample) {
        this.sample = sample;
        return getThis();
    }

    @Override
    public @NonNull ContentFlag<T> build() {
        return new ContentFlag<>(this.name, this.parser, this.sample, this.permission);
    }
}
