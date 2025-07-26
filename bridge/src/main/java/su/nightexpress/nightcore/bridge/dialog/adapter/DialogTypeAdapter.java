package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.*;

public interface DialogTypeAdapter<T> {

    @NotNull T adaptType(@NotNull WrappedDialogType type);

    @NotNull T adaptType(@NotNull WrappedConfirmationType type);

    @NotNull T adaptType(@NotNull WrappedDialogListType type);

    @NotNull T adaptType(@NotNull WrappedMultiActionType type);

    @NotNull T adaptType(@NotNull WrappedNoticeType type);

    @NotNull T adaptType(@NotNull WrappedServerLinksType type);
}
