package su.nightexpress.nightcore.bridge.dialog;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class DialogKeys {

    public static final String NAMESPACE = "nightcore_dialogs";

    public static boolean isRightNamespace(@NotNull NamespacedKey key) {
        return isRightNamespace(key.getNamespace());
    }

    public static boolean isRightNamespace(@NotNull String namespace) {
        return namespace.equalsIgnoreCase(NAMESPACE);
    }
}
