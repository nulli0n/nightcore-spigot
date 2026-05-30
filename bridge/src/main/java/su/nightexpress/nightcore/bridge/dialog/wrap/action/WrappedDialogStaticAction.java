package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;

public record WrappedDialogStaticAction(@NonNull NightClickEvent clickEvent) implements WrappedDialogAction {

    @Override
    @NonNull
    public <A> A adapt(@NonNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
