package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class DialogElementLocale extends LangEntry<DialogElementLocale.Value> {

    public DialogElementLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::read, path, defaultValue);
    }

    @NotNull
    public static DialogElementLocale create(@NotNull String path, @NotNull String... contents) {
        return create(path, DialogDefaults.DEFAULT_PLAIN_BODY_WIDTH, contents);
    }

    @NotNull
    public static DialogElementLocale create(@NotNull String path, int width, @NotNull String... contents) {
        return new DialogElementLocale(path, new Value(String.join(TagWrappers.BR, contents), width));
    }

    @NotNull
    public DialogElementLocale replace(@NotNull UnaryOperator<String> operator) {
        return create(this.path, this.width(), operator.apply(this.contents()));
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
            String contents;

            List<String> list = config.getStringList(path + ".Contents");
            if (!list.isEmpty()) {
                contents = String.join(TagWrappers.BR, list);
            }
            else {
                contents = String.valueOf(config.getString(path + ".Contents"));
            }

            int width = config.getInt(path + ".Width");

            return new Value(contents, width);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            List<String> list = Arrays.asList(ParserUtils.breakDownLineSplitters(this.contents));
            config.set(path + ".Contents", list.size() != 1 ? list : list.getFirst());
            config.set(path + ".Width", this.width);
        }
    }
}
