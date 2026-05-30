package su.nightexpress.nightcore.bridge.dialog;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

public class DialogKeys {

    public static final String NAMESPACE = "nightcore_dialogs";

    public static boolean isRightNamespace(@NonNull NamespacedKey key) {
        return isRightNamespace(key.getNamespace());
    }

    public static boolean isRightNamespace(@NonNull String namespace) {
        return namespace.equalsIgnoreCase(NAMESPACE);
    }
}
