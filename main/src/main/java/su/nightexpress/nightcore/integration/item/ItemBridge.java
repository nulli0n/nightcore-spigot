package su.nightexpress.nightcore.integration.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.Registries;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;
import su.nightexpress.nightcore.integration.item.adapter.impl.VanillaItemAdapter;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class ItemBridge {

    public static void unregisterAll() {
        registry().clear();
    }

    @NonNull
    public static NightRegistry<String, ItemAdapter<?>> registry() {
        return Registries.ITEM_ADAPTER;
    }

    public static void register(@NonNull ItemAdapter<?> adapter) {
        registry().register(adapter.getName(), adapter);
    }

    @NonNull
    public static Set<ItemAdapter<?>> getAdapters() {
        return registry().values();
    }

    @NonNull
    public static VanillaItemAdapter getVanillaAdapter() {
        return VanillaItemAdapter.INSTANCE;
    }

    @Nullable
    public static ItemAdapter<?> getAdapter(@NonNull String name) {
        return registry().byKey(LowerCase.INTERNAL.apply(name));
    }

    @NonNull
    public static ItemAdapter<?> getAdapterOrVanilla(@NonNull String name) {
        return registry().lookup(LowerCase.INTERNAL.apply(name)).orElse(VanillaItemAdapter.INSTANCE);
    }

    @NonNull
    public static Optional<ItemAdapter<?>> adapter(@NonNull ItemStack itemStack) {
        return getAdapters().stream().filter(handler -> handler.canHandle(itemStack)).max(Comparator.comparingInt(
            ItemAdapter::getWeight));
    }

    @Nullable
    public static ItemAdapter<?> getAdapter(@NonNull ItemStack itemStack) {
        return adapter(itemStack).orElse(null);
    }

    @NonNull
    public static ItemAdapter<?> getAdapterOrVanilla(@NonNull ItemStack itemStack) {
        return adapter(itemStack).orElse(VanillaItemAdapter.INSTANCE);
    }

    public static boolean hasAdapter(@NonNull ItemStack itemStack) {
        return getAdapter(itemStack) != null;
    }
}
