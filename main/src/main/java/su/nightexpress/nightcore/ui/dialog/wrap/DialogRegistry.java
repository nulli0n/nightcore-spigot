package su.nightexpress.nightcore.ui.dialog.wrap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightPlugin;

public class DialogRegistry {

    private final NightPlugin                  plugin;
    private final Map<DialogKey<?>, Dialog<?>> byKey;

    public DialogRegistry(@NonNull NightPlugin plugin) {
        this.plugin = plugin;
        this.byKey = new HashMap<>();
    }

    public void clear() {
        this.byKey.clear();
    }

    public <T> void register(@NonNull DialogKey<T> key, @NonNull Supplier<Dialog<T>> supplier) {
        this.register(key, supplier.get());
    }

    public <T> void register(@NonNull DialogKey<T> key, @NonNull Dialog<T> dialog) {
        if (this.byKey.putIfAbsent(key, dialog) == null) {
            this.plugin.injectLang(dialog);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> boolean show(@NonNull Player player, @NonNull DialogKey<T> key, @NonNull T data, @Nullable Runnable callback) {
        Dialog<T> dialog = (Dialog<T>) this.byKey.get(key);

        if (dialog == null) {
            this.plugin.warn("Dialog '%s' not found or disabled.".formatted(key.id()));
            return false;
        }

        dialog.show(player, data, callback);
        return true;
    }
}
