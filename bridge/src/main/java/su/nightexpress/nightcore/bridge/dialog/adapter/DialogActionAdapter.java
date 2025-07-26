package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCommandTemplateAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCustomAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogStaticAction;

public interface DialogActionAdapter<A> {

    @NotNull A adaptAction(@NotNull WrappedDialogAction action);

    @NotNull A adaptAction(@NotNull WrappedDialogStaticAction action);

    @NotNull A adaptAction(@NotNull WrappedDialogCustomAction action);

    @NotNull A adaptAction(@NotNull WrappedDialogCommandTemplateAction action);
}
