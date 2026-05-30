package su.nightexpress.nightcore.integration.item.adapter;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.impl.AdaptedCustomStack;

import java.util.Optional;

public abstract class IdentifiableItemAdapter extends AbstractItemAdapter<ItemIdData> {

    public IdentifiableItemAdapter(@NonNull String name) {
        super(name);
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    @Nullable
    public AdaptedCustomStack readItem(@NonNull FileConfig config, @NonNull String path) {
        ItemIdData idData = ItemIdData.read(config, path);
        return new AdaptedCustomStack(this, idData);
    }

    @Override
    @NonNull
    public Optional<AdaptedCustomStack> adapt(@NonNull ItemStack itemStack) {
        ItemIdData data = this.fromItemStack(itemStack);
        if (data == null) return Optional.empty();

        return Optional.of(new AdaptedCustomStack(this, data));
    }

    @Nullable
    public abstract String getItemId(@NonNull ItemStack itemStack);

    @Nullable
    public abstract ItemStack createItem(@NonNull String itemId);

    @Override
    @Nullable
    public ItemStack toItemStack(@NonNull ItemIdData data) {
        ItemStack itemStack = this.createItem(data.getItemId());
        if (itemStack == null) return null;

        itemStack.setAmount(data.getAmount());
        return itemStack;
    }

    @Override
    @Nullable
    public ItemIdData fromItemStack(@NonNull ItemStack itemStack) {
        String id = this.getItemId(itemStack);
        if (id == null) return null;

        return new ItemIdData(id, itemStack.getAmount());
    }
}
