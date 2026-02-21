package su.nightexpress.nightcore.locale.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class TextLocale extends LangEntry<TextLocale.Value> {

    private static final ConfigType<Value> CONFIG_TYPE = ConfigType.of(Value::read, FileConfig::set);

    public TextLocale(@NonNull String path, @NonNull Value defaultValue) {
        super(CONFIG_TYPE, path, defaultValue);
    }

    @NonNull
    public static TextLocale create(@NonNull String path, @NonNull String string) {
        return new TextLocale(path, new Value(string));
    }

    @NonNull
    public static TextLocale create(@NonNull String path, @NonNull String... text) {
        return new TextLocale(path, new Value(String.join(TagWrappers.BR, text)));
    }

    @NonNull
    public String text() {
        return this.value.text();
    }

    public record Value(@NonNull String text) implements LangValue {

        @NonNull
        public static Value read(@NonNull FileConfig config, @NonNull String path) {
            return new Value(String.valueOf(config.getString(path)));
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            config.set(path, this.text);
        }
    }
}
