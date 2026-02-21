package su.nightexpress.nightcore.locale.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;

public class BooleanLocale extends LangEntry<BooleanLocale.Value> {

    private static final ConfigType<Value> CONFIG_TYPE = ConfigType.of(Value::read, FileConfig::set);

    public BooleanLocale(@NonNull String path, @NonNull Value defaultValue) {
        super(CONFIG_TYPE, path, defaultValue);
    }

    @NonNull
    public static BooleanLocale create(@NonNull String path, @NonNull String onTrue, @NonNull String onFalse) {
        return new BooleanLocale(path, new Value(onTrue, onFalse));
    }

    @NonNull
    public String get(boolean b) {
        return b ? this.value.trueText() : this.value.falseText();
    }

    public record Value(@NonNull String trueText, @NonNull String falseText) implements LangValue {

        @NonNull
        public static Value read(@NonNull FileConfig config, @NonNull String path) {
            String onTrue = config.getString(path + ".onTrue", "true");
            String onFalse = config.getString(path + ".onFalse", "false");

            return new Value(onTrue, onFalse);
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            config.set(path + ".onTrue", this.trueText);
            config.set(path + ".onFalse", this.falseText);
        }
    }
}
