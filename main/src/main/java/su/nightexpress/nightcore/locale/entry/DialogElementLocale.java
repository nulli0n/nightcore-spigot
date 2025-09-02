package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class DialogElementLocale extends LangEntry<DialogElementLocale.Value> {

    public DialogElementLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::read, path, defaultValue);
    }

    @NotNull
    public static DialogElementLocale create(@NotNull String path, @NotNull String... contents) {
        return create(path, 200, contents);
    }

    @NotNull
    public static DialogElementLocale create(@NotNull String path, int width, @NotNull String... contents) {
        return new DialogElementLocale(path, new Value(String.join(TagWrappers.BR, contents), width));
    }

    @NotNull
    public String contents() {
        return this.value.contents();
    }

    public int width() {
        return this.value.width();
    }

    public record Value(@NotNull String contents, int width) implements LangValue {

        @NotNull
        public static Value read(@NotNull FileConfig config, @NotNull String path) {
            String contents = String.valueOf(config.getString(path + ".Contents"));
            int width = config.getInt(path + ".Width");

            return new Value(contents, width);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path + ".Contents", this.contents);
            config.set(path + ".Width", this.width);
        }
    }
}
