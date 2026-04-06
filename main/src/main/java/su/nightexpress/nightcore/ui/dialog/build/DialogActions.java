package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCommandTemplateAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCustomAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogStaticAction;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;

public class DialogActions {

    public static final String OK     = "ok";
    public static final String CANCEL = "cancel";
    public static final String APPLY  = "apply";
    public static final String RESET  = "reset";
    public static final String BACK   = "back";

    @NonNull
    public static WrappedDialogStaticAction staticAction(@NonNull NightClickEvent event) {
        return new WrappedDialogStaticAction(event);
    }

    @NonNull
    public static WrappedDialogCommandTemplateAction commandTemplate(@NonNull String template) {
        return new WrappedDialogCommandTemplateAction(template);
    }

    @NonNull
    public static WrappedDialogCustomAction customClick(@NonNull String id) {
        return customClick(id, null);
    }

    @NonNull
    public static WrappedDialogCustomAction customClick(@NonNull String id, @Nullable NightNbtHolder nbt) {
        return new WrappedDialogCustomAction(id, nbt);
    }
}
