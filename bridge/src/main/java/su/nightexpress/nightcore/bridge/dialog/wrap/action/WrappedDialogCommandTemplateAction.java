package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public record WrappedDialogCommandTemplateAction(@NonNull String template) implements WrappedDialogAction {

    @Override
    @NonNull
    public <A> A adapt(@NonNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
