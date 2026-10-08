package su.nightexpress.nightcore.integration.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItemProvider;
import su.nightexpress.nightcore.integration.item.codec.AdaptedItemCodec;
import su.nightexpress.nightcore.integration.item.dummy.DummyItem;
import su.nightexpress.nightcore.integration.item.registry.RegisteredItemProvider;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class ItemBridgeProvider {

    private final Map<String, RegisteredItemProvider<?>> providers = new HashMap<>();

    public <I extends AdaptedItem> void register(ItemProvider provider, ConfigCodec<I> codec) {
        this.providers.put(LowerCase.internal(provider.getId()), new RegisteredItemProvider<>(provider, codec));
    }

    public @Nullable RegisteredItemProvider<?> getRegisteredProvider(String id) {
        return this.providers.get(LowerCase.internal(id));
    }

    public Optional<RegisteredItemProvider<?>> findRegisteredProvider(String id) {
        return Optional.ofNullable(this.getRegisteredProvider(id));
    }

    public @Nullable ItemProvider getProvider(String id) {
        return this.findRegisteredProvider(id).map(RegisteredItemProvider::provider).orElse(null);
    }

    public Optional<ItemProvider> findProvider(String id) {
        return Optional.ofNullable(this.getProvider(id));
    }

    public Set<ItemProvider> getProviders() {
        return this.providers.values().stream().map(RegisteredItemProvider::provider).collect(Collectors.toSet());
    }

    public void unregisterProvider(String id) {
        this.providers.remove(LowerCase.internal(id));
    }

    public void unregisterAllProviders() {
        this.providers.clear();
    }

    public BukkitItemProvider getBukkitProvider() {
        return BukkitItemProvider.INSTANCE;
    }

    public boolean isBukkit(ItemProvider provider) {
        return provider.is(ItemProviderKeys.BUKKIT);
    }

    public boolean isBukkitOnly(ItemStack itemStack) {
        ItemProvider provider = this.getProvider(itemStack);
        return provider != null && this.isBukkit(provider);
    }

    public boolean isMixed(ItemStack itemStack) {
        return this.getProviders().stream()
            .filter(provider -> !this.isBukkit(provider))
            .filter(provider -> provider.isAvailable() && provider.isProducedBy(itemStack))
            .count() > 1;
    }

    public boolean isProducedBy(ItemStack itemStack, String providerId) {
        ItemProvider provider = this.getProvider(providerId);
        return provider != null && this.isProducedBy(itemStack, provider);
    }

    public boolean isProducedBy(ItemStack itemStack, ItemProvider provider) {
        return provider.isAvailable() && provider.isProducedBy(itemStack);
    }

    public AdaptedItem adaptOrDummy(ItemStack itemStack) {
        return this.findProvider(itemStack)
            .flatMap(provider -> provider.wrapItem(itemStack))
            .orElse(DummyItem.INSTANCE);
    }

    public Optional<AdaptedItem> adapt(ItemStack itemStack) {
        return this.findProvider(itemStack)
            .flatMap(provider -> provider.wrapItem(itemStack));
    }

    @Deprecated
    public Optional<AdaptedItem> read(FileConfig config, String path) {
        return Optional.of(AdaptedItemCodec.read(config, path));
    }

    public Optional<ItemProvider> findProvider(ItemStack itemStack) {
        BukkitItemProvider bukkitProvider = this.getBukkitProvider();

        for (RegisteredItemProvider<?> registered : this.providers.values()) {
            ItemProvider provider = registered.provider();
            if (provider == bukkitProvider) continue;
            if (provider.isAvailable() && provider.isProducedBy(itemStack)) {
                return Optional.of(provider);
            }
        }

        return Optional.of(bukkitProvider);
    }

    public @Nullable ItemProvider getProvider(ItemStack itemStack) {
        return this.findProvider(itemStack).orElse(null);
    }

    public boolean hasProvider(ItemStack itemStack) {
        return this.getProvider(itemStack) != null;
    }
}
