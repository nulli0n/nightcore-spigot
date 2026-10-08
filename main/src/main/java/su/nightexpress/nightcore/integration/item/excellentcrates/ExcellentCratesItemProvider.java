package su.nightexpress.nightcore.integration.item.excellentcrates;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.ItemProvider;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public class ExcellentCratesItemProvider implements ItemProvider {

    @Override
    public String getId() {
        return ItemProviderKeys.EXCELLENT_CRATES;
    }

    @Override
    public boolean isAvailable() {
        return Plugins.isInstalled("ExcellentCrates");
    }

    @Override
    public boolean isProducedBy(ItemStack itemStack) {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();
        if (api == null) {
            return false;
        }

        if (api.getCratesAPI().items().getCrateId(itemStack) != null) {
            return true;
        }

        KeysAPI keys = api.getKeysAPI().orElse(null);
        return keys != null && keys.getItemAPI().getKeyIdFromItem(itemStack) != null;
    }

    @Override
    public Optional<AdaptedItem> wrapItem(ItemStack itemStack) {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();
        if (api == null) {
            return Optional.empty();
        }

        int amount = itemStack.getAmount();

        Identifier crateId = api.getCratesAPI().items().getCrateId(itemStack);
        if (crateId != null) {
            ExcellentCratesItemContext context = new ExcellentCratesItemContext(ExcellentCratesItemType.CRATE, crateId,
                amount);
            return Optional.of(new ExcellentCratesItem(context));
        }

        KeysAPI keys = api.getKeysAPI().orElse(null);
        if (keys != null) {
            Identifier keyId = keys.getItemAPI().getKeyIdFromItem(itemStack);
            if (keyId != null) {
                ExcellentCratesItemContext context = new ExcellentCratesItemContext(ExcellentCratesItemType.KEY, keyId,
                    amount);
                return Optional.of(new ExcellentCratesItem(context));
            }
        }

        return Optional.empty();
    }
}
