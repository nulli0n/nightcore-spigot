package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.*;

public interface DialogTypeAdapter<T> {

    @NonNull
    T adaptType(@NonNull WrappedDialogType type);

    @NonNull
    T adaptType(@NonNull WrappedConfirmationType type);

    @NonNull
    T adaptType(@NonNull WrappedDialogListType type);

    @NonNull
    T adaptType(@NonNull WrappedMultiActionType type);

    @NonNull
    T adaptType(@NonNull WrappedNoticeType type);

    @NonNull
    T adaptType(@NonNull WrappedServerLinksType type);
}
