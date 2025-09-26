package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;

import java.util.function.UnaryOperator;

public class ButtonLocale extends LangEntry<ButtonLocale.Value> {

    public ButtonLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::load, path, defaultValue);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label) {
        return create(path, label, null);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label, int width) {
        return create(path, label, null, width);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label, @Nullable String tooltip) {
        return create(path, label, tooltip, DialogDefaults.DEFAULT_BUTTON_WIDTH);
    }

    @NotNull
    public static ButtonLocale create(@NotNull String path, @NotNull String label, @Nullable String tooltip, int width) {
        return new ButtonLocale(path, new Value(label, tooltip, width));
    }

    @NotNull
    public ButtonLocale replace(@NotNull UnaryOperator<String> operator) {
        return create(this.path, operator.apply(this.value.label()), this.value.tooltip == null ? null : operator.apply(this.value.tooltip()), this.value.width());
    }

    public record Value(@NotNull String label, @Nullable String tooltip, int width) implements LangValue {

        @NotNull
        public static Value load(@NotNull FileConfig config, @NotNull String path) {
            String label = String.valueOf(config.getString(path + ".Label"));
            String tooltip = config.getString(path + ".Tooltip");
            int width = config.getInt(path + ".Width");

            return new Value(label, tooltip, width);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path + ".Label", this.label);
            config.set(path + ".Tooltip", this.tooltip);
            config.set(path + ".Width", this.width);
        }
    }
}
