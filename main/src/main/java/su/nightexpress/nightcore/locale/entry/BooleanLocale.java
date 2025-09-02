package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;

public class BooleanLocale extends LangEntry<BooleanLocale.Value> {

    public BooleanLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::read, path, defaultValue);
    }

    @NotNull
    public static BooleanLocale create(@NotNull String path, @NotNull String onTrue, @NotNull String onFalse) {
        return new BooleanLocale(path, new Value(onTrue, onFalse));
    }

    @NotNull
    public String get(boolean b) {
        return b ? this.value.trueText() : this.value.falseText();
    }

    public record Value(@NotNull String trueText, @NotNull String falseText) implements LangValue {

        @NotNull
        public static Value read(@NotNull FileConfig config, @NotNull String path) {
            String onTrue = config.getString(path + ".onTrue", "true");
            String onFalse = config.getString(path + ".onFalse", "false");

            return new Value(onTrue, onFalse);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path + ".onTrue", this.trueText);
            config.set(path + ".onFalse", this.falseText);
        }
    }
}
