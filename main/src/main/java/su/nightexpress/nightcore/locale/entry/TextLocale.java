package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class TextLocale extends LangEntry<TextLocale.Value> {

    public TextLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::read, path, defaultValue);
    }

    @NotNull
    public static TextLocale create(@NotNull String path, @NotNull String string) {
        return new TextLocale(path, new Value(string));
    }

    @NotNull
    public static TextLocale create(@NotNull String path, @NotNull String... text) {
        return new TextLocale(path, new Value(String.join(TagWrappers.BR, text)));
    }

    /*@NotNull
    public TextLocale withDefault(@NotNull String locale, @NotNull String text) {
        this.withDefault(locale, new Value(text));
        return this;
    }*/

    @NotNull
    public String text() {
        return this.value.text();
    }

    public record Value(@NotNull String text) implements LangValue {

        @NotNull
        public static Value read(@NotNull FileConfig config, @NotNull String path) {
            return new Value(String.valueOf(config.getString(path)));
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path, this.text);
        }
    }
}
