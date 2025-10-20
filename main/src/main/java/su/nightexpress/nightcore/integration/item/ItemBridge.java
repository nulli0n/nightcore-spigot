package su.nightexpress.nightcore.integration.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @NotNull
    public static NightRegistry<String, ItemAdapter<?>> registry() {
        return Registries.ITEM_ADAPTER;
    }

    public static void register(@NotNull ItemAdapter<?> adapter) {
        registry().register(adapter.getName(), adapter);
    }

    @NotNull
    public static Set<ItemAdapter<?>> getAdapters() {
        return registry().values();
    }

    @NotNull
    public static VanillaItemAdapter getVanillaAdapter() {
        return VanillaItemAdapter.INSTANCE;
    }

    @Nullable
    public static ItemAdapter<?> getAdapter(@NotNull String name) {
        return registry().byKey(LowerCase.INTERNAL.apply(name));
    }

    @NotNull
    public static ItemAdapter<?> getAdapterOrVanilla(@NotNull String name) {
        return registry().lookup(LowerCase.INTERNAL.apply(name)).orElse(VanillaItemAdapter.INSTANCE);
    }

    @NotNull
    public static Optional<ItemAdapter<?>> adapter(@NotNull ItemStack itemStack) {
        return getAdapters().stream().filter(handler -> handler.canHandle(itemStack)).max(Comparator.comparingInt(ItemAdapter::getWeight));
    }

    @Nullable
    public static ItemAdapter<?> getAdapter(@NotNull ItemStack itemStack) {
        return adapter(itemStack).orElse(null);
    }

    @NotNull
    public static ItemAdapter<?> getAdapterOrVanilla(@NotNull ItemStack itemStack) {
        return adapter(itemStack).orElse(VanillaItemAdapter.INSTANCE);
    }

    public static boolean hasAdapter(@NotNull ItemStack itemStack) {
        return getAdapter(itemStack) != null;
    }
}
