package su.nightexpress.nightcore.ui.dialog.build;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedConfirmationType;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedDialogListType;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedMultiActionType;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedNoticeType;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.WrappedServerLinksType;
import su.nightexpress.nightcore.locale.LangDefaults;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class DialogTypes {

    @NonNull
    public static WrappedConfirmationType confirmation(@NonNull WrappedActionButton yesButton, @NonNull WrappedActionButton noButton) {
        return new WrappedConfirmationType(yesButton, noButton);
    }

    @NonNull
    public static WrappedDialogListType dialogList(@NonNull List<WrappedDialog> dialogs, @Nullable WrappedActionButton exitAction, int columns, int buttonWidth) {
        return dialogList(dialogs).exitAction(exitAction).columns(columns).buttonWidth(buttonWidth).build();
    }

    public static WrappedDialogListType.@NonNull Builder dialogList(@NonNull List<WrappedDialog> dialogs) {
        return new WrappedDialogListType.Builder(dialogs);
    }

    public static WrappedMultiActionType.@NonNull Builder multiAction(@NonNull WrappedActionButton... actions) {
        return multiAction(Lists.newList(actions));
    }

    public static WrappedMultiActionType.@NonNull Builder multiAction(@NonNull List<WrappedActionButton> actions) {
        return new WrappedMultiActionType.Builder(actions);
    }

    @NonNull
    public static WrappedNoticeType notice() {
        return notice(DialogButtons.action(TagWrappers.LANG.apply(LangDefaults.GAME_GUI_OK)).build());
    }

    @NonNull
    public static WrappedNoticeType notice(@NonNull WrappedActionButton action) {
        return new WrappedNoticeType(action);
    }

    @NonNull
    public WrappedServerLinksType serverLinks(int columns, int buttonWidth) {
        return serverLinks(null, columns, buttonWidth);
    }

    @NonNull
    public WrappedServerLinksType serverLinks(@Nullable WrappedActionButton exitAction, int columns, int buttonWidth) {
        return new WrappedServerLinksType(exitAction, columns, buttonWidth);
    }
}
