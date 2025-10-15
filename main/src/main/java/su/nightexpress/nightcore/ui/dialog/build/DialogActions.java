package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCommandTemplateAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCustomAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogStaticAction;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;

public class DialogActions {

    public static final String OK     = "ok";
    public static final String CANCEL = "cancel";
    public static final String BACK   = "back";

    @NotNull
    public static WrappedDialogStaticAction staticAction(@NotNull NightClickEvent event) {
        return new WrappedDialogStaticAction(event);
    }

    @NotNull
    public static WrappedDialogCommandTemplateAction commandTemplate(@NotNull String template) {
        return new WrappedDialogCommandTemplateAction(template);
    }

    @NotNull
    public static WrappedDialogCustomAction customClick(@NotNull String id) {
        return customClick(id, null);
    }

    @NotNull
    public static WrappedDialogCustomAction customClick(@NotNull String id, @Nullable NightNbtHolder nbt) {
        return new WrappedDialogCustomAction(id, nbt);
    }
}
