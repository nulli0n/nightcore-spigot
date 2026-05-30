package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public record WrappedDialogCustomAction(@NonNull String id,
                                        @Nullable NightNbtHolder nbt) implements WrappedDialogAction {

    @Override
    @NonNull
    public <A> A adapt(@NonNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
