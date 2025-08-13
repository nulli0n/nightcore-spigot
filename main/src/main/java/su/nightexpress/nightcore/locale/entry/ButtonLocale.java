package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;

public class ButtonLocale extends LangEntry<ButtonLocale.Value> {

    public ButtonLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::load, path, defaultValue);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label) {
        return create(path, label, null);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label, @Nullable String tooltip) {
        return new ButtonLocale(path, new Value(label, tooltip));
    }

    public record Value(@NotNull String label, @Nullable String tooltip) implements LangValue {

        @NotNull
        public static Value load(@NotNull FileConfig config, @NotNull String path) {
            String label = String.valueOf(config.getString(path + ".Label"));
            String tooltip = config.getString(path + ".Tooltip");

            return new Value(label, tooltip);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path + ".Label", this.label);
            config.set(path + ".Tooltip", this.tooltip);
        }
    }
}
