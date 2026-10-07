package su.nightexpress.nightcore.integration.item.excellentcrates;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;

@NullMarked
public class ExcellentCratesItem implements AdaptedItem {

    private final ExcellentCratesItemContext context;

    public ExcellentCratesItem(ExcellentCratesItemContext context) {
        this.context = context;
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

        ExcellentCratesItemType type = this.context.type();
        Identifier itemId = this.context.itemId();

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

        ExcellentCratesItemType type = this.context.type();
        Identifier itemId = this.context.itemId();

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
