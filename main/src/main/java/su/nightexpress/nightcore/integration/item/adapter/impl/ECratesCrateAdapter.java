package su.nightexpress.nightcore.integration.item.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class ECratesCrateAdapter extends IdentifiableItemAdapter {

    public ECratesCrateAdapter() {
        super("excellentcrates_crate");
    }

    @Override
    @Nullable
    public String getItemId(@NonNull ItemStack itemStack) {
        if (!CratesAPI.isLoaded()) return null;

        Crate crate = CratesAPI.getCrateManager().getCrateByItem(itemStack);
        return crate == null ? null : crate.getId();
    }

    @Override
    @Nullable
    public ItemStack createItem(@NonNull String itemId) {
        if (!CratesAPI.isLoaded()) return null;

        Crate crate = CratesAPI.getCrateManager().getCrateById(itemId);
        return crate == null ? null : crate.getItemStack();
    }

    @Override
    public boolean canHandle(@NonNull ItemStack itemStack) {
        return CratesAPI.isLoaded() && CratesAPI.getCrateManager().getCrateByItem(itemStack) != null;
    }

    @Override
    public boolean canHandle(@NonNull ItemIdData data) {
        return CratesAPI.isLoaded() && CratesAPI.getCrateManager().getCrateById(data.getItemId()) != null;
    }
}
