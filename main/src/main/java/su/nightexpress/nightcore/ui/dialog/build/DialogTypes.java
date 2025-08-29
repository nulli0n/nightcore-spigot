package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.*;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.List;

public class DialogTypes {

    @NotNull
    public static WrappedConfirmationType confirmation(@NotNull WrappedActionButton yesButton, @NotNull WrappedActionButton noButton) {
        return new WrappedConfirmationType(yesButton, noButton);
    }

    @NotNull
    public static WrappedDialogListType dialogList(@NotNull List<WrappedDialog> dialogs, @Nullable WrappedActionButton exitAction, int columns, int buttonWidth) {
        return dialogList(dialogs).exitAction(exitAction).columns(columns).buttonWidth(buttonWidth).build();
    }

    @NotNull
    public static WrappedDialogListType.Builder dialogList(@NotNull List<WrappedDialog> dialogs) {
        return new WrappedDialogListType.Builder(dialogs);
    }

    @NotNull
    public static WrappedMultiActionType.Builder multiAction(@NotNull WrappedActionButton... actions) {
        return multiAction(Lists.newList(actions));
    }

    @NotNull
    public static WrappedMultiActionType.Builder multiAction(@NotNull List<WrappedActionButton> actions) {
        return new WrappedMultiActionType.Builder(actions);
    }

    @NotNull
    public static WrappedNoticeType notice() {
        return notice(DialogButtons.action(TagWrappers.LANG.apply("gui.ok")).width(150).build()); // TODO Config
    }

    @NotNull
    public static WrappedNoticeType notice(@NotNull WrappedActionButton action) {
        return new WrappedNoticeType(action);
    }

    @NotNull
    public WrappedServerLinksType serverLinks(int columns, int buttonWidth) {
        return serverLinks(null, columns, buttonWidth);
    }

    @NotNull
    public WrappedServerLinksType serverLinks(@Nullable WrappedActionButton exitAction, int columns, int buttonWidth) {
        return new WrappedServerLinksType(exitAction, columns, buttonWidth);
    }
}
