package su.nightexpress.nightcore.integration.item.adapter;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.impl.AdaptedCustomStack;

import java.util.Optional;

public abstract class IdentifiableItemAdapter extends AbstractItemAdapter<ItemIdData> {

    public IdentifiableItemAdapter(@NotNull String name) {
        super(name);
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    @Nullable
    public AdaptedCustomStack readItem(@NotNull FileConfig config, @NotNull String path) {
        ItemIdData idData = ItemIdData.read(config, path);
        return new AdaptedCustomStack(this, idData);
    }

    @Override
    @NotNull
    public Optional<AdaptedCustomStack> adapt(@NotNull ItemStack itemStack) {
        ItemIdData data = this.fromItemStack(itemStack);
        if (data == null) return Optional.empty();

        return Optional.of(new AdaptedCustomStack(this, data));
    }

    @Nullable
    public abstract String getItemId(@NotNull ItemStack itemStack);

    @Nullable
    public abstract ItemStack createItem(@NotNull String itemId);

    @Override
    @Nullable
    public ItemStack toItemStack(@NotNull ItemIdData data) {
        ItemStack itemStack = this.createItem(data.getItemId());
        if (itemStack == null) return null;

        itemStack.setAmount(data.getAmount());
        return itemStack;
    }

    @Override
    @Nullable
    public ItemIdData fromItemStack(@NotNull ItemStack itemStack) {
        String id = this.getItemId(itemStack);
        if (id == null) return null;

        return new ItemIdData(id, itemStack.getAmount());
    }
}
