package su.nightexpress.nightcore.integration.item.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    public String getItemId(@NotNull ItemStack itemStack) {
        Crate crate = CratesAPI.getCrateManager().getCrateByItem(itemStack);
        return crate == null ? null : crate.getId();
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        Crate crate = CratesAPI.getCrateManager().getCrateById(itemId);
        return crate == null ? null : crate.getItem();
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return CratesAPI.getCrateManager().getCrateByItem(itemStack) != null;
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return CratesAPI.getCrateManager().getCrateById(data.getItemId()) != null;
    }
}
