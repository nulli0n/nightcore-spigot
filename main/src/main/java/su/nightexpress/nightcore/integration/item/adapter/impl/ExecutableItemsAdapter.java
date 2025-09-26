package su.nightexpress.nightcore.integration.item.adapter.impl;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.adapter.IdentifiableItemAdapter;

import java.util.Optional;

public class ExecutableItemsAdapter extends IdentifiableItemAdapter {

    public ExecutableItemsAdapter() {
        super("executableitems");
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        ExecutableItemInterface itemInterface = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(itemId).orElse(null);
        return itemInterface == null ? null : itemInterface.buildItem(1, Optional.empty());
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(itemStack).isPresent();
    }

    @Override
    public boolean canHandle(@NotNull ItemIdData data) {
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(data.getItemId());
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        return ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(itemStack).map(ExecutableItemInterface::getId).orElse(null);
    }
}
