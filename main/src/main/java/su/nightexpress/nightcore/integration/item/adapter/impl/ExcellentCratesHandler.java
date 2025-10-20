package su.nightexpress.nightcore.integration.item.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

@Deprecated
public class ExcellentCratesHandler extends IdentifiableItemAdapter {

    private static final String PREFIX_CRATE = "crate_";
    private static final String PREFIX_KEY   = "key_";

    public ExcellentCratesHandler() {
        super("excellentcrates");
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public boolean canHandle(@NotNull ItemStack item) {
        if (!CratesAPI.isLoaded()) return false;

        return CratesAPI.getCrateManager().isCrate(item) || CratesAPI.getKeyManager().isKey(item);
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        if (!CratesAPI.isLoaded()) return null;

        if (itemId.startsWith(PREFIX_CRATE)) {
            String id = itemId.substring(PREFIX_CRATE.length());
            Crate crate = CratesAPI.getCrateManager().getCrateById(id);
            return crate == null ? null : crate.getItemStack();
        }

        if (itemId.startsWith(PREFIX_KEY)) {
            String id = itemId.substring(PREFIX_KEY.length());
            CrateKey key = CratesAPI.getKeyManager().getKeyById(id);
            return key == null ? null : key.getItemStack();
        }

        return null;
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack item) {
        if (!CratesAPI.isLoaded()) return null;

        Crate crate = CratesAPI.getCrateManager().getCrateByItem(item);
        if (crate != null) return PREFIX_CRATE + crate.getId();

        CrateKey key = CratesAPI.getKeyManager().getKeyByItem(item);
        if (key != null) return PREFIX_KEY + key.getId();

        return null;
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        if (!CratesAPI.isLoaded()) return false;

        String itemId = data.getItemId();
        if (itemId.startsWith(PREFIX_CRATE)) {
            String id = itemId.substring(PREFIX_CRATE.length());
            return CratesAPI.getCrateManager().getCrateById(id) != null;
        }

        if (itemId.startsWith(PREFIX_KEY)) {
            String id = itemId.substring(PREFIX_KEY.length());
            return CratesAPI.getKeyManager().getKeyById(id) != null;
        }

        return false;
    }
}
