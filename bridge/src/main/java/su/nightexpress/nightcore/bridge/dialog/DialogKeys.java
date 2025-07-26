package su.nightexpress.nightcore.bridge.dialog;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class DialogKeys {

    public static final String NAMESPACE = "nightcore_dialogs";

    public static boolean isNamespace(@NotNull NamespacedKey key) {
        return isNamespace(key.getNamespace());
    }

    public static boolean isNamespace(@NotNull String namespace) {
        return namespace.equalsIgnoreCase(NAMESPACE);
    }
}
