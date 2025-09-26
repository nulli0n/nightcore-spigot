package su.nightexpress.nightcore.integration.item.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

public class ECratesKeyAdapter extends IdentifiableItemAdapter {

    public ECratesKeyAdapter() {
        super("excellentcrates_key");
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        CrateKey key = CratesAPI.getKeyManager().getKeyByItem(itemStack);
        return key == null ? null : key.getId();
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        CrateKey key = CratesAPI.getKeyManager().getKeyById(itemId);
        return key == null ? null : key.getItem();
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return CratesAPI.getKeyManager().getKeyByItem(itemStack) != null;
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return CratesAPI.getKeyManager().getKeyById(data.getItemId()) != null;
    }
}
