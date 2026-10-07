package su.nightexpress.nightcore.integration.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItemProvider;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class ItemBridgeProvider {

    private final Map<String, ItemProvider> providers = new HashMap<>();

    public void registerProvider(ItemProvider provider) {
        this.providers.put(LowerCase.internal(provider.getId()), provider);
    }

    public @Nullable ItemProvider getProvider(String id) {
        return this.providers.get(LowerCase.internal(id));
    }

    public Optional<ItemProvider> findProvider(String id) {
        return Optional.ofNullable(this.getProvider(id));
    }

    public Set<ItemProvider> getProviders() {
        return Set.copyOf(this.providers.values());
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

    public Optional<AdaptedItem> read(FileConfig config, String path) {
        String providerId = config.getString(path + ".Provider", ItemProviderKeys.BUKKIT);
        ItemProvider provider = this.getProvider(providerId);

        if (provider == null || !provider.isAvailable()) {
            return Optional.empty();
        }

        return provider.readItem(config, path);
    }

    public Optional<ItemProvider> findProvider(ItemStack itemStack) {
        BukkitItemProvider bukkitProvider = this.getBukkitProvider();

        for (ItemProvider provider : this.providers.values()) {
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
