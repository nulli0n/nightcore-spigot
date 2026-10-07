package su.nightexpress.nightcore.integration.item;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ItemBridge {

    private static @Nullable ItemBridgeProvider provider;

    private ItemBridge() {
    }

    public static void setProvider(ItemBridgeProvider provider) {
        ItemBridge.provider = provider;
    }

    public static boolean isAvailable() {
        return provider != null;
    }

    public static ItemBridgeProvider get() {
        if (provider == null) {
            throw new IllegalStateException("ItemBridgeProvider has not been set.");
        }
        return provider;
    }
}
