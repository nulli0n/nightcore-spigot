package su.nightexpress.nightcore.integration.item.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;
import su.nightexpress.nightcore.integration.item.adapter.impl.VanillaItemAdapter;
import su.nightexpress.nightcore.util.LowerCase;

public abstract class AbstractItemAdapter<T> implements ItemAdapter<T> {

    protected final String name;

    public AbstractItemAdapter(@NotNull String name) {
        this.name = LowerCase.INTERNAL.apply(name);
    }

    @Nullable
    public abstract AdaptedItemStack<T> readItem(@NotNull FileConfig config, @NotNull String path);

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isVanilla() {
        return this == VanillaItemAdapter.INSTANCE;
    }
}
