package su.nightexpress.nightcore.integration.item.impl;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.adapter.AbstractItemAdapter;

import java.util.Optional;

public abstract class AdaptedItemStack<T> implements AdaptedItem, Writeable {

    protected final ItemAdapter<T> adapter;
    protected final T              data;

    public AdaptedItemStack(@NonNull ItemAdapter<T> adapter, @NonNull T data) {
        this.adapter = adapter;
        this.data = data;
    }

    @Nullable
    public static AdaptedItemStack<?> read(@NonNull FileConfig config, @NonNull String path) {
        String name = config.getString(path + ".Provider", "null");
        AbstractItemAdapter<?> adapter = (AbstractItemAdapter<?>) ItemBridge.getAdapter(name); // Need FileConfig bridge
        if (adapter == null) return null;

        return adapter.readItem(config, path + ".Data");
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Provider", this.adapter.getName());
        config.set(path + ".Data", this.data);
    }

    @Override
    @NonNull
    public ItemAdapter<T> getAdapter() {
        return this.adapter;
    }

    @NonNull
    public T getData() {
        return this.data;
    }

    @Override
    @NonNull
    public Optional<ItemStack> itemStack() {
        return Optional.ofNullable(this.getItemStack());
    }

    @Override
    public boolean isSimilar(@NonNull ItemStack other) {
        ItemAdapter<?> otherAdapter = ItemBridge.getAdapter(other);
        if (otherAdapter != this.adapter) return false;

        T data = this.adapter.fromItemStack(other);
        return data != null && this.isSimilar(data);
    }

    public abstract boolean isSimilar(@NonNull T other);
}
