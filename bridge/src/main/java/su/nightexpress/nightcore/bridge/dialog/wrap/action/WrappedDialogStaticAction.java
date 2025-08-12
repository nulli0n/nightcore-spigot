package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;

public record WrappedDialogStaticAction(@NotNull NightClickEvent clickEvent) implements WrappedDialogAction {

    @Override
    @NotNull
    public <A> A adapt(@NotNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
