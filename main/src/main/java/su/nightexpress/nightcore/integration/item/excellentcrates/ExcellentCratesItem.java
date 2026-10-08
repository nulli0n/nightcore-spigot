package su.nightexpress.nightcore.integration.item.excellentcrates;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.nightcore.integration.item.BaseProviderDataItem;
import su.nightexpress.nightcore.integration.item.ItemProviderKeys;

@NullMarked
public class ExcellentCratesItem extends BaseProviderDataItem<ExcellentCratesItemContext> {

    public ExcellentCratesItem(ExcellentCratesItemContext context) {
        super(ItemProviderKeys.EXCELLENT_CRATES, context);
    }

    @Override
    public int getAmount() {
        return this.data.amount();
    }

    private boolean isCrate(ItemStack itemStack) {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();

        return api != null && api.getCratesAPI().items().isCrateItem(itemStack);
    }

    private boolean isKey(ItemStack itemStack) {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();

        return api != null && api.getKeysAPI().map(keys -> keys.getItemAPI().isKeyItem(itemStack)).orElse(false);
    }

    @Override
    public boolean isChildren(ItemStack itemStack) {
        return isCrate(itemStack) || isKey(itemStack);
    }

    @Override
    public boolean isValid() {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();
        if (api == null) {
            return false;
        }

        ExcellentCratesItemType type = this.data.type();
        Identifier itemId = this.data.itemId();

        return switch (type) {
            case CRATE -> api.getCratesAPI().registry().contains(itemId);
            case KEY -> api.getKeysAPI().map(keys -> keys.getRegistry().contains(itemId)).orElse(false);
        };
    }

    @Override
    public Optional<ItemStack> itemStack() {
        ExcellentCratesAPI api = ExcellentCratesAPIService.resolve();
        if (api == null) {
            return Optional.empty();
        }

        ExcellentCratesItemType type = this.data.type();
        Identifier itemId = this.data.itemId();

        return switch (type) {
            case CRATE -> {
                Crate crate = api.getCratesAPI().registry().get(itemId);
                if (crate == null) {
                    yield Optional.empty();
                }
                yield Optional.of(api.getCratesAPI().items().createTaggedItem(crate));
            }
            case KEY -> {
                KeysAPI keys = api.getKeysAPI().orElse(null);
                if (keys == null) {
                    yield Optional.empty();
                }

                CrateKey key = keys.getRegistry().get(itemId);
                if (key == null) {
                    yield Optional.empty();
                }

                yield keys.getItemAPI().createTaggedItem(key);
            }
        };
    }
}
