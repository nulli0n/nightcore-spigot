package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.DialogDefaults;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

import java.util.function.UnaryOperator;

public class ButtonLocale extends LangEntry<ButtonLocale.Value> {

    private static final ConfigType<Value> CONFIG_TYPE = ConfigType.of(Value::load, FileConfig::set);

    public ButtonLocale(@NonNull String path, @NonNull Value defaultValue) {
        super(CONFIG_TYPE, path, defaultValue);
    }

    @NonNull
    public static ButtonLocale create(@NonNull String path, @NonNull String label) {
        return create(path, label, null);
    }

    @NonNull
    public static ButtonLocale create(@NonNull String path, @NonNull String label, int width) {
        return create(path, label, null, width);
    }

    @NonNull
    public static ButtonLocale create(@NonNull String path, @NonNull String label, @Nullable String tooltip) {
        return create(path, label, tooltip, DialogDefaults.DEFAULT_BUTTON_WIDTH);
    }

    @NonNull
    public static ButtonLocale create(@NonNull String path, @NonNull String label, @Nullable String tooltip, int width) {
        return new ButtonLocale(path, new Value(label, tooltip, width));
    }

    @NonNull
    public ButtonLocale replace(@NonNull UnaryOperator<String> operator) {
        return create(this.path, operator.apply(this.value.label()), this.value.tooltip == null ? null : operator.apply(this.value.tooltip()), this.value.width());
    }

    @NonNull
    public ButtonLocale replace(@NonNull PlaceholderContext context) {
        return this.replace(context::apply);
    }

    public record Value(@NonNull String label, @Nullable String tooltip, int width) implements LangValue {

        @NonNull
        public static Value load(@NonNull FileConfig config, @NonNull String path) {
            String label = String.valueOf(config.getString(path + ".Label"));
            String tooltip = config.getString(path + ".Tooltip");
            int width = config.getInt(path + ".Width");

            return new Value(label, tooltip, width);
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            config.set(path + ".Label", this.label);
            config.set(path + ".Tooltip", this.tooltip);
            config.set(path + ".Width", this.width);
        }
    }
}
