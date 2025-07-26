package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public record WrappedDialogCustomAction(@NotNull String id, @Nullable NightNbtHolder nbt) implements WrappedDialogAction {

    @Override
    @NotNull
    public <A> A adapt(@NotNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
