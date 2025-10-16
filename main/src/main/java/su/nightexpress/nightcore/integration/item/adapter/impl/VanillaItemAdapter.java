package su.nightexpress.nightcore.integration.item.adapter.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.adapter.AbstractItemAdapter;
import su.nightexpress.nightcore.integration.item.impl.AdaptedVanillaStack;
import su.nightexpress.nightcore.util.ItemTag;

import java.util.Optional;

public class VanillaItemAdapter extends AbstractItemAdapter<ItemTag> {

    public static final VanillaItemAdapter INSTANCE = new VanillaItemAdapter();

    private VanillaItemAdapter() {
        super("vanilla");
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    @Nullable
    public AdaptedVanillaStack readItem(@NotNull FileConfig config, @NotNull String path) {
        ItemTag itemTag = ItemTag.read(config, path);
        return new AdaptedVanillaStack(itemTag);
    }

    @Override
    @NotNull
    public Optional<AdaptedVanillaStack> adapt(@NotNull ItemStack itemStack) {
        ItemTag tag = this.fromItemStack(itemStack);
        return Optional.of(new AdaptedVanillaStack(tag));
    }

    @Override
    public boolean canHandle(@NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canHandle(@NotNull ItemTag data) {
        return this.toItemStack(data) != null;
    }

    @Override
    @Nullable
    public ItemStack toItemStack(@NotNull ItemTag data) {
        return data.getItemStack();
    }

    @Override
    @NotNull
    public ItemTag fromItemStack(@NotNull ItemStack itemStack) {
        return ItemTag.of(itemStack);
    }
}
