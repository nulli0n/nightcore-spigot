package su.nightexpress.nightcore.bridge.dialog.wrap.action;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogActionAdapter;

public record WrappedDialogCommandTemplateAction(@NotNull String template) implements WrappedDialogAction {

    @Override
    @NotNull
    public <A> A adapt(@NotNull DialogActionAdapter<A> adapter) {
        return adapter.adaptAction(this);
    }
}
