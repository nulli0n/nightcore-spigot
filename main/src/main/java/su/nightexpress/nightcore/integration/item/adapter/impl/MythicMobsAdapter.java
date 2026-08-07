package su.nightexpress.nightcore.integration.item.adapter.impl;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;

public class MythicMobsAdapter extends IdentifiableItemAdapter {

    public MythicMobsAdapter() {
        super("mythicmobs");
    }

    @Override
    @Nullable
    public String getItemId(@NonNull ItemStack itemStack) {
        if (itemStack.getType().isAir()) return null;

        return MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack);
    }

    @Override
    @Nullable
    public ItemStack createItem(@NonNull String itemId) {
        return MythicBukkit.inst().getItemManager().getItemStack(itemId);
    }

    @Override
    public boolean canHandle(@NonNull ItemStack itemStack) {
        return MythicBukkit.inst().getItemManager().isMythicItem(itemStack);
    }

    @Override
    public boolean canHandle(@NonNull ItemIdData data) {
        return MythicBukkit.inst().getItemManager().getItem(data.getItemId()).isPresent();
    }
}
